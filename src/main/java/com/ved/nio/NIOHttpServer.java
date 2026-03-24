package com.ved.nio;

import java.net.InetSocketAddress;
import com.ved.http.HttpRequest;
import com.ved.http.HttpResponse;
import com.ved.router.Router;
import com.ved.router.Router.RouteResult;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;

public class NIOHttpServer {
  private int port;

  private HashMap<String, ClientData> requestMap = new HashMap<>();

  private static final int RATE_LIMIT =
        Integer.parseInt(System.getenv().getOrDefault("RATE_LIMIT", "25"));

  public NIOHttpServer(int port) {
    this.port = port;
  }

  public void start() throws Exception {
    System.out.println("Starting NIO server on port " + port);

    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    serverChannel.configureBlocking(false);
    serverChannel.bind(new InetSocketAddress(port));

    Selector selector = Selector.open();
    serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    System.out.println("NIO Server running on port " + port);

    while (true) {

      selector.select();

      Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

      while (keys.hasNext()) {
        SelectionKey key = keys.next();
        keys.remove();

        if (key.isAcceptable()) {
          ServerSocketChannel server = (ServerSocketChannel) key.channel();

          SocketChannel client = server.accept();
          client.configureBlocking(false);

          ByteBuffer buffer = ByteBuffer.allocate(1024);

          client.register(selector, SelectionKey.OP_READ, buffer);
        } else if (key.isReadable()) {
          SocketChannel client = (SocketChannel) key.channel();

          InetSocketAddress address = (InetSocketAddress) client.getRemoteAddress();
          String clientIP = address.getAddress().getHostAddress();

          long currentTime = System.currentTimeMillis();

          ClientData data = requestMap.get(clientIP);

          if (data == null) {
            data = new ClientData(0, currentTime);
            requestMap.put(clientIP, data);
          }

          if (currentTime - data.lastResetTime > 1000) {
            data.count = 0;
            data.lastResetTime = currentTime;
          }

          data.count++;

          boolean allowed = data.count <= RATE_LIMIT;

          ByteBuffer buffer = (ByteBuffer) key.attachment();

          int bytesRead = client.read(buffer);

          if (bytesRead == -1) {
            client.close();
            continue;
          }

          buffer.flip();

          String rawRequest = new String(buffer.array()).trim();
          String requestLine = rawRequest.split("\r\n")[0];
          System.out.println(requestLine);

          String parts[] = requestLine.split(" ");

          String method = parts[0];
          String path = parts[1];
          String protocol = parts[2];

          HttpRequest request = new HttpRequest(method, path, protocol);

          RouteResult result = Router.handle(request, allowed);

          String body = result.body;
          String statusLine = result.statusLine;

          HttpResponse responseObj = new HttpResponse(statusLine, body);
          String response = responseObj.build();

          ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());

          client.write(responseBuffer);

          buffer.clear();

          client.close();
        }
      }
    }
  }
}
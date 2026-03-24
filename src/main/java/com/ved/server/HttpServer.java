package com.ved.server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.ved.http.HttpRequest;

public class HttpServer {
  private int port;

  private ExecutorService threadPool;

  public HttpServer(int port) {
    this.port = port;
    this.threadPool = Executors.newFixedThreadPool(20);
  }

  public void start() throws Exception {
    ServerSocket serverSocket = new ServerSocket(port);
    System.out.println("Server Started on Port : " + port);

    while (true) {
      Socket client = serverSocket.accept();

      threadPool.submit(() -> {
        try {
          handleClient(client);
        } catch(Exception e) {
          e.printStackTrace();
        }
      });
    }
  }

  private void handleClient(Socket client) throws Exception {
    InputStream input = client.getInputStream();
    OutputStream output = client.getOutputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

    String requestLine = reader.readLine();

    if(requestLine == null || requestLine.isEmpty()){
      client.close();
      return;
    }

    System.out.println(requestLine);

    String parts[] = requestLine.split(" ");

    String method = parts[0];
    String path = parts[1];
    String protocol = parts[2];

    HttpRequest request = new HttpRequest(method, path, protocol);

    String body;

    if(request.getPath().equals("/hello")) body = "Hello Endpoint";
    else if(request.getPath().equals("/health")) body = "Server is Healthy";
    else body = "404 Not Found";

    String response =
            "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/plain\r\n" +
            "Content-Length: " + body.length() + "\r\n" +
            "\r\n" +
            body;

    output.write(response.getBytes());
    output.flush();

    client.close();
  }
}
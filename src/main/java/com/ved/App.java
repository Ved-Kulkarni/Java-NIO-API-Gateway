package com.ved;

//import com.ved.server.HttpServer;
import com.ved.nio.NIOHttpServer;

public class App 
{
    public static void main(String[] args) throws Exception{
        //HttpServer server = new HttpServer(9090);
        NIOHttpServer server = new NIOHttpServer(9091);
        server.start();
    }
}
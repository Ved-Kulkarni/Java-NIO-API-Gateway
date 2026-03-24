package com.ved.router;

import com.ved.http.HttpRequest;

public class Router {

    public static class RouteResult {
        public String body;
        public String statusLine;

        public RouteResult(String body, String statusLine) {
            this.body = body;
            this.statusLine = statusLine;
        }
    }

    public static RouteResult handle(HttpRequest request, boolean allowed) {

        String path = request.getPath();

        if (path.equals("/hello")) {
            return new RouteResult("Hello Endpoint\r\n", "HTTP/1.1 200 OK\r\n");
        }

        else if (path.equals("/health")) {
            return new RouteResult("Server is Healthy\r\n", "HTTP/1.1 200 OK\r\n");
        }

        else if (path.equals("/api")) {

            if (allowed) {
                return new RouteResult("API Gateway Hit!\r\n", "HTTP/1.1 200 OK\r\n");
            } 
            else {
                return new RouteResult("Too Many Requests\r\n", "HTTP/1.1 429 Too Many Requests\r\n");
            }
        }

        else {
            return new RouteResult("404 Not Found\r\n", "HTTP/1.1 404 Not Found\r\n");
        }
    }
}
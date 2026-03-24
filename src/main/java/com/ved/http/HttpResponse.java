package com.ved.http;

public class HttpResponse {

  private String statusLine;
  private String body;

  public HttpResponse(String statusLine, String body) {
    this.statusLine = statusLine;
    this.body = body;
  }

  public String build() {
    return statusLine +
          "Content-Type: text/plain\r\n" +
          "Content-Length: " + body.length() + "\r\n" +
          "\r\n" +
          body;
  }
}
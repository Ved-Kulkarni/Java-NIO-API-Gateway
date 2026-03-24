package com.ved.http;

public class HttpRequest {
  private String method;
  private String path;
  private String protocol;
  
  public HttpRequest(String method, String path, String protocol){
    this.method = method;
    this.path = path;
    this.protocol = protocol;
  }

  public String getMethod(){
    return method;
  }

  public String getPath(){
    return path;
  }

  public String getProtocol(){
    return protocol;
  }
}
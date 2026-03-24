package com.ved.nio;

public class ClientData {

  public int count;
  public long lastResetTime;

  public ClientData(int count, long lastResetTime){
    this.count = count;
    this.lastResetTime = lastResetTime;
  }
}

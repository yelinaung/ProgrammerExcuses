package com.yelinaung.programmerexcuses.event;

/**
 * Created by Ye Lin Aung on 14/08/28.
 */
public class QuoteDownloadFailEvent {
  public String failMsg;

  public QuoteDownloadFailEvent(String msg) {
    this.failMsg = msg;
  }
}

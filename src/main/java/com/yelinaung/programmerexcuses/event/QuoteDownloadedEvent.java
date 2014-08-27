package com.yelinaung.programmerexcuses.event;

/**
 * Created by Ye Lin Aung on 14/08/27.
 */
public class QuoteDownloadedEvent {
  public final String message;

  public QuoteDownloadedEvent(String msg) {
    this.message = msg;
  }
}

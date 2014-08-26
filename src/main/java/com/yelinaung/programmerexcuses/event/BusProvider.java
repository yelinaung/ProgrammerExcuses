package com.yelinaung.programmerexcuses.event;

import com.squareup.otto.Bus;

/**
 * Created by Ye Lin Aung on 14/08/26.
 */
public class BusProvider {
  private static final Bus BUS = new Bus();

  public BusProvider() {
  }

  public static Bus getInstance() {
    return BUS;
  }
}

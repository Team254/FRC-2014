package com.team254.lib.util;

/**
 * Latches a boolean.
 *
 * @author richard@team254.com (Richard Lin)
 */
public class Latch {
  private boolean lastVal;

  public boolean update(boolean newVal) {
    boolean result = newVal && !lastVal;
    lastVal = newVal;
    return result;
  }
}

package com.team254.lib;

/**
 * SignalUtils.java
 *
 * @author tombot
 */
public class SignalUtils {

  public static double cap(double signal, double max) {
    if (signal < -max)
      return -max;
    else if (signal > max)
      return max;
    return signal;
  }
}

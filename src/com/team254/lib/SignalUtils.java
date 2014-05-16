package com.team254.lib;

/**
 * SignalUtils.java
 * Some math used in the StateSpace classes
 * @author Tom Bottiglieri
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

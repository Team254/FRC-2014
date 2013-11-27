package com.team254.lib.util;

/**
 * Contains math functions for generating square and sine waves.
 *
 * @author articgrayling8x8@gmail.com (Dorian Chan)
 */
public class Functions {
  /**
   * Calculates the sine wave and returns it.
   */
  public static double sineWave(double timeSec, double periodSec, double amplitude) {
    return Math.sin(timeSec * 2 * Math.PI / periodSec) * amplitude;
  }

  /**
   * Calculates the square wave and returns the correct amplitude.
   */
  public static double squareWave(double timeSec, double periodSec, double amplitude) {
    int periodMs = (int)(periodSec * 1000);
    int incrementTimeMs = (int)(timeSec * 1000) % periodMs;
    if (incrementTimeMs < periodMs / 2) {
      return amplitude;
    } else {
      return -amplitude;
    }
  }
}

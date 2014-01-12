package com.team254.lib.util;

import edu.wpi.first.wpilibj.Timer;

/**
 * Limits the rate of printing to one message per a configured period.
 *
 * @author richard@team254.com (Richard Lin)
 */
public class ThrottledPrinter {
  private double periodSec;
  private double lastPrintTimeSec = 0;

  public ThrottledPrinter(double periodSec) {
    this.periodSec = periodSec;
  }

  public void println(String text) {
    if (Timer.getFPGATimestamp() - lastPrintTimeSec >= periodSec) {
      System.out.println(text);
      lastPrintTimeSec = Timer.getFPGATimestamp();
    }
  }
}

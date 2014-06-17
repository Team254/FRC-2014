package com.team254.frc2014.actions;

import com.team254.lib.util.ThrottledPrinter;

/**
 * This Action Drives the robot until it approaches a valid ultrasonic distance.
 * @author spinkerton
 */
public class DriveToUltrasonicRangeAction extends Action {

  double lastGoodValue;
  double range;
  ThrottledPrinter p = new ThrottledPrinter(.125);
  public DriveToUltrasonicRangeAction(double range) {
    this.range = range;
    this.lastGoodValue = 10;
  }

  public boolean execute() {
    double value = drivebase.getUltrasonicDistance(); // feet
    if (isGoodValue(value)) {
      lastGoodValue = value;
    }
    double error = (lastGoodValue - range);
    p.println("" + error);
    return false;
  }

  private boolean isGoodValue(double value) {
    return value < 150.0/12.0 && value > 5.0/12.0;
  }

  public void init() {
  }

  public void done() {
  }
}

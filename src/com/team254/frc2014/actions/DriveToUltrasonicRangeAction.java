/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.frc2014.actions;

import com.team254.lib.util.ThrottledPrinter;

/**
 *
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
    //drivebase.setLeftRightPower(0, 0);
    return false; //driveController.distanceGoalSettled();
  }

  private boolean isGoodValue(double value) {
    return value < 150.0/12.0 && value > 5.0/12.0;
  }

  public void init() {
  }

  public void done() {
  }
}

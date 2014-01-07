package com.team254.frc2014.actions;

import com.team254.frc2014.Action;

public class DriveAction extends Action {

  double distance;
  public DriveAction(double distance, double timeout) {
    this.distance = distance;
    setTimeout(timeout);
  }
  public boolean execute() {
    return isTimedOut();
  }

  public void init() {
    drivebase.setLeftRightPower(.25, .25); // Change this later
  }

  public void done() {
    drivebase.setLeftRightPower(0, 0);
  }
  
}

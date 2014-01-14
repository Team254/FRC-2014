package com.team254.frc2014.actions;

public class DriveAction extends Action {

  double distance;
  boolean doStop;
  public DriveAction(double distance, boolean doStop, double timeout) {
    this.distance = distance;
    this.doStop = doStop;
    setTimeout(timeout);
  }
  
  public DriveAction(double distance, double timeout) {
    this(distance, true, timeout);
  }
  public boolean execute() {
    return isTimedOut() || 
            (!doStop && driveController.distanceGoalReached()) ||
            (doStop && driveController.distanceGoalSettled()) ;
  }

  public void init() {
    driveController.setDistanceGoal(distance);
  }

  public void done() {
    drivebase.setLeftRightPower(0, 0);
  }
  
}

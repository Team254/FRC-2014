package com.team254.frc2014.actions;

/**
 * SSDriveAction.java
 *
 * @author tombot
 */
public class SSDriveAction extends Action {

  public boolean execute() {
    return false;
  }

  public void init() {
    drivebase.getLeftEncoder().reset();
    drivebase.getRightEncoder().reset();
    drivebase.resetGyro();
    ssDriveController.setGoal(.5, 0, .5, 0);
    ssDriveController.enable();
    driveController.disable();
  }

  public void done() {
    ssDriveController.disable();
  }

}

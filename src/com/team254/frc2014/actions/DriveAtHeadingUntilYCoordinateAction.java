package com.team254.frc2014.actions;

/**
 * DriveAtHeadingUntilYCoordinateAction.java
 *
 * @author tombot
 */
public class DriveAtHeadingUntilYCoordinateAction extends Action {

  private double y, heading;
  private boolean approachPos = false;

  public DriveAtHeadingUntilYCoordinateAction(double wantedHeading, double wantedYmin, double timeout) {
    y = wantedYmin;
    heading = wantedHeading;
    setTimeout(timeout);
  }

  public boolean execute() {
    boolean done;
    if (approachPos) {
      done = driveController.navigator.getY() > y;
    } else {
      done = driveController.navigator.getY() < y;
    }
    return done || isTimedOut();
  }

  public void init() {

    if (driveController.navigator.getY() < y) {
      approachPos = true;
    }
    driveController.setHeadingGoal(heading);
    driveController.setDistanceGoal(50 * (approachPos ? 1.0 : -1.0)); // random distance?
  }

  public void done() {
  }
}

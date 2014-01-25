package com.team254.frc2014.actions;

/**
 * DriveAtHeadingUntilXCoodinateAction.java
 *
 * @author tombot
 */
public class DriveAtHeadingUntilXCoordinateAction extends Action {

  private double x, heading;
  private boolean approachPos = false;

  public DriveAtHeadingUntilXCoordinateAction(double wantedHeading, double wantedXmin, double timeout) {
    x = wantedXmin;
    heading = wantedHeading;
    setTimeout(timeout);
  }

  public boolean execute() {
    boolean done;
    if (approachPos) {
      done = navigator.getX() > x;
    } else {
      done = navigator.getX() < x;
    }
    return done || isTimedOut();
  }

  public void init() {
    if (navigator.getX() < x) {
      approachPos = true;
    }
  }

  public void done() {
  }
}

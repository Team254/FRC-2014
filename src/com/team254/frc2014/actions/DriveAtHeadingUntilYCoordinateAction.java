package com.team254.frc2014.actions;

/**
 * DriveAtHeadingUntilYCoordinateAction.java
 * This class Drives the robot until it reaches a certain Y.
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
    boolean done = approachPos? navigator.getY() > y: navigator.getY() < y;
    return done || isTimedOut();
  }

  public void init() {

    if (navigator.getY() < y) {
      approachPos = true;
    }
  }

  public void done() {
  }
}

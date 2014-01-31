package com.team254.frc2014.actions;

/**
 * DriveAtHeadingUntilXCoodinateAction.java
 * This class Drives the robot until it reaches a certain X.
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
    boolean done = approachPos? navigator.getX() > x: navigator.getX() < x;
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

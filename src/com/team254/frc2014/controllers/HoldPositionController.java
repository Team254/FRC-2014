package com.team254.frc2014.controllers;

import com.team254.lib.Controller;
import com.team254.lib.trajectory.Angles;

/**
 * HoldHeadingController.java
 * This holds the robot in place.
 * @author tombot
 */
public class HoldPositionController extends Controller {

  private double kTurn = 1.0 / 15.0;
  private double kDrive = .85;
  private double heading = 0;
  private double distance = 0;
  
  public void setHeading(double h) {
    heading = h;
  }
  public void setDistance(double d) {
    distance = d;
  }

  public void update() {
    if (!enabled)
      return;
    double angleDiff = Angles.boundAngleNeg180to180Degrees(heading - drivebase.getGyroAngle());
    double turn = kTurn * angleDiff;
    double speed = (distance - drivebase.getAverageDistance()) * kDrive;
    drivebase.setLeftRightPower(speed + turn, speed - turn);
  }
}

package com.team254.frc2014.controllers;

import com.team254.frc2014.ChezyRobot;
import com.team254.frc2014.subsystems.Drivebase;
import com.team254.lib.Controller;

/**
 * DriveController.java
 *
 * @author tombot
 */
public class DriveController extends Controller {
  Drivebase drive = ChezyRobot.drivebase;
  boolean enabled = false;

  double goalDistance = 0;
  double goalHeading = 0;
  double maxAcceleration = Integer.MAX_VALUE;
  double maxVelocity = Integer.MAX_VALUE;
  
  double lastState = 0;
  
  public void setGoals(double distance, double heading, double acceleration, double velocity) {
    goalDistance = distance;
    goalHeading = heading;
    maxAcceleration = acceleration;
    maxVelocity = velocity;
  }
  
  public void reset() {
    drive.getLeftEncoder().reset();
    drive.getRightEncoder().reset();
  }

  public void update() {
    if (!enabled) {
      return;
    }
    // get current state
    double state = (drive.getLeftEncoderDistance() + drive.getRightEncoderDistance()) / 2.0;
  }

}

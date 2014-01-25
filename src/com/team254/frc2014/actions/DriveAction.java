package com.team254.frc2014.actions;

import com.team254.frc2014.Constants;
import edu.missdaisy.utilities.Trajectory;

public class DriveAction extends Action {

  double distance;
  boolean doStop;
  double heading;
  double maxVel = 13.0;
  double maxAcc = 15.0;
  double maxJerk = 15.0 * 5;

  public DriveAction(double distance, double heading, boolean doStop, double timeout) {
    this.distance = distance;
    this.doStop = doStop;
    this.heading = heading;
    setTimeout(timeout);
  }

  public DriveAction(double distance, double timeout) {
    this(distance, 0, true, timeout);
  }

  public boolean execute() {
    return isTimedOut() || driveController.onTarget();
  }

  public void init() {
    drivebase.resetEncoders();
    double width = Constants.robotWidth.getDouble();
    double curHeading = drivebase.getGyroAngle();
    double deltaHeading = heading - curHeading;
    double radius = Math.abs(Math.abs(distance) / (deltaHeading * Math.PI / 180.0));
 
    System.out.println("Generating trajectory...");
    Trajectory.getInstance().generate(Math.abs(distance), maxVel, maxAcc, maxJerk, curHeading, heading, Constants.robotDt.getDouble());
    System.out.println("Finished");

    Trajectory leftProfile = Trajectory.getInstance();
    Trajectory rightProfile = new Trajectory(Trajectory.getInstance()); // Copy

    double faster = (radius + (width / 2.0)) / radius;
    double slower = (radius - (width / 2.0)) / radius;
    System.out.println("faster " + faster);

    if (heading > 0) {
      leftProfile.scale(faster);
      rightProfile.scale(slower);
    } else {
      leftProfile.scale(slower);
      rightProfile.scale(faster);
    }
    driveController.loadProfile(leftProfile, rightProfile, (distance > 0.0 ? 1.0 : -1.0), heading);
    drivebase.useController(driveController);
    driveController.enable();
  }

  public void done() {
    driveController.disable();
    drivebase.setLeftRightPower(0, 0);
  }
}

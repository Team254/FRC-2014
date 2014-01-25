package com.team254.frc2014.actions;

import edu.missdaisy.utilities.Trajectory;

public class DriveAction extends Action {

  double distance;
  boolean doStop;
  double heading;

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

    //drivebase.resetEncoders();
    double width = 25.5 / 12.0;
    double radius = Math.abs(distance) / (heading * Math.PI / 180.0);
    double maxVel = 13.0;
    double maxAcc = 15.0;
    double maxJerk = 15.0 * 5;
    double curHeading = drivebase.getGyroAngle();
    System.out.println("Generating trajectory...");
    Trajectory.getInstance().generate(Math.abs(distance), maxVel, maxAcc, maxJerk, curHeading, curHeading + heading, 1.0 / 100.0);
    System.out.println("Finished");

    Trajectory leftProfile = Trajectory.getInstance();
    Trajectory rightProfile = new Trajectory(Trajectory.getInstance());

    double faster = (radius + (width / 2.0)) / radius;
    double slower = (radius - (width / 2.0)) / radius;

    if (heading > 0) {
      leftProfile.scale(faster);
      rightProfile.scale(slower);
    } else {
      leftProfile.scale(slower);
      rightProfile.scale(faster);
    }
    driveController.loadProfile(leftProfile, rightProfile, (distance > 0.0 ? 1.0 : -1.0), 0);
    driveController.enable();

  }

  public void done() {
    driveController.disable();
    drivebase.setLeftRightPower(0, 0);
  }
}

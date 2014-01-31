package com.team254.frc2014.actions;

import com.team254.frc2014.Constants;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryGenerator;

/**
 * DriveAction tells the robot to go a certain distance in a direction along a global system.
 *
 */
public class DriveAction extends Action {

  double distance;
  boolean doStop;
  double heading;
  double maxVel = 15.0;
  double maxAcc = 16.0;
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
    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = Constants.robotDt.getDouble();
    config.max_acc = maxAcc;
    config.max_jerk = maxJerk;
    config.max_vel = maxVel;
    
    // If you change this to TrapezoidalStrategy, you can use nonzero start and
    // goal velocities.
    TrajectoryGenerator.Strategy strategy = TrajectoryGenerator.SCurvesStrategy;
    Trajectory reference = TrajectoryGenerator.generate(
            config,
            strategy,
            0.0, // start velocity
            curHeading,
            Math.abs(distance),
            0.0, // goal velocity
            heading);
    System.out.println("Finished");

    Trajectory leftProfile = reference;
    Trajectory rightProfile = reference.copy(); // Copy

    double faster = (radius + (width / 2.0)) / radius;
    double slower = (radius - (width / 2.0)) / radius;
    System.out.println("faster " + faster);

    if (deltaHeading > 0) {
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

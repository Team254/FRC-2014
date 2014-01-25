package com.team254.frc2014.actions;

import edu.missdaisy.utilities.DaisyMath;
import edu.missdaisy.utilities.Trajectory;

public class DriveAction extends Action {

  double distance;
  boolean doStop;

  public DriveAction(double distance, boolean doStop, double timeout) {
    this.distance = distance;
    this.doStop = doStop;
    setTimeout(timeout);
  }

  public DriveAction(double distance, double timeout) {
    this(distance, true, timeout);
  }

  public boolean execute() {
    return isTimedOut() || driveController.onTarget();
  }

  public void init() {

    //drivebase.resetEncoders();

    double maxVel = 13.0;
    double maxAcc = 15.0;
    double maxJerk = 15.0 * 5;

    System.out.println("Generating trajectory...");
    Trajectory.getInstance().generate(Math.abs(distance), maxVel, maxAcc, maxJerk, 1.0 / 100.0);
    System.out.println("Finished");


    driveController.loadProfile(Trajectory.getInstance(), (distance > 0.0 ? 1.0 : -1.0), 0);
    driveController.enable();

  }

  public void done() {
    driveController.disable();
    drivebase.setLeftRightPower(0, 0);
  }
}

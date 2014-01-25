package com.team254.frc2014.controllers;

import com.team254.lib.Controller;
import edu.missdaisy.utilities.Trajectory;
import edu.missdaisy.utilities.TrajectoryFollower;

/**
 * DriveController.java
 *
 * @author tombot
 */
public class DriveController extends Controller {

  Trajectory trajectory;
  TrajectoryFollower follower = new TrajectoryFollower();
  double direction;
  double heading;

  public boolean onTarget() {
    return follower.isFinishedTrajectory();// && mFollower.onTarget(distanceThreshold);
  }

  public void init() {

    follower.configure(0, 0, 0, 0.06666666666667, 0);
  }

  public void loadProfile(Trajectory profile, double direction, double heading) {
    reset();
    follower.setTrajectory(profile);
    this.direction = direction;
    this.heading = heading;
  }

  public void reset() {
    follower.reset();
    drivebase.resetEncoders();
  }

  public void update() {
    if (!enabled) {
      return;
    }
    //System.out.println(this.onTarget() + " " + mFollower.isFinishedTrajectory() + " " + mFollower.onTarget(1.0));
    if (onTarget()) {
      drivebase.setLeftRightPower(0.0, 0.0);
    } else {
      double distance = direction * drivebase.getAverageDistance();
      //double angleDiff = DaisyMath.boundAngleNeg180to180Degrees(heading - drivebase.getGyroAngle());

      double speed = direction * follower.calculate(distance);
      //double turn = kTurn * angleDiff;
      double turn = 0;
      drivebase.driveSpeedTurn(speed, turn);
    }
  }

  public void setTrajectory(Trajectory t) {
    this.trajectory = t;
  }
}

package com.team254.frc2014.controllers;

import com.team254.lib.Controller;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.ChezyMath;
/**
 * TrajectoryDriveController.java
 * This controller drives the robot along a specified trajectory.
 * @author tombot
 */
public class TrajctoryDriveController extends Controller {

  public TrajctoryDriveController() {
    init();
  }
  Trajectory trajectory;
  TrajectoryFollower followerLeft = new TrajectoryFollower();
  TrajectoryFollower followerRight = new TrajectoryFollower();
  double direction;
  double heading;
  double kTurn = -1.0/21.0;

  public boolean onTarget() {
    return followerLeft.isFinishedTrajectory(); //mFollower.onTarget(distanceThreshold);
  }

  private void init() {
    followerLeft.configure(.8, 0, 0, 0.06666666666667, 1.0/45.0);
    followerRight.configure(.8, 0, 0, 0.06666666666667, 1.0/45.0);
  }

  public void loadProfile(Trajectory leftProfile, Trajectory rightProfile, double direction, double heading) {
    reset();
    followerLeft.setTrajectory(leftProfile);
    followerRight.setTrajectory(rightProfile);
    this.direction = direction;
    this.heading = heading;
  }

  public void reset() {
    followerLeft.reset();
    followerRight.reset();
    drivebase.resetEncoders();
  }

  public void update() {
    if (!enabled) {
      return;
    }

    if (onTarget()) {
      drivebase.setLeftRightPower(0.0, 0.0);
    } else  {
      double distanceL = direction * drivebase.getLeftEncoderDistance();
      double distanceR = direction * drivebase.getRightEncoderDistance();

      double speedLeft = direction * followerLeft.calculate(distanceL);
      double speedRight = direction * followerRight.calculate(distanceR);
      
      double goalHeading = followerLeft.getHeading();
      double observedHeading =  drivebase.getGyroAngleInRadians();

      double angleDiffRads = ChezyMath.getDifferenceInAngleRadians(observedHeading, goalHeading);// different coordinates
      double angleDiff = Math.toDegrees(angleDiffRads);
      
      System.out.println("goal: " + goalHeading + " real : " + observedHeading);
      double turn = kTurn * angleDiff;
      drivebase.setLeftRightPower(speedLeft + turn, speedRight - turn);
    }
  }

  public void setTrajectory(Trajectory t) {
    this.trajectory = t;
  }
}

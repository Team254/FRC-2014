package com.team254.frc2014.controllers;

import com.team254.frc2014.ChezyRobot;
import com.team254.frc2014.subsystems.Drivebase;
import com.team254.frc2014.subsystems.Navigator;
import com.team254.lib.Controller;
import com.team254.lib.SignalUtils;
import com.team254.lib.util.ThrottledPrinter;

/**
 * DriveController.java
 *
 * @author tombot
 */
public class DriveController extends Controller {

  Drivebase drive = ChezyRobot.drivebase;
  double goalDistance = 0;
  double goalHeading = 0;
  double maxAcceleration = Integer.MAX_VALUE;
  double maxVelocity = Integer.MAX_VALUE;
  double lastState = 0;
  public Navigator navigator = new Navigator();
  ThrottledPrinter p = new ThrottledPrinter(.25);
  private double leftOffset = 0;
  private double rightOffset = 0;
  private double velocity = 0;
  private double lastDistanceState = 0;

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

  private double getDistanceState() {
    return ((drive.getLeftEncoderDistance() - leftOffset) + (drive.getRightEncoderDistance() - rightOffset)) / 2.0;
  }

  public void update() {
    navigator.update(drive.getLeftEncoderDistance(), drive.getRightEncoderDistance(), drive.getGyroAngle());

    if (!enabled) {
      return;
    }

    // get current state
    double state = getDistanceState();
    double push = (goalDistance - getDistanceState()) * .9;

    push = SignalUtils.cap(push, .5);

    double diff = state - lastDistanceState;
    velocity = diff * 100.0;
    lastDistanceState = state;

    double headingError = goalHeading - drive.getGyroAngle();
    double turn = headingError * 0.0145;
    turn = SignalUtils.cap(turn, 1.0);

    drive.setLeftRightPower(push + turn, push - turn);

  }

  private void resetDistanceReference() {
    leftOffset = drive.getLeftEncoderDistance();
    rightOffset = drive.getRightEncoderDistance();
    velocity = 0;
    lastDistanceState = getDistanceState();
  }

  public void setHeadingGoal(double headingGoal) {
    goalHeading = headingGoal;
  }

  public void setDistanceGoal(double distanceGoal) {
    goalDistance = distanceGoal;
    resetDistanceReference();
  }

  public boolean distanceGoalReached() {
    return Math.abs(goalDistance - getDistanceState()) < .1;
  }

  public boolean distanceGoalSettled() {
    return Math.abs(goalDistance - getDistanceState()) < .1 && Math.abs(velocity) < .2;
  }
}

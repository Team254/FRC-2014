package com.team254.frc2014.controllers;

import com.team254.frc2014.subsystems.Drivebase;
import com.team254.lib.StateSpaceController;
import com.team254.lib.StateSpaceGains;
import com.team254.lib.util.Matrix;
import com.team254.lib.util.ThrottledPrinter;

/**
 * StateSpaceDriveController.java
 *
 * @author tombot
 */
public class StateSpaceDriveController extends StateSpaceController {
  double widthInches = 22.0;
  double width = (widthInches/ 100.0) * 2.54;
  Matrix y = new Matrix(2, 1);
  Matrix r = new Matrix(4, 1);
  Drivebase drive;
  double _left_goal = 0, _right_goal = 0;
  double _raw_right = 0, _raw_left = 0, _offset = 0, _integral_offset = 0, _gyro = 0;

  public StateSpaceDriveController(String name, Drivebase drive, StateSpaceGains gains, double period) {
    super(name, 2, 2, 4, gains, period);
    this.drive = drive;
    this.disable();
  }

  public void setGoal(double left, double leftVelocity, double right, double rightVelocity) {
    _left_goal = left;
    _right_goal = right;
    r.set(0, 0, left);
    r.set(1, 0, leftVelocity);
    r.set(2, 0, right);
    r.set(3, 0, rightVelocity);
  }

  void setRawPosition(double left, double right) {
    _raw_right = right;
    _raw_left = left;
    y.set(0,0,left);
    y.set(1,0,right);
  }

  ThrottledPrinter p = new ThrottledPrinter(.1);
  void setPosition(double left, double right, double gyro) {
    // Decay the offset quickly because this gyro is great.
    //_offset = (0.25) * (right - left - gyro * width) / 2.0 + 0.75 * _offset;
    double angleError = (_right_goal - _left_goal) / width - (_raw_right - _offset - _raw_left - _offset) / width;
    // TODO(aschuh): Add in the gyro.
    _integral_offset = 0.0;
    _offset = 0.0;
    _gyro = gyro;
    setRawPosition(left, right);

  }
  int  i = 0;
  public void update() {
    if (!enabled) {
      return;
    }
    // Get Y
    double leftEncoder = drive.getLeftEncoderDistanceInMeters();
    double rightEncoder = drive.getRightEncoderDistanceInMeters();
    setPosition(leftEncoder, rightEncoder, drive.getGyroAngleInRadians());

    // update
    update(r,y);

    double leftMotor = U.get(0, 0) / 12.0;
    double rightMotor = U.get(1, 0) / 12.0;

    drive.setLeftRightPower(leftMotor, rightMotor);
  }
}

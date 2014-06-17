package com.team254.frc2014;

import com.team254.frc2014.subsystems.Drivebase;
import com.team254.lib.util.Util;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * CheesyDriveHelper implements the calculations used in CheesyDrive, sending power to the motors.
 */
public class CheesyDriveHelper {

  private Drivebase drive;
  double oldWheel, quickStopAccumulator;
  private double throttleDeadband = 0.02;
  private double wheelDeadband = 0.02;

  public CheesyDriveHelper(Drivebase drive) {
    this.drive = drive;
  }

  public void cheesyDrive(double throttle, double wheel, boolean isQuickTurn, boolean isHighGear) {
    if (DriverStation.getInstance().isAutonomous()) {
      return;
    }

    double wheelNonLinearity;

    wheel = handleDeadband(wheel, wheelDeadband);
    throttle = handleDeadband(throttle, throttleDeadband);

    double negInertia = wheel - oldWheel;
    oldWheel = wheel;

    if (isHighGear) {
      wheelNonLinearity = 0.6;
      // Apply a sin function that's scaled to make it feel better.
      wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
              / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
      wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
              / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
    } else {
      wheelNonLinearity = 0.5;
      // Apply a sin function that's scaled to make it feel better.
      wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
              / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
      wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
              / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
      wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
              / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
    }

    double leftPwm, rightPwm, overPower;
    double sensitivity;

    double angularPower;
    double linearPower;

    // Negative inertia!
    double negInertiaAccumulator = 0.0;
    double negInertiaScalar;
    if (isHighGear) {
      negInertiaScalar = 5.0;
      sensitivity = Constants.sensitivityHigh.getDouble();
    } else {
      if (wheel * negInertia > 0) {
        negInertiaScalar = 2.5;
      } else {
        if (Math.abs(wheel) > 0.65) {
          negInertiaScalar = 5.0;
        } else {
          negInertiaScalar = 3.0;
        }
      }
      sensitivity = Constants.sensitivityLow.getDouble();
    }
    double negInertiaPower = negInertia * negInertiaScalar;
    negInertiaAccumulator += negInertiaPower;

    wheel = wheel + negInertiaAccumulator;
    if (negInertiaAccumulator > 1) {
      negInertiaAccumulator -= 1;
    } else if (negInertiaAccumulator < -1) {
      negInertiaAccumulator += 1;
    } else {
      negInertiaAccumulator = 0;
    }
    linearPower = throttle;

    // Quickturn!
    if (isQuickTurn) {
      if (Math.abs(linearPower) < 0.2) {
        double alpha = 0.1;
        quickStopAccumulator = (1 - alpha) * quickStopAccumulator + alpha
                * Util.limit(wheel, 1.0) * 5;
      }
      overPower = 1.0;
      if (isHighGear) {
        sensitivity = 1.0;
      } else {
        sensitivity = 1.0;
      }
      angularPower = wheel;
    } else {
      overPower = 0.0;
      angularPower = Math.abs(throttle) * wheel * sensitivity - quickStopAccumulator;
      if (quickStopAccumulator > 1) {
        quickStopAccumulator -= 1;
      } else if (quickStopAccumulator < -1) {
        quickStopAccumulator += 1;
      } else {
        quickStopAccumulator = 0.0;
      }
    }

    rightPwm = leftPwm = linearPower;
    leftPwm += angularPower;
    rightPwm -= angularPower;

    if (leftPwm > 1.0) {
      rightPwm -= overPower * (leftPwm - 1.0);
      leftPwm = 1.0;
    } else if (rightPwm > 1.0) {
      leftPwm -= overPower * (rightPwm - 1.0);
      rightPwm = 1.0;
    } else if (leftPwm < -1.0) {
      rightPwm += overPower * (-1.0 - leftPwm);
      leftPwm = -1.0;
    } else if (rightPwm < -1.0) {
      leftPwm += overPower * (-1.0 - rightPwm);
      rightPwm = -1.0;
    }
    drive.setLeftRightPower(leftPwm, rightPwm);

  }

  public double handleDeadband(double val, double deadband) {
    return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
  }
}

package com.team254.lib.control.impl;

import com.team254.lib.control.MotionProfile;

/**
 * Represents trapezoidal velocity control.
 * Constant acceleration until target (max) velocity is reached, sets acceleration to zero
 * for a calculated time, then decelerates at a constant acceleration with a slope equal to
 * the negative slope of the initial acceleration.
 *
 * @author tom@team254.com (Tom Bottiglieri)
 */
public class TrapezoidProfile implements MotionProfile {

  double lastTime = 0;
  double acceleration, velocity, timeFromMaxVelocity,
          timeToMaxVelocity, sign, timeTotal;

  public TrapezoidProfile(double maxV, double timeToMaxV) {
    setMaxVelocity(maxV);
    setTimeToMaxV(timeToMaxV);
  }

  public double updateSetpoint(double curSetpoint, double curSource, double curTime) {
    double setpoint = curSetpoint;
    double period = curTime - lastTime;
    if (curTime < timeToMaxVelocity) {
      // Accelerate up.
      setpoint += ((acceleration * curTime) * period * sign);
    } else if (curTime < timeFromMaxVelocity) {
      // Maintain max velocity.
      setpoint += (velocity * period * sign);
    } else if (curTime < timeTotal) {
      // Accelerate down.
      double decelTime = curTime - timeFromMaxVelocity;
      double v = velocity + (-acceleration * decelTime);
      setpoint += (v * period * sign);
    }
    lastTime = curTime;
    return setpoint;
  }

  public double setGoal(double goal, double curSource, double t) {
    double setpoint = goal - curSource;
    sign = (setpoint < 0) ? -1.0 : 1.0;
    timeToMaxVelocity = velocity / acceleration;
    double deltaPosMaxV = (sign * setpoint) - (timeToMaxVelocity * velocity);
    double timeAtMaxV = deltaPosMaxV / velocity;
    timeFromMaxVelocity = timeToMaxVelocity + timeAtMaxV;
    timeTotal = timeFromMaxVelocity + timeToMaxVelocity;

    lastTime = t;
    // Set setpoint to current value of PIDSource.
    return curSource;
  }

  public final void setMaxVelocity(double v) {
    velocity = v;
  }

  public final void setTimeToMaxV(double timeToMaxV) {
    acceleration = velocity / timeToMaxV;
  }
}

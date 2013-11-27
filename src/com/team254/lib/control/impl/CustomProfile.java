package com.team254.lib.control.impl;

import com.team254.lib.control.MotionProfile;
import com.team254.lib.util.Interpolator;

/**
 * Represents a customizable motion profile.
 *
 * @author tom@team254.com (Tom Bottiglieri)
 */
public class CustomProfile implements MotionProfile {
  double goal;
  double lastTime;
  double sign = 1;
  boolean done = false;
  Interpolator i = new Interpolator();


  public double updateSetpoint(double curSetpoint, double curSource, double curTime) {
    double period = Math.abs(curTime - lastTime);
    double vel = i.get(curSource);

    double setpoint = curSetpoint;
    setpoint += (vel * period * sign);
    if (setpoint >=  goal && sign > 0) {
      done = true;
    }
    else if (setpoint <= goal && sign < 0) {
      done = true;
    }
    lastTime = curTime;
    System.out.println(goal + " " + setpoint + " " + (vel * period * sign) +
            " " + curSource + " " + vel + " " + curSetpoint);
    if (done) {
      return goal;
    }

    return setpoint;
  }

  public double setGoal(double goal, double curSource, double t) {
    this.goal = goal;
    done = false;
    double setpoint = goal - curSource;
    sign = (setpoint < 0) ? -1.0 : 1.0;
    lastTime = t;
    return curSource;
  }

  public void addWaypoint(double pos, double vel) {
    i.add(pos, vel);
  }

  public void resetProfile() {
    i.clear();
  }
}

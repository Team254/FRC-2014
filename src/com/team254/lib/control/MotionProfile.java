package com.team254.lib.control;

/**
 * Interface for various motion profiles.
 *
 * @author tom@team254.com (Tom Bottiglieri)
 */
public interface MotionProfile {
  public double updateSetpoint(double curSetpoint, double curSource, double curTime);
  public double setGoal(double goal, double curSource, double t);
}

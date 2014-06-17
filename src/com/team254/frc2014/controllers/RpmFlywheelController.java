package com.team254.frc2014.controllers;

import com.team254.lib.ControlOutput;
import com.team254.lib.ControlSource;
import com.team254.lib.FlywheelController;
import com.team254.lib.StateSpaceGains;

/**
 * RpmFlywheelController.java
 * 
 * Our Flywheel controller's model is in Radians/s.
 * This is a shim to allow control in RPM (easier to talk about
 * with random team members)
 *
 * @author tombot
 */
public class RpmFlywheelController extends FlywheelController {
  
  public RpmFlywheelController(String name, ControlOutput output,
          ControlSource sensor, StateSpaceGains gains, double period) {
    super(name, output, sensor, gains, period);
    setNarrowOnTargetWindow();
  }
  private boolean negativeGoal = false;
  public void setVelocityGoal(double v) {
    negativeGoal = v < 0;
    super.setVelocityGoal(Math.abs((v * Math.PI * 2.0) / 60.0));
  }
  
  public double getVelocityGoal() {
    return (super.getVelocityGoal() * 60.0) / (Math.PI * 2.0) * (negativeGoal ? -1.0 : 1.0); 
  }
  
  public double getGoal() {
    return getVelocityGoal();
  }
 
  public void setWideOnTargetWindow() {
    setOnTargetWindow(110);
  }
  
  public final void setNarrowOnTargetWindow() {
    setOnTargetWindow(80);
  }
  
  private void setOnTargetWindow(double windowSize) {
    targetError = (windowSize * Math.PI * 2.0) / 60.0;
  }
}

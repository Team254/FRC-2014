package com.team254.lib;

import com.team254.frc2014.ChezyRobot;

/**
 * Controller.java
 *
 * @author tombot
 */
public abstract class Controller extends ChezyRobot implements Loopable {
  protected boolean enabled = false;
  
  public abstract void update();
  public abstract void reset();
  public abstract double getGoal();
  
  public void enable() {
    enabled = true;
  }
  
  public void disable() {
    enabled = false;
  }

  public boolean enabled() {
    return enabled;
  }
}

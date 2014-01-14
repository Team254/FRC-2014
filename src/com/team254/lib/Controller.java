package com.team254.lib;

/**
 * Controller.java
 *
 * @author tombot
 */
public abstract class Controller {
  protected boolean enabled = false;
  
  public abstract void update();
  
  public void enable() {
    enabled = true;
  }
  
  public void disable() {
    enabled = false;
  }
}

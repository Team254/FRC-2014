package com.team254.lib;

import edu.wpi.first.wpilibj.Timer;

/**
 * TimedBoolean.java
 * 
 * Latch a boolean value for some set time.
 *
 * @author Tom Bottiglieri
 */
public class TimedBoolean {

  Timer t = new Timer();
  double timeout;
  boolean on = false;

  public TimedBoolean(double timeout) {
    this.timeout = timeout;
    t.start();
  }

  public boolean get() {
    return on && t.get() < timeout;
  }

  public void trigger() {
    on = true;
    t.reset();
  }
  
  public void off() {
    on = false;
  }
  
  public void trigger(double newTimeout) {
    this.timeout = newTimeout;
    trigger();
  }
}

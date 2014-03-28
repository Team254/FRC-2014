package com.team254.lib;

import edu.wpi.first.wpilibj.Timer;

/**
 * TimedBoolean.java
 *
 * @author tombot
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
}

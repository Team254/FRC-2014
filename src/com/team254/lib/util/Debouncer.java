package com.team254.lib.util;

import edu.wpi.first.wpilibj.Timer;

/**
 * Compensates for "jumps" in analog signals sources.
 *
 * @author tom@team254.com (Tom Bottiglieri)
 */
public class Debouncer {
  Timer t = new Timer();
  double time;
  boolean first = true;

  public Debouncer(double time) {
    this.time = time;
  }

  public boolean update(boolean val) {
    if (first) {
      first = false;
      t.start();
    }
    if(!val) {
      t.reset();
    }
    return t.get() > time;
  }

  public void reset() {
    t.reset();
  }
}

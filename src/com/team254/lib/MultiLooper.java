package com.team254.lib;

import java.util.Vector;

/**
 * MultiLooper.java
 * 
 * Runs several Loopables simultaneously with one Looper.
 * Useful for running a bunch of control loops
 * with only one Thread worth of overhead.
 * 
 * @author Tom Bottiglieri
 */
public class MultiLooper implements Loopable {
  private Looper looper;
  private Vector loopables = new Vector();
  public MultiLooper(double period) {
    looper = new Looper(this, period);
  }

  public void update() {
    int i;
    for (i = 0; i < loopables.size(); ++i) {
      Loopable c = (Loopable) loopables.elementAt(i);
      if (c != null) {
          c.update();
      }
    }
  }

  public void start() {
    looper.start();    
  }
  
  public void stop() {
    looper.stop();
  }

  public void addLoopable(Loopable c) {
    loopables.addElement(c);
  }
}

package com.team254.lib;

import java.util.Vector;

/**
 * ControlUpdater.java
 *
 * @author tombot
 */
public class ControllerUpdater implements Loopable {
  private Looper looper;
  private Vector controllers = new Vector();
  public ControllerUpdater(double period) {
    looper = new Looper(this, period);
  }

  public void update() {
    int i;
    for (i = 0; i < controllers.size(); ++i) {
      Controller c = (Controller) controllers.elementAt(i);
      if (c != null) {
        c.update();
      }
    }
  }

  public void start() {
    looper.start();
    for (int i = 0; i < controllers.size(); ++i) {
      Controller c = (Controller) controllers.elementAt(i);
      if (c != null) {
        c.enable();
      }
    }
    
  }
  
  public void stop() {
    looper.stop();
        for (int i = 0; i < controllers.size(); ++i) {
      Controller c = (Controller) controllers.elementAt(i);
      if (c != null) {
        c.disable();
      }
    }
  }

  public void addController(Controller c) {
    controllers.addElement(c);
  }
}

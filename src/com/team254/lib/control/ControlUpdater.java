package com.team254.lib.control;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Keeps track of, updates, and pokes the subsystems and controllers periodically.
 *
 * @author richard@team254.com (Richard Lin)
 */
public class ControlUpdater {
  private static ControlUpdater instance = null;
  private Vector systems = new Vector();
  private Timer controlUpdater;
  private double period = 1.0 / 100.0;

  private class UpdaterTask extends TimerTask {
    private ControlUpdater updater;

    public UpdaterTask(ControlUpdater updater) {
      if (updater == null) {
        throw new NullPointerException("Given ControlUpdater was null");
      }
      this.updater = updater;
    }

    public void run() {
      updater.update();
    }
  }

  public void update() {
    for(int i = 0; i < systems.size(); i++) {
      ((Updatable)systems.elementAt(i)).update();
    }
  }

  public static ControlUpdater getInstance() {
    if(instance == null) {
      instance = new ControlUpdater();
    }
    return instance;
  }

  public void start() {
    if(controlUpdater == null) {
      controlUpdater = new Timer();
      controlUpdater.schedule(new UpdaterTask(this), 0L, (long) (this.period * 1000));
    }
  }

  public void stop() {
    if(controlUpdater != null) {
      controlUpdater.cancel();
      controlUpdater = null;
    }
  }

  public void add(Updatable system) {
    systems.addElement(system);
  }

  public void setPeriod(double period) {
    this.period = period;
    stop();
    start();
  }
}

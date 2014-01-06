package com.team254.lib;

import java.util.Timer;
import java.util.TimerTask;

public class Looper {

  private double period = 1.0 / 100.0;
  private Loopable loopable;
  private Timer looperUpdater;

  public Looper(Loopable loopable, double period) {
    this.period = period;
    this.loopable = loopable;
  }

  private class UpdaterTask extends TimerTask {

    private Looper looper;

    public UpdaterTask(Looper looper) {
      if (looper == null) {
        throw new NullPointerException("Given Looper was null");
      }
      this.looper = looper;
    }

    public void run() {
      looper.update();
    }
  }

  public void start() {
    if (looperUpdater == null) {
      looperUpdater = new Timer();
      looperUpdater.schedule(new UpdaterTask(this), 0L, (long) (this.period * 1000));
    }
  }

  public void stop() {
    if (looperUpdater != null) {
      looperUpdater.cancel();
      looperUpdater = null;
    }
  }

  private void update() {
    loopable.update();
  }
}

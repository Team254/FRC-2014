package com.team254.frc2014.actions;

import edu.wpi.first.wpilibj.Timer;

/**
 * This class makes the robot wait
 * @author akalb
 */
public class WaitUntilAutonTimeAction extends Action {

  Timer timer;
  double time;
  public WaitUntilAutonTimeAction(Timer autonTimer, double seconds) {
    setTimeout(10);
    timer = autonTimer;
    time = seconds;
  }

  public boolean execute() {
    return isTimedOut() || (timer.get() >= time);
  }

  public void init() {
  }

  public void done() {
  }
}

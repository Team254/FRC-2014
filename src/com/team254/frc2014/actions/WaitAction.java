package com.team254.frc2014.actions;

/**
 * This class makes the robot wait
 * @author akalb
 */
public class WaitAction extends Action {

  public WaitAction(double seconds) {
    setTimeout(seconds);
  }

  public boolean execute() {
    return isTimedOut();
  }

  public void init() {
  }

  public void done() {
    System.out.println("WaitAction " + this.toString() + "DONE");
  }
}

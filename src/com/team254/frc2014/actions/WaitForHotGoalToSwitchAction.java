package com.team254.frc2014.actions;

/**
 * WaitForHotGoalToSwitchAction.java
 *
 * @author tombot
 */
public class WaitForHotGoalToSwitchAction extends Action {
  
  public WaitForHotGoalToSwitchAction(double timeout) {
    setTimeout(timeout);
  }

  public boolean execute() {
    return bannerHotGoalDetector.probablySawGoalChange() || isTimedOut();
  }

  public void init() {
    
  }

  public void done() {
  }

}

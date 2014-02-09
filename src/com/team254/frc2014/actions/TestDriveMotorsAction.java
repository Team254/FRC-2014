package com.team254.frc2014.actions;

import com.team254.frc2014.ChezyRobot;
import com.team254.frc2014.subsystems.Drivebase;
/*
 * Runs the drive motors at full speed for five seconds
 */
public class TestDriveMotorsAction extends Action  {
  
  private Drivebase d = ChezyRobot.drivebase;
  
  public boolean execute() {
    d.setLeftRightPower(1, 1);
    return isTimedOut();
  }

  public void init() {
    setTimeout(5.0);
  }

  public void done() {
    d.setLeftRightPower(0,0);
  }
  
}

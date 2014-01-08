package com.team254.frc2014;

// This is where the magic happens!
import com.team254.frc2014.auto.ThreeBallAuto;
import com.team254.lib.ChezyIterativeRobot;
import com.team254.lib.Server;

public class ChezyCompetition extends ChezyIterativeRobot {

  AutoMode currentAutoMode = new ThreeBallAuto();
  Server s = new Server();
  Thread t;

  public void autonomousInit() {
    currentAutoMode.start();
  }

  public void disabledInit() {
    currentAutoMode.stop();
  }

  public void teleopInit() {
    currentAutoMode.stop();
    // This is just here for testing purposes
    t = new Thread(s);
    t.start();
  }

  public void teleopPeriodic() {
    ChezyRobot.cdh.cheesyDrive(-ChezyRobot.leftStick.getY(), ChezyRobot.rightStick.getX(), ChezyRobot.rightStick.getRawButton(2), true);
    s.pushData("Testing");
  }
}

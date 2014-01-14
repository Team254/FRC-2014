package com.team254.frc2014;

// This is where the magic happens!
import com.team254.frc2014.auto.ThreeBallAuto;
import com.team254.lib.ChezyIterativeRobot;
import com.team254.lib.Server;
import com.team254.lib.util.ThrottledPrinter;

public class ChezyCompetition extends ChezyIterativeRobot {

  AutoMode currentAutoMode = new ThreeBallAuto();
  Server s = new Server();
  Thread t;
  ThrottledPrinter p = new ThrottledPrinter(1);

  public void robotInit() {
    t = new Thread(s);
    t.start();
    ChezyRobot.initRobot();
    ChezyRobot.controlUpdater.start();
  }

  public void autonomousInit() {
    ChezyRobot.drivebase.resetGyro();
    ChezyRobot.driveController.navigator.resetWithReferenceHeading(ChezyRobot.drivebase.getGyroAngle());
    ChezyRobot.driveController.enable();
    System.out.println(currentAutoMode.getClass().toString());
    currentAutoMode.start();
  }

  public void disabledInit() {
    ChezyRobot.driveController.disable();
    currentAutoMode.stop();
  }

  public void teleopInit() {
    currentAutoMode.stop();
    // This is just here for testing purposes
  }

  public void teleopPeriodic() {
    ChezyRobot.cdh.cheesyDrive(-ChezyRobot.leftStick.getY(), ChezyRobot.rightStick.getX(), ChezyRobot.rightStick.getRawButton(2), true);
  }

  public void disabledPeriodic() {
    // Print the Gyro value - hardware might be broken
    //p.println(""+ChezyRobot.drivebase.getGyroAngle());
  }
}

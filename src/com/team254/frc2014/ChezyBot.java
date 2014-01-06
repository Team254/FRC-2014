package com.team254.frc2014;

// This is where the magic happens!
import com.team254.frc2014.subsystems.Drivebase;
import com.team254.lib.ChezyIterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class ChezyBot extends ChezyIterativeRobot {

  // Make a bunch of subsystems
  public Drivebase drivebase = new Drivebase();
  public CheesyDriveHelper cdh = new CheesyDriveHelper(drivebase);
  public Joystick leftStick = new Joystick(Constants.leftJoystickPort.getInt());
  public Joystick rightStick = new Joystick(Constants.rightJoystickPort.getInt());

  public void teleopPeriodic() {
    cdh.cheesyDrive(-leftStick.getY(), rightStick.getX(), rightStick.getRawButton(2), true);
  }
}

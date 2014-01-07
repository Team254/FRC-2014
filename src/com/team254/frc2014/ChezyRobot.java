/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.frc2014;

import com.team254.frc2014.subsystems.Drivebase;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author tombot
 */
public class ChezyRobot {
  
  // Make a bunch of subsystems
  public static Drivebase drivebase = new Drivebase();
  public static CheesyDriveHelper cdh = new CheesyDriveHelper(drivebase);
  public static Joystick leftStick = new Joystick(Constants.leftJoystickPort.getInt());
  public static Joystick rightStick = new Joystick(Constants.rightJoystickPort.getInt());
  
}

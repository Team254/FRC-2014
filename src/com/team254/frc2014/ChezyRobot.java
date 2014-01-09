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
  public static final Drivebase drivebase = new Drivebase();
  public static final CheesyDriveHelper cdh = new CheesyDriveHelper(drivebase);
  public static final Joystick leftStick = new Joystick(Constants.leftJoystickPort.getInt());
  public static final Joystick rightStick = new Joystick(Constants.rightJoystickPort.getInt());
  public static final AutoModeSelector ams = new AutoModeSelector();
  
}

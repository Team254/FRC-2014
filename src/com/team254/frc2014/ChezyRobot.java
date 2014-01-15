package com.team254.frc2014;

import com.team254.frc2014.controllers.DriveController;
import com.team254.frc2014.subsystems.Drivebase;
import com.team254.lib.ControllerUpdater;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author tombot
 */
public class ChezyRobot {

  static {
    poke();
  }

  private static void poke() {
  } // this is to combat Java's lazy loading of static classes
  // Subsystems
  public static final Drivebase drivebase = new Drivebase();
  public static final CheesyDriveHelper cdh = new CheesyDriveHelper(drivebase);
  public static final Joystick leftStick = new Joystick(Constants.leftJoystickPort.getInt());
  public static final Joystick rightStick = new Joystick(Constants.rightJoystickPort.getInt());
  public static final AutoModeSelector ams = new AutoModeSelector();
  // Controllers
  public static final DriveController driveController = new DriveController();
  public static ControllerUpdater controlUpdater = new ControllerUpdater(1.0 / 100.0);

  public static void initRobot() {
    controlUpdater.addController(driveController);
  }
}

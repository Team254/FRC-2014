package com.team254.frc2014;

import com.team254.frc2014.controllers.HoldPositionController;
import com.team254.frc2014.controllers.TrajctoryDriveController;
import com.team254.frc2014.subsystems.Drivebase;
import com.team254.frc2014.subsystems.HotGoalDetector;
import com.team254.frc2014.subsystems.Intake;
import com.team254.frc2014.subsystems.Navigator;
import com.team254.frc2014.subsystems.Shooter;
import com.team254.lib.MultiLooper;
import edu.wpi.first.wpilibj.Joystick;

/**
 * ChezyRobot defines all of the subsystems.
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
  public static final Intake intake = new Intake();
  public static final CheesyDriveHelper cdh = new CheesyDriveHelper(drivebase);
  public static final Shooter shooter = new Shooter();
  
  public static final Joystick leftStick = new Joystick(Constants.leftJoystickPort.getInt());
  public static final Joystick rightStick = new Joystick(Constants.rightJoystickPort.getInt()); 
  public static final OperatorJoystick operatorJoystick = new OperatorJoystick(Constants.gamepadPort.getInt());
  public static final AutoModeSelector ams = new AutoModeSelector();
  
  // Controllers
  public static MultiLooper controlUpdater = new MultiLooper(1.0 / 100.0);
  
  public static TrajctoryDriveController driveController = new TrajctoryDriveController();
  public static HoldPositionController headingController = new HoldPositionController();
  
  public static HotGoalDetector hotGoalDetector = new HotGoalDetector();
  public static Navigator navigator = new Navigator(drivebase);

  public static void initRobot() {
    controlUpdater.addController(drivebase);
    controlUpdater.addController(intake);
    controlUpdater.addController(shooter);
    controlUpdater.addController(navigator);
  }
}

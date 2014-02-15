package com.team254.frc2014;

import com.team254.frc2014.controllers.HoldPositionController;
import com.team254.frc2014.controllers.OpenLoopController;
import com.team254.frc2014.controllers.TrajctoryDriveController;
import com.team254.frc2014.subsystems.Drivebase;
import com.team254.frc2014.subsystems.HotGoalDetector;
import com.team254.frc2014.subsystems.Intake;
import com.team254.frc2014.subsystems.Navigator;
import com.team254.frc2014.controllers.RpmFlywheelController;
import com.team254.frc2014.subsystems.*;
import com.team254.lib.MultiLooper;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

/**
 * ChezyRobot defines all of the subsystems.
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
  public static final Shooter shooter = new Shooter();
  public static final Compressor compressor = new Compressor(Constants.pressureSwitch.getInt(), Constants.compressorRelay.getInt());
  public static final Clapper clapper = new Clapper();
  
  // Set up the intakes
  public static final Talon frontIntakeRoller = new Talon(Constants.frontIntakeRollerPort.getInt());
  public static final Solenoid frontIntakeSolenoid = new Solenoid(Constants.frontIntakeSolenoidPort.getInt());
  public static final AnalogChannel frontIntakeSwitch = new AnalogChannel(Constants.frontIntakeSwitchPort.getInt());
  public static final Encoder frontIntakeEncoder = new Encoder(Constants.frontIntakeEncoderPortA.getInt(), Constants.frontIntakeEncoderPortB.getInt());
  
  public static final Talon rearIntakeRoller = new Talon(Constants.rearIntakeRollerPort.getInt());
  public static final Solenoid rearIntakeSolenoid = new Solenoid(Constants.rearIntakeSolenoidPort.getInt());
  public static final AnalogChannel rearIntakeSwitch = new AnalogChannel(Constants.rearIntakeSwitchPort.getInt());
  public static final Encoder rearIntakeEncoder = new Encoder(Constants.rearIntakeEncoderPortA.getInt(), Constants.rearIntakeEncoderPortB.getInt());
  
  public static final Intake frontIntake = new Intake("Front Intake", frontIntakeRoller, frontIntakeEncoder, frontIntakeSwitch, frontIntakeSolenoid, true);
  public static final Intake rearIntake = new Intake("Rear Intake", rearIntakeRoller, rearIntakeEncoder, rearIntakeSwitch, rearIntakeSolenoid, true);
  
  public static final CheesyDriveHelper cdh = new CheesyDriveHelper(drivebase);
  
  public static final Joystick leftStick = new Joystick(Constants.leftJoystickPort.getInt());
  public static final Joystick rightStick = new Joystick(Constants.rightJoystickPort.getInt());
  public static final OperatorJoystick operatorJoystick = new OperatorJoystick(Constants.gamepadPort.getInt());
  public static final AutoModeSelector ams = new AutoModeSelector();
  
  // Controllers
  public static MultiLooper subsystemUpdater100Hz = new MultiLooper(1.0 / 100.0);
  public static TrajctoryDriveController driveController = new TrajctoryDriveController();
  public static HoldPositionController headingController = new HoldPositionController();
  public static HotGoalDetector hotGoalDetector = new HotGoalDetector();
  public static Navigator navigator = new Navigator(drivebase);
  public static OpenLoopController openLoopShooterController = new OpenLoopController(shooter);
  public static final RpmFlywheelController shooterController = new RpmFlywheelController("ShooterController", shooter, shooter, ShooterGains.getGains()[0], 1.0/100.0);

  public static void initRobot() {
    // Add all subsystems to a 100Hz Looper
    subsystemUpdater100Hz.addLoopable(drivebase);
    subsystemUpdater100Hz.addLoopable(frontIntake);
    subsystemUpdater100Hz.addLoopable(rearIntake);
    subsystemUpdater100Hz.addLoopable(shooter);
    subsystemUpdater100Hz.addLoopable(navigator);
    subsystemUpdater100Hz.addLoopable(clapper);
    compressor.start();
    shooter.useController(shooterController);
  }
}

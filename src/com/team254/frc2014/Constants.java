package com.team254.frc2014;

import com.team254.lib.util.ConstantsBase;

/**
 * Manages constant values used everywhere in the robot code.
 * Variables can be declared here with default values and overridden with a text file uploaded to
 * the robot's file system.
 *
 * @author brandon.gonzalez.451@gmail.com (Brandon Gonzalez)
 */

public class Constants extends ConstantsBase {
  // Declare the constants and their default values here.
  // Control board mappings
  public static final Constant leftJoystickPort = new Constant("leftJoystickPort", 1);
  public static final Constant rightJoystickPort = new Constant("rightJoystickPort", 2);
  public static final Constant gamepadPort = new Constant("gamepadPort", 3);

  // Speed controller mappings
  public static final Constant leftDrivePortA = new Constant("leftDrivePortA", 6);
  public static final Constant leftDrivePortB = new Constant("leftDrivePortB", 7);
  public static final Constant leftDrivePortC = new Constant("leftDrivePortC", 8);
  public static final Constant rightDrivePortA = new Constant("rightDrivePortA", 3);
  public static final Constant rightDrivePortB = new Constant("rightDrivePortB", 4);
  public static final Constant rightDrivePortC = new Constant("rightDrivePortC", 5);

  public static final Constant frontIntakeRollerPort = new Constant("frontIntakeRollerPort", 1);
  public static final Constant rearIntakeRollerPort = new Constant("rearIntakeRollerPort", 10);

  public static final Constant leftShooterWheelPort = new Constant("leftShooterWheelPort", 9);
  public static final Constant rightShooterWheelPort = new Constant("rightShooterWheelPort", 2);

  // Solenoids
  public static final Constant pressureSwitch = new Constant("pressureSwitch",  14);
  public static final Constant compressorRelay = new Constant("compressorRelay", 1);

  public static final Constant shifterPort = new Constant("shifterPort", 1);
  public static final Constant catcherSolenoidPort = new Constant("catcherSolenoidPort", 2);
  public static final Constant rearClapperSolenoidPort = new Constant("rearClapperSolenoidPort", 3); 
  public static final Constant frontClapperSolenoidPort = new Constant("frontClapperSolenoidPort", 4); 
  public static final Constant hoodSolenoidPort = new Constant("hoodSolenoidPort", 7);
  public static final Constant frontIntakeSolenoidPort = new Constant("frontIntakeSolenoidPort", 5);
  public static final Constant rearIntakeSolenoidPort = new Constant("rearIntakeSolenoidPort", 6);


  // Operator control mappings
  public static final Constant autonSelectControlPort = new Constant("autonSelectControlPort",11);

  // Sensor mappings
  public static final Constant gyroPort = new Constant("gyroPort", 1);
  
  // Drive encoders
  public static final Constant leftEncoderPortA = new Constant("leftEncoderPortA", 1);
  public static final Constant leftEncoderPortB = new Constant("leftEncoderPortB", 2);
  public static final Constant rightEncoderPortA = new Constant("rightEncoderPortA", 3);
  public static final Constant rightEncoderPortB = new Constant("rightEncoderPortB", 4);

  // Ultrasonic sensors
  public static final Constant ultrasonicInputPort = new Constant("ultrasonicPortA", 10);
  public static final Constant ultrasonicOutputPort = new Constant("ultrasonicPortB", 11);

  // Shooter encodsers
  public static final Constant shooterReflectorPort = new Constant("shooterReflectorPort", 13);

  // Drive tuning
  public static final Constant sensitivityHigh = new Constant("sensitivityHigh", .85);
  public static final Constant sensitivityLow = new Constant("sensitivityLow", .75);

  public static final Constant driveStraightKPLow = new Constant("driveStraightKPLow", 0.07);
  public static final Constant driveStraightKILow = new Constant("driveStraightKILow", 0);
  public static final Constant driveStraightKDLow = new Constant("driveStraightKDLow", 0.02);

  public static final Constant driveTurnKPLow = new Constant("driveTurnKPLow", 0.03);
  public static final Constant driveTurnKILow = new Constant("driveTurnKILow", 0);
  public static final Constant driveTurnKDLow = new Constant("driveTurnKDLow", 0.045);

  public static final Constant frontIntakeSwitchPort = new Constant("frontIntakeSwitchPort", 2);
  public static final Constant rearIntakeSwitchPort = new Constant("rearIntakeSwitchPort", 3);
  
  public static final Constant intakeUpButtonPort = new Constant("intakeUpPort", 10);
  public static final Constant intakeDownButtonPort = new Constant("intakeDownPort", 9);
  public static final Constant intakeDownSwitchPort = new Constant("intakeDownSwitchPort", 12);
  
  public static final Constant frontIntakeEncoderPortA = new Constant("frontIntakeEncoderPortA", 6);
  public static final Constant frontIntakeEncoderPortB = new Constant("frontIntakeEncoderPortB", 12);
  
  public static final Constant rearIntakeEncoderPortA = new Constant("frontIntakeEncoderPortA", 7);
  public static final Constant rearIntakeEncoderPortB = new Constant("frontIntakeEncoderPortB", 8);
  
  public static final Constant robotWidth = new Constant("robotWidth", 25.5 / 12.0);
  public static final Constant robotDt = new Constant("robotDt", 1.0 / 100.0);
  
  public static final double fieldWidth = 26.0;
  public static final double distanceToWhiteLine = 18.0;

  static {
    // Set any overridden constants from the file on startup.
    readConstantsFromFile();
  }

  // Prevent instantiation of this class, as it should only be used statically.
  private Constants() {
  }
}

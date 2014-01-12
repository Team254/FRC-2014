package com.team254.frc2014;

import com.team254.lib.util.ConstantsBase;

/**
 * Manages constant values used everywhere in the robot code.
 * Variables can be declared here with default values and overridden with a text file uploaded to
 * the robot's file system.
 *
 * @author brandon.gonzalez.451@gmail.com (Brandon Gonzalez)
 */

// Intake down - 10
// Intake up -
public class Constants extends ConstantsBase {
  // Declare the constants and their default values here.
  // Control board mappings
  public static final Constant leftJoystickPort = new Constant("leftJoystickPort", 1);
  public static final Constant rightJoystickPort = new Constant("rightJoystickPort", 2);
  public static final Constant gamepadPort = new Constant("gamepadPort", 3);

  // Speed controller mappings
  public static final Constant intakePivotPort = new Constant("intakePivotPort", 1);
  public static final Constant intakePort = new Constant("intakePort", 2);

  public static final Constant leftDrivePortA = new Constant("leftDrivePortA", 3);
  public static final Constant leftDrivePortB = new Constant("leftDrivePortB", 4);
  public static final Constant leftDrivePortC = new Constant("leftDrivePortC", 5);
  public static final Constant rightDrivePortA = new Constant("rightDrivePortA", 6);
  public static final Constant rightDrivePortBC = new Constant("rightDrivePortBC", 7);

  public static final Constant conveyorPort = new Constant("conveyorPort", 8);
  public static final Constant frontShooterPort = new Constant("frontShooterPort", 9);
  public static final Constant backShooterPort = new Constant("backShooterPort", 10);

  // Solenoids
  public static final Constant pressureSwitch = new Constant("pressureSwitch",  9);
  public static final Constant compressorRelay = new Constant("compressorRelay", 8);

  public static final Constant shifterPort = new Constant("shifterPort", 8);
  public static final Constant ptoPort = new Constant("ptoPort", 7);
  public static final Constant shooterAnglePort = new Constant("shooterAnglePort", 6);
  public static final Constant shooterLoaderPort = new Constant("shooterLoaderPort", 5);
  public static final Constant hangerRetractedPort = new Constant("hangerRetractedPort", 4);
  public static final Constant hangerExtendedPort = new Constant("hangerExtendedPort", 3);
  public static final Constant indexerRightPort = new Constant("indexerRightPort", 2);
  public static final Constant indexerLeftPort = new Constant("indexerLeftPort", 1);

  // Operator control mappings
  public static final Constant autonSelectControlPort = new Constant("autonSelectControlPort",11);
  public static final Constant intakeUpButtonPort = new Constant("intakeUpPort", 10);
  public static final Constant intakeDownButtonPort = new Constant("intakeDownPort", 9);
  public static final Constant intakeUpSwitchPort = new Constant("intakeUpSwitchPort", 2);
  public static final Constant intakeDownSwitchPort = new Constant("intakeDownSwitchPort", 12);
  public static final Constant intakeOutControlPort = new Constant("intakeOutControlPort", 8);
  public static final Constant intakeControlPort = new Constant("intakeControlPort", 7);
  public static final Constant shootControlPort = new Constant("shootControlPort", 6);
  public static final Constant indexControlPort = new Constant("indexControlPort", 5);
  public static final Constant backPyramidControlPort = new Constant("backPyramidControlPort", 4);
  public static final Constant frontPyramidControlPort = new Constant("frontPyramidControlPort", 3);
  public static final Constant rapidFirePort = new Constant("rapidFirePort", 2);
  public static final Constant shooterOnPort = new Constant("shooterOnPort", 3);
  public static final Constant climbButtonPort = new Constant("climbButtonPort", 1);

  // Sensor mappings
  public static final Constant gyroPort = new Constant("gyroPort", 1);
  public static final Constant pitchGyroPort = new Constant("pitchGyroPort", 2);
  public static final Constant pressureTransducerPort = new Constant("pressureTransducerPort", 3);
  public static final Constant indexerDownSensorPort = new Constant("indexerDownSensorPort", 10);
  public static final Constant indexerUpSensorPort = new Constant("indexerUpSensorPort", 11);
  public static final Constant rightDiscSensorPort = new Constant("rightDiscSensorPort", 12);
  public static final Constant leftDiscSensorPort = new Constant("leftDiscSensorPort", 14);

  // Drive encoders
  public static final Constant leftEncoderPortA = new Constant("leftEncoderPortA", 3);
  public static final Constant leftEncoderPortB = new Constant("leftEncoderPortB", 4);
  public static final Constant rightEncoderPortA = new Constant("rightEncoderPortA", 7);
  public static final Constant rightEncoderPortB = new Constant("rightEncoderPortB", 8);

  // Shooter encoders
  public static final Constant shootEncoderPort = new Constant("shootEncoderPort", 6);

  // Intake encoder
  public static final Constant intakeEncoderPortA = new Constant("intakeEncoderPortA", 7);
  public static final Constant intakeEncoderPortB = new Constant("intakeEncoderPortB", 8);
  public static final Constant intakeZeroSensorPort = new Constant("intakeZeroSensorPort", 13);

  // Drive tuning
  public static final Constant sensitivityHigh = new Constant("sensitivityHigh", .85);
  public static final Constant sensitivityLow = new Constant("sensitivityLow", .75);

  public static final Constant driveStraightKPLow = new Constant("driveStraightKPLow", 0.07);
  public static final Constant driveStraightKILow = new Constant("driveStraightKILow", 0);
  public static final Constant driveStraightKDLow = new Constant("driveStraightKDLow", 0.02);

  public static final Constant driveTurnKPLow = new Constant("driveTurnKPLow", 0.03);
  public static final Constant driveTurnKILow = new Constant("driveTurnKILow", 0);
  public static final Constant driveTurnKDLow = new Constant("driveTurnKDLow", 0.045);

  public static final Constant driveStraightKPHigh = new Constant("driveStraightKPHigh", 0.045);
  public static final Constant driveStraightKIHigh = new Constant("driveStraightKIHigh", 0);
  public static final Constant driveStraightKDHigh = new Constant("driveStraightKDHigh", 0.17);

  public static final Constant driveTurnKPHigh = new Constant("driveTurnKPHigh", 0.045);
  public static final Constant driveTurnKIHigh = new Constant("driveTurnKIHigh", 0);
  public static final Constant driveTurnKDHigh = new Constant("driveTurnKDHigh", 0.15);

  public static final Constant hangerUpKP = new Constant("hangerUpKP", 0.005);
  public static final Constant hangerUpKI = new Constant("hangerUpKI", 0.0);
  public static final Constant hangerUpKD = new Constant("hangerUpKD", 0.004);

  public static final Constant hangerDownKP = new Constant("hangerDownKP", 0.0);
  public static final Constant hangerDownKI = new Constant("hangerDownKI", 0.0);
  public static final Constant hangerDownKD = new Constant("hangerDownKD", 0.0);

  public static final Constant intakeKP = new Constant("intakeKP", 0.09);
  public static final Constant intakeKI = new Constant("intakeKI", 0.0);
  public static final Constant intakeKD = new Constant("intakeKD", 0.24);

  public static final Constant minShootRpm = new Constant("minShootRpm", 11750);
  public static final Constant shootRpm = new Constant("shootRpm", 10250);

  public static final Constant testBumpSensor = new Constant("testBumpSensor", 0);

  static {
    // Set any overridden constants from the file on startup.
    readConstantsFromFile();
  }

  // Prevent instantiation of this class, as it should only be used statically.
  private Constants() {
  }
}

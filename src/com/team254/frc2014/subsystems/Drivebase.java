package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.lib.ChezyGyro;
import com.team254.lib.Controller;
import com.team254.lib.Loopable;
import com.team254.lib.Subsystem;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Hashtable;

/**
 * This class defines the drivebase.
 * @author spinkerton
 */
public class Drivebase extends Subsystem implements Loopable {

  // ticks to feet
  public final double RIGHT_ENCOCDER_TO_DISTANCE_RATIO = (3.5 * Math.PI) / (12.0 * 256.0);
  public final double LEFT_ENCOCDER_TO_DISTANCE_RATIO = (3.5 * Math.PI) / (12.0 * 256.0);
  // Speed controllers
  private Talon leftDriveA = new Talon(Constants.leftDrivePortA.getInt());
  private Talon leftDriveB = new Talon(Constants.leftDrivePortB.getInt());
  private Talon leftDriveC = new Talon(Constants.leftDrivePortC.getInt());
  private Talon rightDriveA = new Talon(Constants.rightDrivePortA.getInt());
  private Talon rightDriveB = new Talon(Constants.rightDrivePortB.getInt());
  private Talon rightDriveC = new Talon(Constants.rightDrivePortC.getInt());
  //Encoders
  private Encoder leftEncoder = new Encoder(Constants.leftEncoderPortA.getInt(),
          Constants.leftEncoderPortB.getInt(), false);
  private Encoder rightEncoder = new Encoder(Constants.rightEncoderPortA.getInt(),
          Constants.rightEncoderPortB.getInt(), true);
  
  //Solenoids
  private Solenoid shifter = new Solenoid(Constants.shifterPort.getInt());
  //Ultrasonic Sensor
  // private Ultrasonic ultrasonic = new Ultrasonic(Constants.ultrasonicInputPort.getInt(),
  //       Constants.ultrasonicOutputPort.getInt());
  //Gyro
  public ChezyGyro gyro;
  private Controller controller;

  public void setLeftRightPower(double leftPower, double rightPower) {
    leftDriveA.set(leftPower);
    leftDriveB.set(leftPower);
    leftDriveC.set(leftPower);
    rightDriveA.set(-rightPower);
    rightDriveB.set(-rightPower);
    rightDriveC.set(-rightPower);
  }
  public void setLowgear(boolean low) {
    shifter.set(low);
  }
  public Drivebase() {
    super("Drivebase");
    //System.out.println("Making gyro!");
    gyro = new ChezyGyro(Constants.gyroPort.getInt());
    //System.out.println("Done making gyro!");
    //ultrasonic.setEnabled(true);
    // ultrasonic.setAutomaticMode(true);
    leftEncoder.start();
    rightEncoder.start();
  }

  public Hashtable serialize() {
    Hashtable leftDrive = new Hashtable();
    Hashtable rightDrive = new Hashtable();
    Hashtable encoders = new Hashtable();

    leftDrive.put("leftDriveA", new Double(leftDriveA.get()));
    leftDrive.put("leftDriveB", new Double(leftDriveB.get()));
    leftDrive.put("leftDriveC", new Double(leftDriveC.get()));

    rightDrive.put("rightDriveA", new Double(rightDriveA.get()));
    rightDrive.put("rightDriveB", new Double(rightDriveB.get()));
    rightDrive.put("rightDriveC", new Double(rightDriveC.get()));

    encoders.put("leftEncoder", new Double(leftEncoder.get()));
    encoders.put("rightEncoder", new Double(rightEncoder.get()));    
    data.put("leftDrive", leftDrive);
    data.put("rightDrive", rightDrive);
    data.put("encoders", encoders);
    data.put("gyro", new Double(getGyroAngle()));
    return data;
  }

  public Encoder getLeftEncoder() {
    return leftEncoder;
  }

  public double getLeftEncoderDistance() { // in feet
    return leftEncoder.get() * LEFT_ENCOCDER_TO_DISTANCE_RATIO;
  }

  public double getLeftEncoderDistanceInMeters() {
    return getLeftEncoderDistance() * 0.3048;
  }

  public Encoder getRightEncoder() {
    return rightEncoder;
  }

  public double getRightEncoderDistance() {
    return rightEncoder.get() * RIGHT_ENCOCDER_TO_DISTANCE_RATIO;
  }

  public double getRightEncoderDistanceInMeters() {
    return getRightEncoderDistance() * 0.3048;
  }

  public double getGyroAngle() {
    return -gyro.getAngle();
  }

  public double getGyroAngleInRadians() {
    return (getGyroAngle() * Math.PI) / 180.0;
  }

  public void resetGyro() {
    gyro.reset();
  }
  

  public double getUltrasonicDistance() {
    //ultrasonic.ping();
    return 3; //ultrasonic.getRangeInches() / 12.0;
  }

  public double getAverageDistance() {
    return (getRightEncoderDistance() + getLeftEncoderDistance()) / 2.0;
  }

  public void driveSpeedTurn(double speed, double turn) {
    double left = speed + turn;
    double right = speed - turn;
    setLeftRightPower(left, right);
  }
  
  public void resetEncoders() {
    leftEncoder.reset();
    rightEncoder.reset();
  }
  
  public void update() {
    SmartDashboard.putNumber("driveLeftEncoder", getLeftEncoderDistance());
    SmartDashboard.putNumber("driveRightEncoder", getRightEncoderDistance());
    SmartDashboard.putNumber("gyro", getGyroAngle());
    super.update();
  }

}

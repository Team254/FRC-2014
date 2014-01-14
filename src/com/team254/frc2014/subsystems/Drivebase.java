package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.lib.Subsystem;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Talon;
import java.util.Hashtable;

public class Drivebase extends Subsystem {
  
  // ticks to feet
  public final double LEFT_ENCOCDER_TO_DISTANCE_RATIO = (3.5 * Math.PI) / (12.0 * 256.0);
  public final double RIGHT_ENCOCDER_TO_DISTANCE_RATIO = (3.5 * Math.PI) / (12.0 * 300.0);
  
  // Speed controllers
  private Talon leftDriveA = new Talon(Constants.leftDrivePortA.getInt());
  private Talon leftDriveB = new Talon(Constants.leftDrivePortB.getInt());
  private Talon leftDriveC = new Talon(Constants.leftDrivePortC.getInt());
  private Talon rightDriveA = new Talon(Constants.rightDrivePortA.getInt());
  private Talon rightDriveBC = new Talon(Constants.rightDrivePortBC.getInt());

  //Encoders
  private Encoder rightEncoder = new Encoder(Constants.leftEncoderPortA.getInt(),
          Constants.leftEncoderPortB.getInt(), false);
  private Encoder leftEncoder = new Encoder(Constants.rightEncoderPortA.getInt(),
          Constants.rightEncoderPortB.getInt(), true);

  //Gyro
  private Gyro gyro;
  
  public void setLeftRightPower(double leftPower, double rightPower) {
    leftDriveA.set(leftPower);
    leftDriveB.set(leftPower);
    leftDriveC.set(leftPower);
    rightDriveA.set(-rightPower);
    rightDriveBC.set(-rightPower);
  }
  
  public Drivebase() {
    super("Drivebase");
    System.out.println("Making gyro!");
    gyro = new Gyro(Constants.gyroPort.getInt());
    System.out.println("Done making gyro!");
    leftEncoder.start();
    rightEncoder.start();
  }

  public Hashtable serialize() {
    data.put("leftDriveA", new Double(leftDriveA.get()));
    data.put("leftDriveB", new Double(leftDriveB.get()));
    data.put("leftDriveC", new Double(leftDriveC.get()));
    data.put("rightDriveA", new Double(rightDriveA.get()));
    data.put("rightDriveBC", new Double(rightDriveBC.get()));
    data.put("leftEncoder", new Double(leftEncoder.get()));
    data.put("rightEncoder", new Double(rightEncoder.get()));
    //data.put("gyro", new Double(getGyroAngle()));
    return data;
  }

  public Encoder getLeftEncoder() {
    return leftEncoder;
  }
  
  public double getLeftEncoderDistance() {
    return leftEncoder.get() * LEFT_ENCOCDER_TO_DISTANCE_RATIO;
  }

  public Encoder getRightEncoder() {
    return rightEncoder;
  }
  
  public double getRightEncoderDistance() {
    return rightEncoder.get() * RIGHT_ENCOCDER_TO_DISTANCE_RATIO;
  }

  public double getGyroAngle() {
    return gyro.getAngle();
  }
  
  public void resetGyro() {
    gyro.reset();
  }

}

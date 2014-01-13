package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.lib.Subsystem;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import java.util.Hashtable;

public class Drivebase extends Subsystem {
  
  public final double ENCOCDER_TO_DISTANCE_RATIO = 1.0;
  
  // Speed controllers
  private Talon leftDriveA = new Talon(Constants.leftDrivePortA.getInt());
  private Talon leftDriveB = new Talon(Constants.leftDrivePortB.getInt());
  private Talon leftDriveC = new Talon(Constants.leftDrivePortC.getInt());
  private Talon rightDriveA = new Talon(Constants.rightDrivePortA.getInt());
  private Talon rightDriveBC = new Talon(Constants.rightDrivePortBC.getInt());

  //Encoders
  private Encoder leftEncoder = new Encoder(Constants.leftEncoderPortA.getInt(),
          Constants.leftEncoderPortB.getInt(), true);
  private Encoder rightEncoder = new Encoder(Constants.rightEncoderPortA.getInt(),
          Constants.rightEncoderPortB.getInt());

  //Gyro
  private Gyro gyro = new Gyro(Constants.gyroPort.getInt());
  
  public void setLeftRightPower(double leftPower, double rightPower) {
    leftDriveA.set(leftPower);
    leftDriveB.set(leftPower);
    leftDriveC.set(leftPower);
    rightDriveA.set(-rightPower);
    rightDriveBC.set(-rightPower);
  }
  
  public Drivebase() {
    super("Drivebase");
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
    return leftEncoder.get() * ENCOCDER_TO_DISTANCE_RATIO;
  }

  public Encoder getRightEncoder() {
    return rightEncoder;
  }
  
  public double getRightEncoderDistance() {
    return rightEncoder.get() * ENCOCDER_TO_DISTANCE_RATIO;
  }

  public double getGyroAngle() {
    return gyro.getAngle();
  }

}

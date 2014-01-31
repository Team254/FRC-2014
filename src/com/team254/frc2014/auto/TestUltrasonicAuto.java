package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;
import com.team254.frc2014.FieldPosition;

/**
 *
 * @author spinkerton
 */
public class TestUltrasonicAuto extends AutoMode {

  public TestUltrasonicAuto() {
    super("Test ultasonic");
  }
  
  protected void routine() {
    driveToUltrasonicRange(4);
    System.out.println("done ultrasonic");
  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLine;
  }

}

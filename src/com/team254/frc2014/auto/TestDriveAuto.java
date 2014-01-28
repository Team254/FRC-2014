package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;
import com.team254.frc2014.FieldPosition;

/**
 * TestDriveAuto.java
 *
 * @author tombot
 */
public class TestDriveAuto extends AutoMode {

  public TestDriveAuto() {
    super("Test driving");
  }
  
  protected void routine() {
    waitTime(.5);
    boolean goRight = hotGoalDetector.hotGoalIsOnLeft();
    double xDir = goRight ? 1.0 : -1.0;
    drive(5,10);
    driveSCurve(xDir * 6, 12, 0, 10);
    headingController.setDistance(drivebase.getAverageDistance());
    headingController.setHeading(0);
    drivebase.useController(headingController);  
  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLineOffsetByBall;
  }
  
}

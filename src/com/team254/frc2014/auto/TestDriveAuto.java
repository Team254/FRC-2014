package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;

/**
 * TestDriveAuto.java
 *
 * @author tombot
 */
public class TestDriveAuto extends AutoMode {

  protected void routine() {
    waitTime(.5);
    boolean goRight = hotGoalDetector.hotGoalIsOnLeft();
    drive(5,10);
    driveArc(7, 45 * (goRight ? 1.0 : -1.0), 10);
    driveArc(7, 0, 10);
    headingController.setDistance(drivebase.getAverageDistance());
    headingController.setHeading(0);
    drivebase.useController(headingController);
  }
  
}

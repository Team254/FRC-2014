package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;

/**
 * TestSSDriveAuto.java
 *
 * @author tombot
 */
public class TestSSDriveAuto extends AutoMode {

  protected void routine() {
    driveController.disable();
    driveSS(0,0);
  }

}

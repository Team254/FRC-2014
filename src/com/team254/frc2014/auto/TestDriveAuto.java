package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;

/**
 * TestDriveAuto.java
 *
 * @author tombot
 */
public class TestDriveAuto extends AutoMode {

  protected void routine() {
    driveArc(8, 45, 10);
    driveArc(8, 0, 10);
  }
  
}

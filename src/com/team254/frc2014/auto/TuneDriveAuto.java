package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;
import com.team254.frc2014.FieldPosition;

/**
 * TuneDriveAuto.java
 *
 * @author tombot
 */
public class TuneDriveAuto extends AutoMode {
  
  public TuneDriveAuto() {
    super("Tune drive");
  }

  protected void routine() {
    double heading = 0;
    // Turn on heading controller
    for (int i = 0; i < 20; ++i){
      heading = (i%2==0) ? 0 : 60;
      //heading = heading * (i * 1.0);
      headingController.setHeading(heading);
      drivebase.useController(headingController);
      waitTime(5);
    }
  }

  public FieldPosition getFieldPosition() {
    return null;
  }

}

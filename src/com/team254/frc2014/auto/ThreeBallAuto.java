package com.team254.frc2014.auto;

import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.LanedAutoMode;
import edu.wpi.first.wpilibj.Timer;

/**
 * ThreeBallAuto.java
 * This autonomous picks up three balls and scores them all in le hot goal.
 * @author tombot
 */
public class ThreeBallAuto extends LanedAutoMode {
  
  public ThreeBallAuto() {
    super("Three ball");
  }
  
  private double getXOffset() {
    switch(lane) {
      case INSIDE_LANE:
        return 3; 
      case OUTSIDE_LANE:
        return 8;
      case WALL_LANE:
        return 10.5;
      case MIDDLE_LANE:
      default:
        return 6;
    }
  }
  
  protected void routine() {
    waitTime(.5);
    Timer t = new Timer();
    t.start();
    boolean goRight = hotGoalDetector.hotGoalIsOnLeft();
    double xDir = 1.0; // goRight ? 1.0 : -1.0;
    drive(4,10);
    System.out.println("t elapsed " + t.get());
    double yDistance = 12;
    if (lane == WALL_LANE) {
      yDistance -= 2;
    }
    driveSCurve(xDir * getXOffset(), yDistance, 0, 10);
    drivebase.resetEncoders();
    headingController.setDistance(0);
    headingController.setHeading(0);
    drivebase.useController(headingController);  
  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLineOffsetByBall;
  }
  
}

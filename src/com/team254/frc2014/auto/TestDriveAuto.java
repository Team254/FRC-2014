package com.team254.frc2014.auto;

import com.team254.frc2014.path.AutoPath;
import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.LanedAutoMode;
import com.team254.frc2014.path.DriveToWallPath;
import com.team254.lib.trajectory.Path;
import edu.wpi.first.wpilibj.Timer;

/**
 * TestDriveAuto.java
 *
 * @author tombot
 */
public class TestDriveAuto extends LanedAutoMode {
  
  public TestDriveAuto() {
    super("Test driving");
  }
  
  private double getXOffset() {
    switch(lane) {
      case INSIDE_LANE:
        return 4; 
      case OUTSIDE_LANE:
        return 8;
      case WALL_LANE:
        return 10.5;
      case MIDDLE_LANE:
      default:
        return 6;
    }
  }
  public static int goLeftCounter = 0;
  
  protected void routine() {
    goLeftCounter++;
    Timer t = new Timer();
    t.start();
    boolean goLeft = goLeftCounter % 2 == 0;
    System.out.println("Going left? " + goLeft);
    Path path = goLeft ? DriveToWallPath.getInstance().getLeftGoalPath() : DriveToWallPath.getInstance().getRightGoalPath();
    drivePath(path, 10);
    drivebase.resetEncoders();
    headingController.setDistance(0);
    headingController.setHeading(0);
    drivebase.useController(headingController);
    System.out.println("Drive time: " + t.get());
  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLineOffsetByBall;
  }
  
}

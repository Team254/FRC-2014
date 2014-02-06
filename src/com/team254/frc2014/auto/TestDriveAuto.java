package com.team254.frc2014.auto;

import com.team254.frc2014.path.AutoPath;
import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.LanedAutoMode;
import com.team254.frc2014.path.CenterLanePath;
import com.team254.frc2014.path.DriveToWallPath;
import com.team254.frc2014.path.InsideLanePath;
import com.team254.frc2014.path.OutsideLanePath;
import com.team254.lib.Route;
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
  
  private AutoPath getAutoPath() {
    switch(lane) {
      case INSIDE_LANE:
        return InsideLanePath.getInstance(); 
      case OUTSIDE_LANE:
        return OutsideLanePath.getInstance();
      case WALL_LANE:
        return DriveToWallPath.getInstance();
      case MIDDLE_LANE:
      default:
        return CenterLanePath.getInstance();
    }
  }
  public static int goLeftCounter = 0;
  
  protected void routine() {
    goLeftCounter++;
    Timer t = new Timer();
    t.start();
    boolean goLeft = goLeftCounter % 2 == 0;
    System.out.println("Going left? " + goLeft);
    AutoPath autoPath = getAutoPath();
    Route route = goLeft ? autoPath.getLeftGoalRoute() : autoPath.getRightGoalRoute();
    driveRoute(route, 10);
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

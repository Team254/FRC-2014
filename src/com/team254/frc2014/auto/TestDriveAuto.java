package com.team254.frc2014.auto;

import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.LanedAutoMode;
import com.team254.frc2014.paths.TestPath;
import com.team254.lib.Route;
import com.team254.lib.trajectory.Trajectory;
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
  

  public static int goLeftCounter = 0;
  
  protected void routine() {
    goLeftCounter++;
    Timer t = new Timer();

    boolean goLeft = goLeftCounter % 2 == 0;
    System.out.println("Going left? " + goLeft);
    System.out.println("here");
    Route route = new Route();
    route.leftTrajectory = new Trajectory(TestPath.Left);
    route.rightTrajectory = new Trajectory(TestPath.Right);
    t.start();
    driveRoute(route, 10);
    drivebase.resetEncoders();
    System.out.println("Drive time: " + t.get());
    headingController.setDistance(0);
    headingController.setHeading(Math.toDegrees(Math.PI/6.0));
    drivebase.useController(headingController);

  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLineOffsetByBall;
  }
  
}

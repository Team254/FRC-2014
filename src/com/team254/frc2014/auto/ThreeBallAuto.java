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
public class ThreeBallAuto extends LanedAutoMode {
  
  public ThreeBallAuto() {
    super("Three Ball");
  }
  

  public static int goLeftCounter = 0;
  
  protected void routine() {
        clapper.wantFront = false;
    clapper.wantRear = false;
    
    goLeftCounter++;
    Timer t = new Timer();
    boolean goLeft = goLeftCounter % 2 == 0;
    System.out.println("Going left? " + goLeft);
    System.out.println("here");
    Route route = new Route();
    route.leftTrajectory = new Trajectory(TestPath.Left);
    route.rightTrajectory = new Trajectory(TestPath.Right);
    
    shooterController.setVelocityGoal(4200);
    
    t.start();
    driveRoute(route, 10);
    drivebase.resetEncoders();
    System.out.println("Drive time: " + t.get());
    headingController.setDistance(0);
    headingController.setHeading(Math.toDegrees(Math.PI/6.0));
    drivebase.useController(headingController);
    
    waitTime(.5);
    t.start();
    clapper.wantFront = true;
    clapper.wantRear = true;
    waitTime(.5);
    shooterController.setVelocityGoal(0);

  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLineOffsetByBall;
  }
  
}

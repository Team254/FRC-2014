package com.team254.frc2014.auto;

import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.ConfigurationAutoMode;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.trajectory.Path;
import edu.wpi.first.wpilibj.Timer;

/**
 * TestDriveAuto.java
 *
 * @author tombot
 */
public class TestDriveAuto extends ConfigurationAutoMode {
  
  public TestDriveAuto() {
    super("Test driving");
  }
  
  Path path = AutoPaths.get("CenterLanePath");
  
  public static int goLeftCounter = 0;
  
  protected void routine() {
    goLeftCounter++;
    Timer t = new Timer();

    boolean goLeft = goLeftCounter % 2 == 0;
    System.out.println("Going left? " + goLeft);
    System.out.println("here");
    //new CenterLanePath();
    t.start();
    drivePath(path, 10);
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

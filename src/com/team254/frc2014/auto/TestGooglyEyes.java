package com.team254.frc2014.auto;

import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.ConfigurationAutoMode;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.trajectory.Path;
import edu.wpi.first.wpilibj.Timer;


/**
 *
 * @author Mani Gnanasivam
 * @author EJ Sebathia
 */
public class TestGooglyEyes extends ConfigurationAutoMode {

  public TestGooglyEyes() {
    super("TestGooglyEyes");
  }
  static Path path = AutoPaths.get("CenterLanePath");
  protected void routine() {
    hotGoalDetector.startSampling();
    
    Timer t = new Timer();
    t.start();

    // Turn on wheel
    //shooterController.setVelocityGoal(4000);
    
    // Grab balls from ground
    clapper.wantFront = false;
    clapper.wantRear = false;
    waitTime(1.0);
    
    boolean onLeft = hotGoalDetector.hotGoalStartedOnLeft();
    boolean goLeft = !onLeft;
    System.out.println("Hot goal started on left: "  + onLeft);
    
    
    
    waitTime(5);
    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.wantBumperGather = false;
    rearIntake.wantBumperGather = false;
  }

  public FieldPosition getFieldPosition() {
    return null;
  }
    
 
}

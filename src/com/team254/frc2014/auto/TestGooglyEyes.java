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
  protected void routine() {
    hotGoalDetector.startSampling();
    
    Timer t = new Timer();
    t.start();

    // Turn on wheel
    //shooterController.setVelocityGoal(4000);
    
    // Grab balls from ground
    clapper.wantFront = false;
    clapper.wantRear = false;
    waitForHotGoalToSwitch(2.0);
    
    boolean goLeft = hotGoalDetector.goLeft();
    System.out.println("go left: "  + goLeft);
    
    
    shooterController.setVelocityGoal(4000);
    waitTime(3.0);
    shooterController.setVelocityGoal(0);
    

    
    
    
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

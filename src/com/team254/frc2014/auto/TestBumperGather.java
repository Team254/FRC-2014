package com.team254.frc2014.auto;

import com.team254.frc2014.ConfigurationAutoMode;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.trajectory.Path;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Mani Gnanasivam
 * @author EJ Sebathia
 */
public class TestBumperGather extends ConfigurationAutoMode {

  public TestBumperGather() {
    super("Test Bumper Gather");
  }

  protected void routine() {
    bannerHotGoalDetector.startSampling();
    boolean goLeft;
    Timer t = new Timer();
    t.start();
    settler.set(true);

    // Grab balls from ground
    pinniped.wantFront = false;
    pinniped.wantRear = false;
    frontIntake.wantBumperGather = true;
    rearIntake.wantBumperGather = true;
    waitTime(6);
    settler.set(false);
    goLeft = !bannerHotGoalDetector.goLeft();
    System.out.println("Going Left? " + goLeft);
  }

}

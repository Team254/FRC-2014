package com.team254.frc2014.auto;

import com.team254.frc2014.ConfigurationAutoMode;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Tom Botiglieri
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

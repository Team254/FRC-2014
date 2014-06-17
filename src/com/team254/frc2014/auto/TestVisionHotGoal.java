package com.team254.frc2014.auto;

import com.team254.frc2014.ConfigurationAutoMode;

/**
 *
 * @author tombot
 */
public class TestVisionHotGoal extends ConfigurationAutoMode {

  public TestVisionHotGoal() {
    super("TestVisionHotGoal");
  }

  protected void routine() {
    visionHotGoalDetector.reset();
    visionHotGoalDetector.startSampling();
    waitTime(1.5);
    visionHotGoalDetector.stopSampling();
    System.out.println("Vision go left? " + visionHotGoalDetector.goLeft());
  }

}

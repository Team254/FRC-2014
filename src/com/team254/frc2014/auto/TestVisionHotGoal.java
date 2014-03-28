package com.team254.frc2014.auto;

import com.team254.frc2014.ChezyRobot;
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

  public FieldPosition getFieldPosition() {
    return null;
  }
}

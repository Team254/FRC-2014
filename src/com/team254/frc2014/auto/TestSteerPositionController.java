package com.team254.frc2014.auto;

import com.team254.frc2014.ConfigurationAutoMode;

/**
 *
 * @author Mani Gnanasivam
 * @author EJ Sebathia
 */
public class TestSteerPositionController extends ConfigurationAutoMode {

  public TestSteerPositionController() {
    super("Test Deke Steer");
  }

  protected void routine() {
    visionHotGoalDetector.reset();
    visionHotGoalDetector.startSampling();
    steerHeadingController.setHeading(0);
    drivebase.useController(steerHeadingController);
  }

}

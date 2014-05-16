package com.team254.frc2014.auto;

import com.team254.frc2014.AutoModeSelector.Configuration;
import com.team254.frc2014.ConfigurationAutoMode;
import com.team254.frc2014.controllers.HoldPositionController;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.trajectory.Path;


/**
 *
 * @author Tom Botiglieri
 * @author Brandon Gonzales
 * @author Jared Russel
 * @author Art Kalb
 */
public class AerialAssistAuto extends ConfigurationAutoMode {

  protected boolean endsClose(Configuration config) {
    if (config.pathToTake >= 0 && config.pathToTake < AutoPaths.kPathNames.length) {
      return "InsideLanePathClose".equals(AutoPaths.kPathNames[config.pathToTake]); 
    }
    return false;
  }
  
  public AerialAssistAuto() {
    super("Default auto mode");
  }
  
  protected void routine() {
    drivebase.resetEncoders();
    headingController.setHeading(0);
    drivebase.useController(headingController);
    
    boolean endingClose = endsClose(config);
    
    // Start voting 
    visionHotGoalDetector.reset();
    visionHotGoalDetector.startSampling();
   
    if (endingClose) {
      wantedStartRpm = config.numBalls == 0 ? 0 : config.numBalls > 1 ? closeIntakeDownPreset : closeIntakeUpPreset;
    } else {
      wantedStartRpm = config.numBalls == 0 ? 0 : config.numBalls > 1 ? farIntakeDownPreset : farIntakeUpPreset;
    }

    // Settler down
    settler.set(true);
    
    // Grab balls from ground
    pinniped.wantFront = false;
    pinniped.wantRear = false;
    frontIntake.wantBumperGather = config.numBalls == 3 || (config.numBalls == 2 && !config.preferRearBall);
    rearIntake.wantBumperGather = config.numBalls == 3 || (config.numBalls == 2 && config.preferRearBall);
    
    // Turn on wheel
    shooterController.enable();
    System.out.println("Using " + wantedStartRpm + " RPM for first shot");
    shooterController.setVelocityGoal(wantedStartRpm);
    
    // Wait for interrupt from hot goal sensor
    double howLongToWait = 1.5;
    if (config.pathToTake == AutoPaths.WALL_LANE_ID) {
      howLongToWait = 1.2;
    }
    waitUntilTime(howLongToWait);
    
    Path path = AutoPaths.getByIndex(config.pathToTake);
    if (path == null) {
      path = AutoPaths.get("InsideLanePathFar");
    }

    drivePathWithFlip(path, visionHotGoalDetector, 10);
    visionHotGoalDetector.stopSampling();

    System.out.println("Finished driving at: " + autoTimer.get());
   
    
    // Maybe activate steering controller for dekes
    boolean doSteer = config.doSteer;
    HoldPositionController holdPositionController;
    if (doSteer) {
      holdPositionController=  steerHeadingController;
      // Reset steering stuf
      steerHeadingController.reset();
      visionHotGoalDetector.reset();
      visionHotGoalDetector.startSampling();
    } else {
      holdPositionController = headingController;
    }
    
    // Turn on heading controller
    holdPositionController.setDistance(0);
    double endHeading = Math.toDegrees(path.getEndHeading());
    drivebase.resetEncoders();
    holdPositionController.setHeading(endHeading);
    drivebase.useController(holdPositionController);
    
    // Wait until hot goal is about to switch
    waitUntilTime(4.5);
    
    
    // Last shot rpm
    wantedEndRpm = endingClose ? closeIntakeUpPreset : farIntakeUpPreset;
    System.out.println("Using " + wantedEndRpm + " RPM for end shots");
 
    if (config.numBalls == 3) {
      shootThree();
    } else if (config.numBalls == 2) {
      if (config.preferRearBall) {
        shootTwoWithRearBall();
      } else {
        shootTwoWithFrontBall();
      }
    } else if (config.numBalls == 1)  {
      shootOne();
    }
 
    // Print out time
    System.out.println("Auto done at: " + autoTimer.get());
    
    // Clean up
    rearIntake.setManualRollerPower(0);
    shooterController.setVelocityGoal(0);
  }

}

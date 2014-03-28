package com.team254.frc2014.auto;

import com.team254.frc2014.ConfigurationAutoMode;
import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.trajectory.Path;


/**
 *
 * @author Mani Gnanasivam
 * @author EJ Sebathia
 */
public class AerialAssistAuto extends ConfigurationAutoMode {

  static Path centerPathFar = AutoPaths.get("CenterLanePathFar");
  static Path centerPathClose = AutoPaths.get("CenterLanePathClose");
  static Path insidePathFar = AutoPaths.get("InsideLanePathFar");
  static Path wallPath = AutoPaths.get("WallLanePath");
  static Path straightPath = AutoPaths.get("StraightAheadPath");
  
  protected boolean endingInOpenField() {
    return config.pathToTake == ConfigurationAutoMode.MIDDLE_LANE;
  }
  
  public AerialAssistAuto() {
    super("Default auto mode");
  }
  
  protected void routine() {
    // Start voting 
    visionHotGoalDetector.reset();
    visionHotGoalDetector.startSampling();
   
    if (endingInOpenField() && config.endClose) {
      wantedStartRpm = config.numBalls == 0 ? 0 : config.numBalls > 1 ? closeIntakeDownPreset : closeIntakeUpPreset;
    } else {
      wantedStartRpm = config.numBalls == 0 ? 0 : config.numBalls > 1 ? farIntakeDownPreset : farIntakeUpPreset;
    }

    // Settler down
    settler.set(true);
    

    // Grab balls from ground
    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.wantBumperGather = config.numBalls == 3 || (config.numBalls == 2 && !config.preferRearBall);
    rearIntake.wantBumperGather = config.numBalls == 3 || (config.numBalls == 2 && config.preferRearBall);
    
    // Wait for interrupt from hot goal sensor
    waitForHotGoalToSwitch(1.6);
    
    // Turn on wheel
    shooterController.enable();
    shooterController.setVelocityGoal(wantedStartRpm);
    

    // Grab "time of match start" timeshift
    double timeOfSwitch = autoTimer.get();
    
    // Stop voting
    visionHotGoalDetector.stopSampling();
    boolean goLeft = visionHotGoalDetector.goLeft();
    System.out.println("Hot goal started on left: "  + !goLeft);

    Path path = insidePathFar;
    if (config.pathToTake == ConfigurationAutoMode.WALL_LANE) {
      path = wallPath;
    } else if (config.pathToTake == ConfigurationAutoMode.STRAIGHT_PATH) {
      path = straightPath;
    } else if (config.pathToTake == ConfigurationAutoMode.MIDDLE_LANE) {
      if (config.endClose) {
        path = centerPathClose;
      } else {
        path = centerPathFar;
      }
    } else if (config.pathToTake == ConfigurationAutoMode.INSIDE_LANE) {
      if (config.endClose) {
        path = insidePathFar; // TODO: make this
      } else {
        path = insidePathFar;
      }
    }

    // Drive to correct place
    if (goLeft) {
      path.goLeft();
    }
    else {
      path.goRight();
    }
    drivePath(path, 10);
    System.out.println("Finished driving at: " + autoTimer.get());
   
    // Hold position
    drivebase.resetEncoders();
    headingController.setDistance(0);
    double endHeading = Math.toDegrees(path.getEndHeading());
    System.out.println("Before deke heading: " + endHeading);
    
    // Do deke if needed
    if (endingInOpenField() && config.doDeke && false) {
      if (!goLeft) {
        endHeading = (360.0 - endHeading) * 0.8;
      } else {
        endHeading = endHeading * -0.8;
      }
    }
 
    System.out.println("After deke heading : " + endHeading);
    
    // Turn on heading controller
    headingController.setHeading(endHeading);
    drivebase.useController(headingController);
    
    // Wait until hot goal is about to switch
    waitUntilTime(4.5);
    
    
    // Last shot rpm
    wantedEndRpm = (endingInOpenField() && config.endClose) ? closeIntakeUpPreset : farIntakeUpPreset;
 
    System.out.println("Shooting 1st ball at: " + autoTimer.get() + " after time of hot switch: " + timeOfSwitch);
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

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLine;
  }
}

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
public class AerialAssistAuto extends ConfigurationAutoMode {

  static Path centerPathFar = AutoPaths.get("CenterLanePathFar");
  static Path centerPathClose = AutoPaths.get("CenterLanePathClose");
  static Path wallPath = AutoPaths.get("WallLanePath");
  
  public AerialAssistAuto() {
    super("Default auto mode");
  }
  
  protected void routine() {
    if (config.lane == ConfigurationAutoMode.MIDDLE_LANE && config.endClose) {
      wantedStartRpm = config.numBalls == 0 ? 0 : config.numBalls > 1 ? closeIntakeDownPreset : closeIntakeUpPreset;
    } else {
      wantedStartRpm = config.numBalls == 0 ? 0 : config.numBalls > 1 ? farIntakeDownPreset : farIntakeUpPreset;
    }
    
    hotGoalDetector.startSampling();

    
    // Setller
    settler.set(true);
    
    // Turn on wheel
    shooterController.enable();
    shooterController.setVelocityGoal(wantedStartRpm);
    
    // Grab balls from ground
    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.wantBumperGather = config.numBalls == 3 || (config.numBalls == 2 && !config.preferRearBall);
    rearIntake.wantBumperGather = config.numBalls == 3 || (config.numBalls == 2 && config.preferRearBall);
    waitTime(.5);
    waitTime(.5);
    
    hotGoalDetector.stopSampling();
    boolean goLeft = hotGoalDetector.goLeft();
    System.out.println("Hot goal started on left: "  + !goLeft);
    

    Path path = centerPathClose;
    if (config.lane == ConfigurationAutoMode.WALL_LANE) {
      path = wallPath;
    } else if (config.lane == ConfigurationAutoMode.MIDDLE_LANE) {
      if (config.endClose) {
        path = centerPathClose;
      } else {
        path = centerPathFar;
      }
    }
    // Drive to correct place
    if (goLeft)
      path.goLeft();
    else
      path.goRight();
    drivePath(path, 10);
    System.out.println("Finished driving at: " + autoTimer.get());
   
    // Hold position
    drivebase.resetEncoders();
    headingController.setDistance(0);
    double endHeading = Math.toDegrees(path.getEndHeading());
    System.out.println("before: " + endHeading);
    if (config.lane == ConfigurationAutoMode.MIDDLE_LANE && config.doDeke) {
      if (!goLeft) {
        endHeading = (360.0 - endHeading) * 0.8;
      } else {
        endHeading = endHeading * -0.8;
      }
    }
    System.out.println("after: " + endHeading);
    headingController.setHeading(endHeading);
    drivebase.useController(headingController);
    
    // Wait until hot goal is about to switch
    if (!hotGoalDetector.getNotSure()) {
     waitUntilTime(4.0);
    }
 
    wantedEndRpm = (config.lane == ConfigurationAutoMode.MIDDLE_LANE && config.endClose) ? closeIntakeUpPreset : farIntakeUpPreset;
 
    System.out.println("Shooting 1st ball at: " + autoTimer.get());
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

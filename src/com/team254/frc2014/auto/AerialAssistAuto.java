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
public class AerialAssistAuto extends ConfigurationAutoMode {

  static Path centerPath = AutoPaths.get("CenterLanePath");
  static Path wallPath = AutoPaths.get("WallLanePath");
  
  public AerialAssistAuto() {
    super("Default auto mode");
  }
  
  protected void routine() {
    
    boolean goLeft = true;//config.lane == MIDDLE_LANE;//(Math.floor(Timer.getFPGATimestamp()) % 2 == 0);

    
    // Turn on wheel
    shooterController.setVelocityGoal(config.numBalls == 0 ? 0 : config.numBalls > 1 ? 4000 : 4300);
    
    // Grab balls from ground
    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.wantBumperGather = config.numBalls == 3 || (config.numBalls == 2 && !config.preferRearBall);
    rearIntake.wantBumperGather = config.numBalls == 3 || (config.numBalls == 2 && config.preferRearBall);
    waitTime(.5);
    

    Path path = centerPath;
    if (config.lane == ConfigurationAutoMode.WALL_LANE) {
      path = wallPath;
    } else if (config.lane == ConfigurationAutoMode.MIDDLE_LANE) {
      path = centerPath;
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
    headingController.setHeading(Math.toDegrees(path.getEndHeading()));
    drivebase.useController(headingController);
    
    // Wait until hot goal is about to switch
    waitUntilTime(4.0);
 
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

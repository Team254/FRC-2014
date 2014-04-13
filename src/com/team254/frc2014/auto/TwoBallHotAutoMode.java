package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;
import com.team254.frc2014.Constants;
import com.team254.frc2014.actions.DrivePathWithRunningShotAction;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.trajectory.Path;

/**
 * TwoBallHotAutoMode.java
 *
 * @author tombot
 */
public class TwoBallHotAutoMode extends AutoMode {

  public TwoBallHotAutoMode() {
    super("Two ball hot, straight");
  }
  
  protected void routine() {
    wantedEndRpm = Constants.autonFarIntakeUpPreset.getDouble();
    wantedStartRpm = Constants.runningFarPreset.getDouble();
    headingController.setHeading(0);
    drivebase.useController(headingController);

    // Start voting 
    visionHotGoalDetector.reset();
    visionHotGoalDetector.startSampling();
    
    settler.set(true);
    shooter.setHood(false);
    
    // Turn on shooter
    shooterController.enable();
    shooterController.setVelocityGoal(Constants.runningFarPreset.getDouble());
    
    // Grab balls from ground
    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.wantBumperGather = true;

    
    // Settle the balls, wait for hot goal to flip
    waitTime(.75);
    
    DrivePathWithRunningShotAction driveAction = drivePathWithRunningShot(AutoPaths.get("StraightAheadPath"), 10);
    
    System.out.println("did shot? " + driveAction.didShot());
    
   // Speed up for 2nd and 3rd shots
    shooterController.setVelocityGoal(Constants.autonFarIntakeUpPreset.getDouble());
    if (driveAction.didShot()) {
    frontIntake.wantShoot = false;
    waitTime(0.2);
    frontIntake.wantBumperGather = false;
    frontIntake.wantGather = false;
    
    // Settle time
    waitTime(0.2);
    settler.set(true);
    
      // Shoot second ball
      waitTime(.25);
      rearIntake.setManualRollerPower(Constants.rearRollerShootPower.getDouble());
      settler.set(false);
      clapper.wantShot = true;
      waitTime(.5);
      System.out.println("2nd shot at: " + autoTimer.get());
      clapper.wantShot = false;
      rearIntake.setManualRollerPower(0);
      waitTime(0.3);
      shooterController.setVelocityGoal(0);
    } else {
      shootTwoWithFrontBall();
    }
  }
  
  public DrivePathWithRunningShotAction drivePathWithRunningShot(Path r, double timeout) {
    DrivePathWithRunningShotAction a = new DrivePathWithRunningShotAction(r, null, timeout);
    runAction(a);
    return a;
  }
  


}

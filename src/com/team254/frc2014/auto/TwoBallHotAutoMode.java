package com.team254.frc2014.auto;

import com.team254.frc2014.ConfigurationAutoMode;
import com.team254.frc2014.Constants;
import com.team254.frc2014.actions.DrivePathWithRunningShotAction;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.trajectory.Path;

/**
 * TwoBallHotAutoMode.java
 * 
 * This auto mode is meant to drive straight and either
 * a) Shoot 1 ball on the run, then shoot 1 ball while sitting still in the first 5 seconds
 * b) Drive, then wait until 4.75 seconds to shoot the 2 balls
 *
 * @author Tom Bottiglieri
 */
public class TwoBallHotAutoMode extends ConfigurationAutoMode {

  public TwoBallHotAutoMode() {
    super("Two ball hot, straight");
  }

  protected void routine() {
    wantedEndRpm = Constants.autonFarIntakeUpPreset.getDouble();
    wantedStartRpm = Constants.runningFarPreset.getDouble();
    drivebase.resetEncoders();
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
    pinniped.wantFront = false;
    pinniped.wantRear = false;
    frontIntake.wantBumperGather = true;


    // Settle the balls, wait for hot goal to flip
    waitTime(.75);

    DrivePathWithRunningShotAction driveAction = drivePathWithRunningShot(AutoPaths.get("StraightAheadPath"), 10);

    System.out.println("did shot? " + driveAction.didShot());
    
    
    drivebase.resetEncoders();
    headingController.setDistance(0);
    headingController.setHeading(0);
    drivebase.useController(headingController);

    // Speed up for 2nd and 3rd shots
    shooterController.setVelocityGoal(Constants.autonFarIntakeUpPreset.getDouble());
    if (driveAction.didShot()) {
      System.out.println("Did shot!");
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
      pinniped.wantShot = true;
      waitTime(.5);
      System.out.println("2nd shot at: " + autoTimer.get());
      pinniped.wantShot = false;
      rearIntake.setManualRollerPower(0);
      waitTime(0.3);
      shooterController.setVelocityGoal(0);
    } else {
      waitUntilTime(4.25);
      shootTwoWithFrontBall();
    }
    shooterController.setVelocityGoal(0);
  }

  public DrivePathWithRunningShotAction drivePathWithRunningShot(Path r, double timeout) {
    DrivePathWithRunningShotAction a = new DrivePathWithRunningShotAction(r, visionHotGoalDetector, config.startOnLeft, timeout);
    runAction(a);
    return a;
  }
}

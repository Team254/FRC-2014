package com.team254.frc2014;

import com.team254.frc2014.actions.Action;
import com.team254.frc2014.actions.DriveAction;
import com.team254.frc2014.actions.DrivePathAction;
import com.team254.frc2014.actions.DrivePathWithPathFlipperAction;
import com.team254.frc2014.actions.WaitAction;
import com.team254.frc2014.actions.WaitForHotGoalToSwitchAction;
import com.team254.frc2014.actions.WaitUntilAutonTimeAction;
import com.team254.frc2014.hotgoal.HotGoalDetector;
import com.team254.lib.trajectory.Path;
import edu.wpi.first.wpilibj.Timer;

/**
 * AutoMode provides a model which all autonomi will follow.
 */
public abstract class AutoMode extends ChezyRobot implements Runnable {

  Action currentAction = null;
  Thread autoThread = new Thread(this);
  private boolean alive = true;
  protected String description;
  protected Timer autoTimer = new Timer();

  protected double farIntakeUpPreset = Constants.staticFarPreset.getDouble();
  protected double farIntakeDownPreset = farIntakeUpPreset - 250;
  protected double closeIntakeUpPreset = Constants.autonClosePreset.getDouble();
  protected double closeIntakeDownPreset = closeIntakeUpPreset - 50;
  protected double wantedStartRpm = farIntakeDownPreset;
  protected double wantedEndRpm = farIntakeUpPreset;
    
  
  public AutoMode(String description) {
    this.description = description;
  }

  protected abstract void routine();
  public abstract FieldPosition getFieldPosition();
  public String getDescription() {
    return description;
  }

  public void run() {
    System.out.println("Starting auto mode!");
    try {
      autoTimer.reset();
      autoTimer.start();
      routine();
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("Ending auto mode!");
  }

  public void start() {
    stop();
    alive = true;
    autoThread = new Thread(this);
    autoThread.start();
  }

  public void stop() {
    stopCurrentAction();
    alive = false;
  }

  public void stopCurrentAction() {
    if (currentAction != null) {
      currentAction.kill();
    }
  }

  public void startCurrentAction() {
    if (currentAction != null) {
      currentAction.run();
    }
  }

  public void runAction(Action action) {
    if (alive) {
      stopCurrentAction();
      currentAction = action;
      startCurrentAction();
    }
  }
  
  public void shootThree() {
     // Wait for 5 seconds in
    rearIntake.wantShoot = frontIntake.wantShoot = true;
    waitTime(.5);
    
    // Shoot first ball
    settler.set(false);
    clapper.wantShot = true;
    waitTime(.45);
    clapper.wantShot = false;
    rearIntake.wantShoot = frontIntake.wantShoot = false;
    
    // Speed up for 2nd and 3rd shots
    shooterController.setVelocityGoal(wantedEndRpm);
    
    // Queue 2nd ball
    rearIntake.wantBumperGather = false;
    rearIntake.wantDown = true;
    rearIntake.setManualRollerPower(1.0);
    waitTime(0.3);
    rearIntake.wantDown = false;
    waitTime(.35);
    rearIntake.setManualRollerPower(0);
    
    // Settle time
    waitTime(.25);
    settler.set(true);
    waitTime(.20);
    
    // Shoot second ball
    frontIntake.wantShoot = true;
    waitTime(.25);
    rearIntake.setManualRollerPower(Constants.rearRollerShootPower.getDouble());
    settler.set(false);
    clapper.wantShot = true;
    waitTime(.45);
    clapper.wantShot = false;
    
    rearIntake.setManualRollerPower(0);
    waitTime(0.3);
    frontIntake.wantShoot = false;
    
    // Queue 3rd ball
    frontIntake.wantBumperGather = false;
    frontIntake.wantDown = true;
    frontIntake.setManualRollerPower(1.0);
    waitTime(0.3);
    frontIntake.setManualRollerPower(1.0);
    frontIntake.wantDown = false;
    waitTime(0.45);
    frontIntake.setManualRollerPower(0.00);

    
    // Settle time
    waitTime(.25);
    settler.set(true);
    waitTime(.5);
    
    // Shoot thirdball
    settler.set(false);
    rearIntake.setManualRollerPower(Constants.rearRollerShootPower.getDouble());
    clapper.wantShot = true;
    waitTime(.45);
    clapper.wantShot = false;
  }
  
  public void shootOne() {
    // Speed up for 2nd and 3rd shots
    shooterController.setVelocityGoal(wantedEndRpm);
    // Wait for 5 seconds in
    // Shoot second ball
    waitTime(.5);
    rearIntake.setManualRollerPower(Constants.rearRollerShootPower.getDouble());
    settler.set(false);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    rearIntake.setManualRollerPower(0);
    waitTime(0.3);
    
  }
  
    public void shootTwoWithFrontBall() {
    // Wait for 5 seconds in
    waitTime(.5);
    frontIntake.wantShoot = true;
    waitTime(.75);

    // Shoot first ball
    rearIntake.setManualRollerPower(Constants.rearRollerShootPower.getDouble());
    waitTime(.25);
    settler.set(false);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    rearIntake.setManualRollerPower(0);
    frontIntake.wantShoot = false;
    
    // Speed up for 2nd and 3rd shots
    shooterController.setVelocityGoal(wantedEndRpm);
    
    // Queue 2nd ball
    frontIntake.wantBumperGather = false;
    frontIntake.wantDown = true;
    frontIntake.setManualRollerPower(1.0);
    waitTime(0.3);
    frontIntake.wantDown = false;
    waitTime(.4);
    frontIntake.setManualRollerPower(0);
    
    // Settle time
    waitTime(0.5);
    settler.set(true);
    waitTime(0.75);
    
    // Shoot second ball
    waitTime(.25);
    rearIntake.setManualRollerPower(Constants.rearRollerShootPower.getDouble());
    settler.set(false);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    rearIntake.setManualRollerPower(0);
    waitTime(0.3);
  }
  
  public void shootTwoWithRearBall() {
    // Wait for 5 seconds in
    waitTime(.5);
    rearIntake.wantShoot = true;
    waitTime(1.0);

    // Shoot first ball
    settler.set(false);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    rearIntake.wantShoot = false;
    
    // Speed up for 2nd and 3rd shots
   shooterController.setVelocityGoal(wantedEndRpm);
    
    // Queue 2nd ball
    rearIntake.wantBumperGather = false;
    rearIntake.wantDown = true;
    rearIntake.setManualRollerPower(1.0);
    waitTime(0.3);
    rearIntake.wantDown = false;
    waitTime(.4);
    rearIntake.setManualRollerPower(0);
    
    // Settle time
    waitTime(0.5);
    settler.set(true);
    waitTime(0.75);
    
    // Shoot second ball
    waitTime(.25);
    rearIntake.setManualRollerPower(Constants.rearRollerShootPower.getDouble());
    settler.set(false);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    rearIntake.setManualRollerPower(0);
    waitTime(0.3);
  }

  public void drive(double feet, double timeout) {
    runAction(new DriveAction(feet, timeout));
  }
  
  public void waitForHotGoalToSwitch(double timeout) {
    runAction(new WaitForHotGoalToSwitchAction(timeout));
  }

  public void driveAndCoast(double feet, double timeout) {
    runAction(new DriveAction(feet, 0, false, timeout));
  }
  public void driveArc(double feet, double heading, double timeout) {
    runAction(new DriveAction(feet, heading, false, timeout));
  }
  
  public void waitTime(double seconds) {
    runAction(new WaitAction(seconds));
  }
  
  public void waitUntilTime(double seconds) {
    runAction(new WaitUntilAutonTimeAction(autoTimer, seconds));
  }

  public void dimeStop() {
    drive(0, 0.25);
  }
  
  public void drivePath(Path r, double timeout) {
    runAction(new DrivePathAction(r, timeout)); 
  }
  
  public void drivePathWithFlip(Path r, HotGoalDetector detector, double timeout) {
    runAction(new DrivePathWithPathFlipperAction(r, detector, timeout)); 
  }

}

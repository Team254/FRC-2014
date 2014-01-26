package com.team254.frc2014;

import com.team254.frc2014.actions.Action;
import com.team254.frc2014.actions.DriveAction;
import com.team254.frc2014.actions.DriveAtHeadingUntilXCoordinateAction;
import com.team254.frc2014.actions.DriveAtHeadingUntilYCoordinateAction;
import com.team254.frc2014.actions.DriveToUltrasonicRangeAction;
import com.team254.frc2014.actions.WaitAction;

public abstract class AutoMode extends ChezyRobot implements Runnable {

  Action currentAction = null;
  Thread autoThread = new Thread(this);
  private boolean alive = true;

  protected abstract void routine();

  public void run() {
    System.out.println("Starting auto mode!");
    routine();
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

  public void drive(double feet, double timeout) {
    runAction(new DriveAction(feet, timeout));
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

  public void dimeStop() {
    drive(0, 0.25);
  }

  public void driveAtHeadingToX(double heading, double x, double timeout) {
    runAction(new DriveAtHeadingUntilXCoordinateAction(heading, x, timeout));
  }

  public void driveAtHeadingToY(double heading, double y, double timeout) {
    runAction(new DriveAtHeadingUntilYCoordinateAction(heading, y, timeout));
  }

  public void driveToUltrasonicRange(double range) {
    runAction(new DriveToUltrasonicRangeAction(range));
  }
}

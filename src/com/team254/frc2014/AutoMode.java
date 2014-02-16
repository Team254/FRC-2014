package com.team254.frc2014;

import com.team254.frc2014.actions.Action;
import com.team254.frc2014.actions.DriveAction;
import com.team254.frc2014.actions.DriveAtHeadingUntilXCoordinateAction;
import com.team254.frc2014.actions.DriveAtHeadingUntilYCoordinateAction;
import com.team254.frc2014.actions.DriveRouteAction;
import com.team254.frc2014.actions.DriveToUltrasonicRangeAction;
import com.team254.frc2014.actions.WaitAction;
import com.team254.lib.util.ChezyMath;
import com.team254.path.Path;

/**
 * AutoMode provides a model which all autonomi will follow.
 */
public abstract class AutoMode extends ChezyRobot implements Runnable {

  Action currentAction = null;
  Thread autoThread = new Thread(this);
  private boolean alive = true;
  protected String description;
  
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
  
  
  public void drivePath(Path r, double timeout) {
    runAction(new DriveRouteAction(r, timeout)); 
  }
  
  public void driveSCurve(double deltaX, double deltaY, double startHeading, double timeout) {
    double leftOver =  0;
    double signDx = deltaX < 0 ? -1.0 : 1.0;
    System.out.println("dx " + deltaX + " dy " + deltaY);
    if (deltaY < Math.abs(deltaX)) {
      System.out.println("Y is less than X for S turn. Going straight in middle so we don't go backwards.");
      leftOver = Math.abs(deltaX) - Math.abs(deltaY);
      deltaX = deltaX - leftOver;
    }
    deltaX = Math.abs(deltaX) * signDx;
    double x = Math.abs(deltaX) / 2.0;
    double y = Math.abs(deltaY) / 2.0;
    double radius = ((x * x) + (y * y)) / (2 * x);
    double angle = ChezyMath.asin(y/radius);
    double circumference = 2 * Math.PI * radius;
    double arcLength = (angle / (2 * Math.PI)) * circumference;
    if (deltaX < 0) {
      angle = -angle;
    }
    double angleDeg = Math.toDegrees(angle);
    double endHeading1 = startHeading +  angleDeg;
    System.out.println("x " + x + " y " + y + " rad " + radius + " angle " + angle + " arc " + arcLength + " heading " + endHeading1);
    driveArc(arcLength, endHeading1, timeout / 2.0);
    if (leftOver > 0) {
      driveArc(leftOver, endHeading1, timeout / 3.0);
    }
    driveArc(arcLength, startHeading, timeout / 2.0);
  }
}

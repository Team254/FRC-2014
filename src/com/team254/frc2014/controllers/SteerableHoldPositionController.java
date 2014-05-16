package com.team254.frc2014.controllers;

/**
 * SteerableHoldPositionController.java
 *
 * Let's steer the robot with Cheesy Vision during auto mode!
 * 
 * @author Tom Bottiglieri
 */
public class SteerableHoldPositionController extends HoldPositionController {
  public final static double STEER_DELTA_HEADING = 4.0;
  int oldLeft = 0, oldRight = 0;
  public void update() {
    drivebase.setLowgear(true);
    int left =  visionHotGoalDetector.getLeftCount();
    int right = visionHotGoalDetector.getRightCount();
    double deltaHeading = 0;
    if (left > oldLeft) {
      deltaHeading += STEER_DELTA_HEADING;
    } 
    if (right > oldRight) {
      deltaHeading -= STEER_DELTA_HEADING;
    }
    if (right > oldRight && left > oldLeft) {
      deltaHeading = 0;
    }
    heading += deltaHeading;
    
    oldLeft = left;
    oldRight = right;
    super.update();
  }
  
  public void reset() {
    super.reset();
    oldLeft = visionHotGoalDetector.getLeftCount();
    oldRight = visionHotGoalDetector.getRightCount();
  }

}

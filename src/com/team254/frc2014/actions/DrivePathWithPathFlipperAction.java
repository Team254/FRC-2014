package com.team254.frc2014.actions;

import com.team254.frc2014.hotgoal.HotGoalDetector;
import com.team254.lib.trajectory.Path;

/**
 * DrivePathAction causes the robot to drive along a Path
 *
 */
public class DrivePathWithPathFlipperAction extends DrivePathAction {
  HotGoalDetector detector;
  boolean flipped = false;
  boolean dontEverFlip = false;
  
  public DrivePathWithPathFlipperAction(Path path, HotGoalDetector detector, double timeout) {
    super(path, timeout);
    this.detector = detector;
  }
  
  public boolean execute() {
    int curSegment = driveController.getFollowerCurrentSegment();
    boolean canFlip = path.canFlip(curSegment);
    if (!canFlip) {
      dontEverFlip = true;
    }
    if (!flipped && !dontEverFlip && detector != null) {
      if (canFlip && detector.probablySawGoalChange()) {
        System.out.println("Got a flip! " + detector.goLeft());
        if (detector.goLeft()) {
          hotGoalDirection = "LEFT";
          path.goLeft();
        } else {
          hotGoalDirection = "RIGHT";
          path.goRight();
        }
        flipped = true;
      }
    }
    return super.execute();
  }
}

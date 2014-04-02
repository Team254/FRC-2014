package com.team254.frc2014.actions;

import com.team254.frc2014.hotgoal.HotGoalDetector;
import com.team254.frc2014.hotgoal.VisionHotGoalDetector;
import com.team254.lib.trajectory.Path;
import edu.wpi.first.wpilibj.Timer;

/**
 * DrivePathAction causes the robot to drive along a Path
 *
 */
public class DrivePathWithPathFlipperAction extends DrivePathAction {
  HotGoalDetector detector;
  boolean flipped = false;
  
  public DrivePathWithPathFlipperAction(Path path, HotGoalDetector detector, double timeout) {
    super(path, timeout);
    this.detector = detector;
  }
  
  public boolean execute() {
    int curSegment = driveController.getFollowerCurrentSegment();
    boolean canFlip = path.canFlip(curSegment);
    System.out.println("Fol: " + curSegment + "  " + canFlip);
    if (!flipped && detector != null) {

      
      if (detector.probablySawGoalChange() && canFlip) {
        System.out.println("Got a flip! " + detector.goLeft());
        if (detector.goLeft()) {
          path.goLeft();
        } else {
          path.goRight();
        }
        flipped = true;
      }
    }
    return super.execute();
  }
}

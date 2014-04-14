package com.team254.frc2014.actions;

import com.team254.frc2014.Constants;
import com.team254.frc2014.hotgoal.HotGoalDetector;
import com.team254.frc2014.hotgoal.VisionHotGoalDetector;
import com.team254.lib.trajectory.Path;
import edu.wpi.first.wpilibj.Timer;

/**
 * DrivePathAction causes the robot to drive along a Path
 *
 */
public class DrivePathWithRunningShotAction extends DrivePathAction {
 
  HotGoalDetector detector;
  boolean flipped = false;
  boolean dontEverFlip = false;
  boolean didShot = false;
  Timer didShotTimer = new Timer();
  boolean startedOnLeft = false;
  
  public DrivePathWithRunningShotAction(Path path, HotGoalDetector detector, boolean startedOnLeft, double timeout) {
    super(path, timeout);
    this.detector = detector;
    this.startedOnLeft = startedOnLeft;
  }
  
  public boolean execute() {
    int curSegment = driveController.getFollowerCurrentSegment();
    int numSegments = driveController.getNumSegments();
    
    double percentDone = 0;
    if (numSegments != 0) {
      percentDone = (curSegment * 1.0)  / (numSegments * 1.0);
    }
    
   
    
    boolean goalStartedHot = false;
    
    if (startedOnLeft && detector.getLeftCount() > 5){
      goalStartedHot = true;
    } else if (!startedOnLeft && detector.getRightCount() > 5){
      goalStartedHot = true;
    }
    
    
    if (goalStartedHot && percentDone > .35 && !didShot) {
      frontIntake.wantShoot = true;
      rearIntake.setManualRollerPower(Constants.rearRollerShootPower.getDouble());
      settler.set(false);
      clapper.wantTimedShot = true;
      clapper.wantShot = true;
      didShot = true;
      didShotTimer.start();
    }
    
    if (didShot && didShotTimer.get() > .5) {
      frontIntake.wantBumperGather = false;
      frontIntake.wantGather = true;
    }
    
    return super.execute();
  }
  
  public boolean didShot() {
    return didShot;
  }
  
  public void done() {
    frontIntake.wantShoot = false;
    clapper.wantTimedShot = false;
    clapper.wantShot = false;
  }
}

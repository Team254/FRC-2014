package com.team254.frc2014.actions;

import com.team254.lib.Route;
import edu.wpi.first.wpilibj.Timer;

/**
 * DrivePathAction causes the robot to drive along a Path
 *
 */
public class DriveRouteAction extends Action {

  double heading;
  Route r;

  public DriveRouteAction(Route route, double timeout) {
    r = route;
    setTimeout(timeout);
  }

  public boolean execute() {
    return isTimedOut() || driveController.onTarget();
  }

  public void init() {
    System.out.println("Init Drive " + Timer.getFPGATimestamp());
    drivebase.resetEncoders();
    driveController.loadProfile(r.leftTrajectory, r.rightTrajectory, 1.0, heading);
    drivebase.useController(driveController);
    driveController.enable();
  }

  public void done() {
    System.out.println("Done Drive " + Timer.getFPGATimestamp());
    driveController.disable();
    drivebase.setLeftRightPower(0, 0);
  }
}

package com.team254.frc2014.actions;

import com.team254.frc2014.Constants;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.PathGenerator;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryGenerator;

/**
 * DrivePathAction causes the robot to drive along a Path
 *
 */
public class DrivePathAction extends Action {

  double heading;
  Path p;

  public DrivePathAction(Path path, double timeout) {
    p = path;
    setTimeout(timeout);
  }

  public boolean execute() {
    return isTimedOut() || driveController.onTarget();
  }

  public void init() {
    drivebase.resetEncoders();
    double width = Constants.robotWidth.getDouble();

    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 12.0;
    config.max_jerk = 75.0;
    config.max_vel = 10.0;
    Trajectory traj = PathGenerator.generateFromPath(p, config);
    Trajectory[] output = PathGenerator.makeLeftAndRightTrajectories(traj, width);
    driveController.loadProfile(output[0], output[1], 1.0, heading);
    drivebase.useController(driveController);
    driveController.enable();
  }

  public void done() {
    driveController.disable();
    drivebase.setLeftRightPower(0, 0);
  }
}

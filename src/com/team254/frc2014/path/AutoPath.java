package com.team254.frc2014.path;

import com.team254.frc2014.Constants;
import com.team254.lib.Route;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.PathGenerator;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryGenerator;

/**
 * AutoPath.java
 * 
 * An AutoPath holds a route for the left and right hot goal. These routes
 * should be mirror images.
 *
 * @author tombot
 */
public abstract class AutoPath {

  Route leftRoute;
  Route rightRoute;
 
  public Route getLeftGoalRoute() {
    return leftRoute;
  }

  public Route getRightGoalRoute() {
    return rightRoute;
  }

  protected abstract Path definePath(boolean goLeft);

  public void generatePaths() {
    Path leftPath = definePath(true);
    Path rightPath = definePath(false);
    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 12.0;
    config.max_jerk = 75.0;
    config.max_vel = 10.0;
    for (int i = 0; i < 2; i++) {
      Route newHolder = new Route();
      newHolder.path = i == 0 ? leftPath : rightPath;
      Trajectory traj = PathGenerator.generateFromPath(newHolder.path, config);
      double width = Constants.robotWidth.getDouble();
      Trajectory[] output = PathGenerator.makeLeftAndRightTrajectories(traj, width);
      newHolder.leftTrajectory = output[0];
      newHolder.rightTrajectory = output[1];
      if (i == 0) {
        leftRoute = newHolder;
      } else {
        rightRoute = newHolder;
      }
    }
  }
}

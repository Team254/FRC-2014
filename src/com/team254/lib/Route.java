package com.team254.lib;

import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Trajectory;

/**
 * Route.java
 * 
 * A route holds a path and left/right wheel trajectories
 * 
 * @author tombot
 */
public class Route {
  public Path path;
  public Trajectory leftTrajectory;
  public Trajectory rightTrajectory;
}
package com.team254.frc2014;

import com.team254.lib.trajectory.Path;

/**
 * AutoPaths.java
 *
 * @author tombot
 */
public class AutoPaths {
  public static Path autoSPath() {
    Path p = new Path(10);
    p.addWaypoint(new Path.Waypoint(0, 0, 0));
    p.addWaypoint(new Path.Waypoint(5, 0, 0));
    p.addWaypoint(new Path.Waypoint(14, -7, 0));
    p.addWaypoint(new Path.Waypoint(16, -7, 0));
    return p;
  }
  
  public static Path autoWallPath() {
    Path p = new Path(10);
    p.addWaypoint(new Path.Waypoint(0, 0, 0));
    p.addWaypoint(new Path.Waypoint(5, 0, 0));
    p.addWaypoint(new Path.Waypoint(13.25, 9.5, 0));
    return p;
  }

}

package com.team254.frc2014.path;

import com.team254.lib.trajectory.Path;

/**
 * DriveToWallPath.java
 *
 * @author tombot
 */
public class DriveToWallPath extends AutoPath {
  protected Path definePath(boolean goLeft) {
    double dir = goLeft ? 1.0 : -1.0;
    Path p = new Path(10);
    p.addWaypoint(new Path.Waypoint(0, 0, 0));
    p.addWaypoint(new Path.Waypoint(5, 0, 0));
    p.addWaypoint(new Path.Waypoint(12, dir * 10, 0));
    p.addWaypoint(new Path.Waypoint(14, dir * 10, 0));
    System.out.println(goLeft + "\n" + p);
    return p;
  }
  
  public static DriveToWallPath getInstance() {
    return (DriveToWallPath) AutoPathFactory.get(DriveToWallPath.class);
  }
}

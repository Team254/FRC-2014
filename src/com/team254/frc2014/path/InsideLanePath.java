package com.team254.frc2014.path;

import com.team254.lib.trajectory.Path;

/**
 * CenterLanePath.java
 *
 * @author tombot
 */
public class InsideLanePath extends AutoPath {

  protected Path definePath(boolean goLeft) {
    double dir = goLeft ? 1.0 : -1.0;
    Path p = new Path(10);
    p.addWaypoint(new Path.Waypoint(0, 0, 0));
    p.addWaypoint(new Path.Waypoint(5, 0, 0));
    p.addWaypoint(new Path.Waypoint(14, dir * 3.5, 0));
    p.addWaypoint(new Path.Waypoint(17, dir * 3.5, 0));
    return p;
  }

  public static InsideLanePath getInstance() {
    return (InsideLanePath) AutoPathFactory.get(InsideLanePath.class);
  }
}

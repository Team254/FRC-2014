package com.team254.frc2014.path;

import com.team254.lib.trajectory.Path;

/**
 * CenterLanePath.java
 *
 * @author tombot
 */
public class OutsideLanePath extends AutoPath {

  protected Path definePath(boolean goLeft) {
    double dir = goLeft ? 1.0 : -1.0;
    Path p = new Path(10);
    p.addWaypoint(new Path.Waypoint(0, 0, 0));
    p.addWaypoint(new Path.Waypoint(5, 0, 0));
    p.addWaypoint(new Path.Waypoint(14, dir * 8, 0));
    p.addWaypoint(new Path.Waypoint(17, dir * 8, 0));
    return p;
  }

  public static OutsideLanePath getInstance() {
    return (OutsideLanePath) AutoPathFactory.get(OutsideLanePath.class);
  }
}

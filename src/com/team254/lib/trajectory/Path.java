package com.team254.lib.trajectory;

/**
 * A Path is a series of Waypoints.
 *
 * @author Art Kalb
 * @author Stephen Pinkerton
 * @author Jared341
 */
public class Path {

  public static class Waypoint {

    public Waypoint(double x, double y, double theta) {
      this.x = x;
      this.y = y;
      this.theta = theta;
    }

    public double x;
    public double y;
    public double theta;
  }

  Waypoint[] waypoints_;
  int num_waypoints_;

  public Path(int max_size) {
    waypoints_ = new Waypoint[max_size];
  }

  public void addWaypoint(Waypoint w) {
    if (num_waypoints_ < waypoints_.length) {
      waypoints_[num_waypoints_] = w;
      ++num_waypoints_;
    }
  }

  public int getNumWaypoints() {
    return num_waypoints_;
  }

  public Waypoint getWaypoint(int index) {
    if (index >= 0 && index < getNumWaypoints()) {
      return waypoints_[index];
    } else {
      return null;
    }
  }
}

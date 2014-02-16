package com.team254.path;

import com.team254.lib.trajectory.Trajectory;

/**
 * Base class for an autonomous path.
 * 
 * @author Jared341
 */
public class Path {
  protected Trajectory.Pair go_left_pair_;
  protected String name_;
  protected boolean go_left_;
  
  public Path(String name, Trajectory.Pair go_left_pair) {
    name_ = name;
    go_left_pair_ = go_left_pair;
    go_left_ = true;
  }
  
  public String getName() { return name_; }
  
  public void goLeft() { 
    go_left_ = true; 
    go_left_pair_.left.setInvertedY(false);
    go_left_pair_.right.setInvertedY(false);
  }
  
  public void goRight() {
    go_left_ = true; 
    go_left_pair_.left.setInvertedY(true);
    go_left_pair_.right.setInvertedY(true);
  }
  
  public Trajectory getLeftWheelTrajectory() {
    return go_left_pair_.left;
  }
  
  public Trajectory getRightWheelTrajectory() {
    return go_left_pair_.right;
  }
}

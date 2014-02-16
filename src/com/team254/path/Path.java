package com.team254.path;

import com.team254.lib.trajectory.Trajectory;

/**
 * Base class for an autonomous path.
 * 
 * @author Jared341
 */
public class Path {
  protected Trajectory.Pair go_left_pair_;
  protected Trajectory.Pair go_right_pair_;;
  protected String name_;
  protected boolean go_left_;
  
  public Path(String name, Trajectory.Pair go_left_pair, 
          Trajectory.Pair go_right_pair) {
    name_ = name;
    go_left_pair_ = go_left_pair;
    go_right_pair_ = go_right_pair;
    go_left_ = true;
  }
  
  public String getName() { return name_; }
  
  public void goLeft() { go_left_ = true; }
  
  public void goRight() { go_left_ = false; }
  
  public Trajectory getLeftWheelTrajectory() {
    return (go_left_ ? go_left_pair_.left : go_right_pair_.left);
  }
  
  public Trajectory getRightWheelTrajectory() {
    return (go_left_ ? go_left_pair_.right : go_right_pair_.right);
  }
}

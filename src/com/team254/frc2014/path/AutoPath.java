package com.team254.frc2014.path;

import com.team254.lib.trajectory.Path;

/**
 * AutoPaths.java
 *
 * @author tombot
 */
public abstract class AutoPath {

  Path leftPath;
  Path rightPath;
 
  public Path getLeftGoalPath() {
    return leftPath;
  }
  public Path getRightGoalPath() {
    return rightPath;
  }
  protected abstract Path definePath(boolean goLeft);
  
  public void generatePaths() {
    leftPath = definePath(true);
    rightPath = definePath(false);
  }
}

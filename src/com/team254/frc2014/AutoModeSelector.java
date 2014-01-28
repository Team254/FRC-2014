package com.team254.frc2014;

import java.util.Vector;

/**
 * AutoModeSelector.java
 *
 * @author tombot
 */
public class AutoModeSelector {
  private int currentIndex = 0;
  int lane = 0;
  Vector autoModes = new Vector();
  
  public void addAutoMode(AutoMode m) {
    autoModes.addElement(m);
  }
  
  public AutoMode currentAutoMode() {
    AutoMode am = (AutoMode) autoModes.elementAt(currentIndex % autoModes.size());
    if (am instanceof LanedAutoMode) {
      ((LanedAutoMode) am).setLane(lane);
    }
    return am;
  }
  
  public int getSeletedId() {
    return (currentIndex % autoModes.size())+ 1;
  }
  
  public void increment() {
    currentIndex++;
  }
  
  public void incrementLane() {
    lane++;
    if (lane > LanedAutoMode.WALL_LANE) {
      lane = LanedAutoMode.MIDDLE_LANE;
    }
    currentAutoMode();
  }
  
  public String getLaneName() {
    switch (lane) {
      case LanedAutoMode.MIDDLE_LANE:
        return "Middle";
      case LanedAutoMode.OUTSIDE_LANE:
        return "Outside";
      case LanedAutoMode.INSIDE_LANE:
        return "Inside";
      case LanedAutoMode.WALL_LANE:
        return "Wall";
    }
    return "broked!";
  }
}

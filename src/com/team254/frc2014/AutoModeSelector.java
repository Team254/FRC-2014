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
    return am;
  }
  
  public int getSeletedId() {
    return (currentIndex % autoModes.size())+ 1;
  }
  
  public void increment() {
    currentIndex++;
  }
}

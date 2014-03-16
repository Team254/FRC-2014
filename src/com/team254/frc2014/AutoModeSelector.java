package com.team254.frc2014;

import java.util.Vector;

/**
 * AutoModeSelector.java
 * Selects which automode to use
 * @author tombot
 */
public class AutoModeSelector {
  
  public class Configuration {
    public int startPos;
    public int lane;
    public int numBalls;
    public boolean doDeke = false;
    public boolean preferRearBall = true;
    public boolean endClose = false;
    public Configuration(int startPos, int lane, int numBalls, boolean doDeke, boolean preferRearBall, boolean endClose) {
      this.startPos = startPos;
      this.lane = lane;
      this.numBalls = numBalls;
      this.doDeke = doDeke;
      this.preferRearBall = preferRearBall;
      this.endClose = endClose;
    }
  }
  
  Configuration configuration = new Configuration(0, ConfigurationAutoMode.MIDDLE_LANE, 3, false, true, false);
  private int currentIndex = 0;
  Vector autoModes = new Vector();
  
  public void addAutoMode(AutoMode m) {
    autoModes.addElement(m);
  }
  
  public AutoMode currentAutoMode() {
    AutoMode am = (AutoMode) autoModes.elementAt(currentIndex % autoModes.size());
    if (am instanceof ConfigurationAutoMode) {
      ((ConfigurationAutoMode) am).setConfiguration(configuration);
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
    configuration.lane++;
    if (configuration.lane > ConfigurationAutoMode.WALL_LANE) {
      configuration.lane = ConfigurationAutoMode.MIDDLE_LANE;
    }
    currentAutoMode();
  }
  
  public String getLaneName() {
    switch (configuration.lane) {
      case ConfigurationAutoMode.MIDDLE_LANE:
        return "Middle";
      case ConfigurationAutoMode.WALL_LANE:
        return "Wall";
    }
    return "broked!";
  }
  
  public void decrementNumBalls() {
    if (configuration.numBalls != 2) {
      configuration.preferRearBall = true;
    }

    if (configuration.numBalls == 2 && configuration.preferRearBall) {
      configuration.preferRearBall = false;
    } else {
      configuration.numBalls--;
    }

    if (configuration.numBalls < 0) {
      configuration.numBalls = 3;
    }
    currentAutoMode();
  }
  
  public void toggleDoDeke() {
    configuration.doDeke = !configuration.doDeke;
    currentAutoMode();
  }
  
  public void toggleEndClose() {
    configuration.endClose = !configuration.endClose;
    currentAutoMode();
  }
  
  public boolean getEndClose() {
    return configuration.endClose;
  }
  
  public boolean getDoDeke() {
    return configuration.doDeke;
  }
  
  public int getNumBalls() {
    return configuration.numBalls;
  }
  
  private void togglePreferRearBall() {
    configuration.preferRearBall = !configuration.preferRearBall;
    currentAutoMode();
  }
  
  public boolean getPreferRearBall() {
    return configuration.preferRearBall;
  }
  
  public String getNumBallsWithPreference() {
    int n = getNumBalls();
    String s = "" + n;
    if (n == 2) {
      s += getPreferRearBall() ? "R" : "F";
    }
    return s;
  }
}

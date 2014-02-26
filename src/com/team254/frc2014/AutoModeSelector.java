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
    boolean doDeke = false;
    public Configuration(int startPos, int lane, int numBalls, boolean doDeke) {
      this.startPos = startPos;
      this.lane = lane;
      this.numBalls = numBalls;
      this.doDeke = doDeke;
    }
  }
  
  Configuration configuration = new Configuration(0, ConfigurationAutoMode.MIDDLE_LANE, 3, false);
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
      case ConfigurationAutoMode.OUTSIDE_LANE:
        return "Outside";
      case ConfigurationAutoMode.INSIDE_LANE:
        return "Inside";
      case ConfigurationAutoMode.WALL_LANE:
        return "Wall";
    }
    return "broked!";
  }
  
  public void decrementNumBalls() {
    configuration.numBalls--;
    if (configuration.numBalls < 1) {
      configuration.numBalls = 3;
    }
    currentAutoMode();
  }
  
  public void toggleDoDeke() {
    configuration.doDeke = !configuration.doDeke;
    currentAutoMode();
  }
  
  public boolean getDoDeke() {
    return configuration.doDeke;
  }
  
  public int getNumBalls() {
    return configuration.numBalls;
  }
}

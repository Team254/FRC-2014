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
  
  private class AutoModeHolder {
    public String name;
    public Class mode;
    public AutoModeHolder(String name, Class mode) {
      this.name = name;
      this.mode = mode;
    }
  }
  
  public void addAutoMode(String name, Class mode) {
    autoModes.addElement(new AutoModeHolder(name, mode));
  }
  
  public String currentAutoMode() {
    AutoModeHolder amh = (AutoModeHolder) autoModes.elementAt(currentIndex);
    return amh.name;
  }
  
  public AutoMode instantiateAutoMode() {
    AutoModeHolder amh = (AutoModeHolder) autoModes.elementAt(currentIndex);
    Class c = amh.mode;
    AutoMode a = null;
    try {
       a = (AutoMode) c.newInstance();
       if (a instanceof LanedAutoMode) {
         ((LanedAutoMode) a).setLane(lane);
       }
    } catch (InstantiationException ex) {
      ex.printStackTrace();
    } catch (IllegalAccessException ex) {
      ex.printStackTrace();
    }
    return a;
  }
  
  
}

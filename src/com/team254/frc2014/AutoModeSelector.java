package com.team254.frc2014;

import com.team254.frc2014.paths.AutoPaths;
import java.util.Vector;

/**
 * AutoModeSelector.java
 * Selects which auto mode to use
 * @author Tom Bottiglieri (tom@team254.com)
 */
public class AutoModeSelector {

  public class Configuration {
    public boolean startOnLeft;
    public int pathToTake;
    public int numBalls;
    public boolean doSteer = false;
    public boolean preferRearBall = true;
    public Configuration(boolean startOnLeft, int path, int numBalls, boolean doDeke, boolean preferRearBall) {
      this.startOnLeft = startOnLeft;
      this.pathToTake = path;
      this.numBalls = numBalls;
      this.doSteer = doDeke;
      this.preferRearBall = preferRearBall;
    }
  }
  
  Configuration configuration = new Configuration(true, 0, 3, false, true);
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
    configuration.pathToTake++;
    if (configuration.pathToTake >= AutoPaths.kPathDescriptions.length) {
      configuration.pathToTake = 0;
    }
    currentAutoMode();
  }
  
  public String getPathName() {
    if (configuration.pathToTake >= 0 && configuration.pathToTake < AutoPaths.kPathDescriptions.length) {
      return AutoPaths.kPathDescriptions[configuration.pathToTake];
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
  
  public void toggleDoSteer() {
    configuration.doSteer = !configuration.doSteer;
    currentAutoMode();
  }
  
  public boolean getDoSteer() {
    return configuration.doSteer;
  }
  
  public void toggleStartOnLeft() {
    configuration.startOnLeft = !configuration.startOnLeft;
    currentAutoMode();
  }
  
  public boolean getStartOnLeft() {
    return configuration.startOnLeft;
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

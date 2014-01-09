package com.team254.frc2014;

/**
 * LanedAutoMode.java
 *
 * @author tombot
 */
public abstract class LanedAutoMode extends AutoMode {

  int lane;

  public LanedAutoMode(int lane) {
    super();
    this.lane = lane;
  }

  public void setLane(int lane) {
    this.lane = lane;
  }
}
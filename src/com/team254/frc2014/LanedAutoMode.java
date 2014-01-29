package com.team254.frc2014;

/**
 * LanedAutoMode.java
 * LanedAutoMode defines all autonomi which utilize lanes.
 * @author tombot
 */
public abstract class LanedAutoMode extends AutoMode {

  public LanedAutoMode(String d) {
    super(d);
  }
  
  protected int lane = MIDDLE_LANE;
  public static final int MIDDLE_LANE = 0;
  public static final int INSIDE_LANE = 1;
  public static final int OUTSIDE_LANE = 2;
  public static final int WALL_LANE = 3;

  public void setLane(int lane) {
    this.lane = lane;
  }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.frc2014.hotgoal;

/**
 *
 * @author tombot
 */
public interface HotGoalDetector {
  public boolean probablySawGoalChange();
  public void startSampling();
  public void stopSampling();
  public boolean getNotSure();
  public void reset();
  public boolean goLeft();
  
}

package com.team254.frc2014.subsystems;

import com.team254.lib.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;

/**
 * This determines which side has the hot goal lit.
 * @author tombot
 */
public class HotGoalDetector extends Subsystem  {
  private static DigitalInput leftSensor = new DigitalInput(12);
  private static DigitalInput rightSensor = new DigitalInput(11);
  
  private int leftCount = 0;
  private int rightCount = 0;
  private boolean sampling = false;
  
  public void reset() {
    sampling = false;
    leftCount = rightCount = 0;
  }

  public HotGoalDetector() {
    super("HotGoalDetector");
  }
  
  public boolean getLeftRaw() {
    return leftSensor.get();
  }

  public boolean getRightRaw() {
     return rightSensor.get();
  }
  public Hashtable serialize() {
    return new Hashtable();
  }
  
  private void updateVotes() {
    leftCount += getLeftRaw() ? 1 : 0;
    rightCount += getRightRaw() ? 1 : 0;
  }
  
  public boolean hotGoalStartedOnLeft() {
    System.out.println("left: " + leftCount + "right " + rightCount);
    return leftCount > rightCount;
  }
  
  public void startSampling() {
    sampling = true;
  }
  
  public void stopSampling() {
    sampling = false;
  }
  public void update() {
    super.update();
    if (sampling) {
      updateVotes();
    }
  }
  
}

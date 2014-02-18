package com.team254.frc2014.subsystems;

import com.team254.lib.Subsystem;
import com.team254.lib.util.Util;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;

/**
 * This determines which side has the hot goal lit.
 * @author tombot
 */
public class HotGoalDetector extends Subsystem {
  private static DigitalInput leftSensor = new DigitalInput(12);
  private static DigitalInput rightSensor = new DigitalInput(11);
  
  private boolean disabledLeft;
  private boolean disabledRight;
  private boolean autonLeft;
  private boolean autonRight;
  

  public HotGoalDetector() {
    super("HotGoalDetector");
  }
  
  public boolean getLeft() {
    return !leftSensor.get();
  }

  public boolean getRight() {
     return !rightSensor.get();
  }
  public Hashtable serialize() {
    return new Hashtable();
  }
  
  public void updateDisabled() {
    
    disabledLeft = getLeft();
    disabledRight = getRight();
  }
  
  public void updateAutonomous() {
    autonLeft = getLeft();
    autonRight = getRight();
  }
  
  public boolean gotoLeftGoal() {
    return Util.isLeftGoal(disabledLeft, disabledRight, autonLeft, autonRight);
  }
  
  public boolean hotGoalIsOnLeft() {
    System.out.println("timer: " + (int) Timer.getFPGATimestamp());
    return ((int) Timer.getFPGATimestamp()) % 2 == 0;
  }
  
}

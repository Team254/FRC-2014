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
  
    /**
   * Returns true if the robot should go left, false if right.
   * @param ld
   * @param rd
   * @param la
   * @param ra
   * @return
   */
  public static boolean isLeftGoal(boolean ld, boolean rd,
                                     boolean la, boolean ra) {
    if(la && !ra){
      return true;
    } else if (ra && !la) {
      return false;
    } else if(!ra && !la) {
      if(ld){
        return false; // we saw left and now dont. assume right stayed up and we are blind.
      } else if(rd) {
        return true; // we saw right and now dont. assume left stayed up and we are blind.
      } else {
        //Uncertain
        return true;
      }
    } else {
      //Uncertain
      return false;
    }
  }

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
    return !isLeftGoal(disabledLeft, disabledRight, autonLeft, autonRight);
  }
  
  public boolean hotGoalIsOnLeft() {
    System.out.println("timer: " + (int) Timer.getFPGATimestamp());
    return ((int) Timer.getFPGATimestamp()) % 2 == 0;
  }
  
}

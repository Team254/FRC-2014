package com.team254.frc2014.subsystems;

import com.team254.lib.Subsystem;
import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;

/**
 *
 * @author tombot
 */
public class HotGoalDetector extends Subsystem {

  public HotGoalDetector() {
    super("HotGoalDetector");
  }

  public Hashtable serialize() {
    return new Hashtable();
  }
  
  public boolean hotGoalIsOnLeft() {
    System.out.println("timer: " + (int) Timer.getFPGATimestamp());
    return ((int) Timer.getFPGATimestamp()) % 2 == 0;
  }
  
}

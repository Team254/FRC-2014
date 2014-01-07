package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;
import edu.wpi.first.wpilibj.Timer;

public class ThreeBallAuto extends AutoMode {

  protected void routine() {
    drive(0, 1);
    System.out.println("auto 1 " + Timer.getFPGATimestamp());
    waitTime(3);
    System.out.println("auto 2 " + Timer.getFPGATimestamp());
    drive(0, 1);
    System.out.println("auto 3 " + Timer.getFPGATimestamp());
    waitTime(3);
    System.out.println("auto 4 " + Timer.getFPGATimestamp());
    drive(0, 1);
    System.out.println("auto 5 " + Timer.getFPGATimestamp());
    waitTime(5);
    System.out.println("auto 6 " + Timer.getFPGATimestamp());
  }
}

package com.team254.frc2014.auto;

import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.LanedAutoMode;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Mani Gnanasivam
 * @author EJ Sebathia
 */
public class TestThreeBallShootAuto extends LanedAutoMode {

  public TestThreeBallShootAuto() {
    super("Test Shoooting");
  }

  protected void routine() {
    Timer t = new Timer();
    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.wantDown = true;
    rearIntake.wantDown = true;
    shooterController.setVelocityGoal(4200);
    waitTime(2.0);
    t.start();
    clapper.wantFront = true;
    clapper.wantRear = true;
    waitTime(1.0);
    clapper.wantRear = false;
    waitTime(0.2);
    rearIntake.wantDown = false;
    waitTime(0.8);
    clapper.wantRear = true;
    waitTime(1.0);
    clapper.wantFront = false;
    waitTime(0.2);
    frontIntake.wantDown = false;
    waitTime(0.8);
    clapper.wantFront = true;
    waitTime(.25);
    System.out.println(t.get());
    frontIntake.wantDown = true;

    waitTime(1.0);
    
  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLine;
  }
}

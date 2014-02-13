package com.team254.frc2014.auto;

import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.LanedAutoMode;

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
    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.setPositionDown(true);
    rearIntake.setPositionDown(true);
    shooterController.setVelocityGoal(4200);
    waitTime(2.0);
    clapper.wantFront = true;
    clapper.wantRear = true;
    waitTime(1.0);
    clapper.wantRear = false;
    waitTime(0.2);
    rearIntake.setPositionDown(false);
    waitTime(0.8);
    clapper.wantRear = true;
    waitTime(1.0);
    clapper.wantFront = false;
    waitTime(0.2);
    frontIntake.setPositionDown(false);
    waitTime(0.8);
    clapper.wantFront = true;
    waitTime(1.0);
    
  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLine;
  }
}

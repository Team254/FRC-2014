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
    
    drivebase.resetEncoders();
    drivebase.resetGyro();
    headingController.setDistance(0);
    headingController.setHeading(0);
    drivebase.useController(headingController);
    
    
    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.wantDown = true;
    rearIntake.wantDown = true;
    shooterController.setVelocityGoal(4000);
    waitTime(2.0);
    t.start();
 
    rearIntake.setManualRollerPower(0);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    rearIntake.setManualRollerPower(0);
   
    shooterController.setVelocityGoal(4300);
    rearIntake.setManualRollerPower(.5);
    waitTime(0.3);
    rearIntake.wantDown = false;
    waitTime(.4);
    rearIntake.setManualRollerPower(0);
    waitTime(.9);
    
    rearIntake.setManualRollerPower(1);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    rearIntake.setManualRollerPower(0);

    waitTime(0.2);
    frontIntake.setManualRollerPower(0.5);
    frontIntake.wantDown = false;
    waitTime(0.8);
    frontIntake.setManualRollerPower(0.00);
    waitTime(0.5);
    
    rearIntake.setManualRollerPower(1);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    System.out.println(t.get());
    rearIntake.setManualRollerPower(0);
    shooterController.setVelocityGoal(0);
  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLine;
  }
}

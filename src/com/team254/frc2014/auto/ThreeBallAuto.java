package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;
import com.team254.frc2014.FieldPosition;

public class ThreeBallAuto extends AutoMode {
  public ThreeBallAuto() {
    super("Score three balls");
  }
  
  protected void routine() {
    // Intakes the first ball
    intake.setPositionDown(true);
    waitTime(0.25);
    intake.setAutoIntake(true);
   /*intake.setIntakeRoller(1.0);
    waitTime(.4);
    intake.setIntakeRoller(-.5);
    waitTime(.1);
    intake.setIntakeRoller(0.0);
    */
    
    waitTime(1);
    // Drive to the goal
    driveAndCoast(5, 6);

    // Turn
    driveAtHeadingToX(35, 3.5, 15);

    dimeStop();
    
    //Turn shooter on before we get to the goal
   // shooter.setShooter(1.0);
    
    driveAtHeadingToY(0, 15.0, 15);
    dimeStop();

    // First shot
    intake.setAutoIntake(false);
    waitTime(2.0);
    shooter.setPopper(true);
    waitTime(2.0);
    shooter.setPopper(false); // Post first shot
   // shooter.setShooter(0);
    waitTime(2.50);
    intake.setManualRollerPower(1.0);
    intake.setPositionDown(false);
    
    waitTime(1.0);
    // Intake ball into the shooter system
    intake.setManualRollerPower(1.0);
    waitTime(1.5);
  //  shooter.setShooter(1.0);
    waitTime(1.5);
    intake.setManualRollerPower(0.0);
    // Second shot
    shooter.setPopper(true);
    waitTime(2.0);
    shooter.setPopper(false);
    
  //  shooter.setShooter(0.0);


    dimeStop();
  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLineOffsetByBall;
  }
}

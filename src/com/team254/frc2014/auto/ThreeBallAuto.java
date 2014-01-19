package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;
import com.team254.frc2014.ChezyRobot;

public class ThreeBallAuto extends AutoMode {

  protected void routine() {   
    driveController.setHeadingGoal(0);
    
    // Intakes the first ball
    intake.setSolenoid(true);
    waitTime(0.25);
    intake.setIntakeRoller(1.0);
    waitTime(.4);
    intake.setIntakeRoller(-.5);
    waitTime(.1);
    intake.setIntakeRoller(0.0);
    
    // Drive to the goal
    driveAndCoast(5, 6);

    // Turn
    driveAtHeadingToX(35, 3.5, 15);

    System.out.println("nav: " + ChezyRobot.driveController.navigator.toString());

    dimeStop();
    
    //Turn shooter on before we get to the goal
    shooter.setShooter(1.0);
    
    driveAtHeadingToY(0, 18.0, 15);
    dimeStop();

    // First shot
    waitTime(2.0);
    shooter.setPopper(true);
    waitTime(2.0);
    shooter.setPopper(false); // Post first shot
    shooter.setShooter(0);
    waitTime(2.50);
    intake.setIntakeRoller(1.0);
    intake.setSolenoid(false);
    
    waitTime(1.0);
    // Intake ball into the shooter system
    intake.setIntakeRoller(1.0);
    waitTime(1.5);
    shooter.setShooter(1.0);
    waitTime(1.5);
    intake.setIntakeRoller(0.0);
    // Second shot
    shooter.setPopper(true);
    waitTime(2.0);
    shooter.setPopper(false);
    
    shooter.setShooter(0.0);

    System.out.println("nav: " + ChezyRobot.driveController.navigator.toString());

    dimeStop();

  }
}

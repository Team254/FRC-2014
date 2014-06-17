package com.team254.frc2014.auto;

import com.team254.frc2014.ChezyRobot;
import com.team254.frc2014.ConfigurationAutoMode;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author tombot
 */
public class TestGooglyEyes extends ConfigurationAutoMode {

  public TestGooglyEyes() {
    super("TestGooglyEyes");
  }

  protected void routine() {
    bannerHotGoalDetector.startSampling();

    Timer t = new Timer();
    t.start();



    shooterController.setVelocityGoal(5300);
    // Grab balls from ground
    pinniped.wantFront = false;
    pinniped.wantRear = false;
    waitForHotGoalToSwitch(1.5);


    boolean goLeft = bannerHotGoalDetector.goLeft();
    System.out.println("go left: " + goLeft);

    shooterController.setVelocityGoal(0);





    waitTime(1);
    System.out.println("t: " + autoTimer.get() + " " + ChezyRobot.operatorJoystick.getNoMotorInboundButton());

    waitTime(1);
    System.out.println("t: " + autoTimer.get() + " " + ChezyRobot.operatorJoystick.getPreset1Button());

    waitTime(1);
    System.out.println("t: " + autoTimer.get() + " " + ChezyRobot.operatorJoystick.getPreset1Button());

    waitTime(1);
     System.out.println("t: " + autoTimer.get() + " " + ChezyRobot.operatorJoystick.getPreset1Button());

    waitTime(1);
     System.out.println("t: " + autoTimer.get() + " " + ChezyRobot.operatorJoystick.getPreset1Button());

    waitTime(1);
     System.out.println("t: " + autoTimer.get() + " " + ChezyRobot.operatorJoystick.getPreset1Button());

    waitTime(1);
     System.out.println("t: " + autoTimer.get() + " " + ChezyRobot.operatorJoystick.getPreset1Button());

    waitTime(1);
    System.out.println("t: " + autoTimer.get() + " " + ChezyRobot.operatorJoystick.getPreset1Button());


    pinniped.wantFront = false;
    pinniped.wantRear = false;
    frontIntake.wantBumperGather = false;
    rearIntake.wantBumperGather = false;
  }

}

package com.team254.frc2014.auto;

import com.team254.frc2014.ChezyRobot;
import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.ConfigurationAutoMode;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.trajectory.Path;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Mani Gnanasivam
 * @author EJ Sebathia
 */
public class TestGooglyEyes extends ConfigurationAutoMode {

  public TestGooglyEyes() {
    super("TestGooglyEyes");
  }

  protected void routine() {
    bannerHotGoalDetector.startSampling();

    Timer t = new Timer();
    t.start();

    // Turn on wheel
    //shooterController.setVelocityGoal(4000);

    shooterController.setVelocityGoal(5300);
    // Grab balls from ground
    clapper.wantFront = false;
    clapper.wantRear = false;
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


    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.wantBumperGather = false;
    rearIntake.wantBumperGather = false;
  }

  public FieldPosition getFieldPosition() {
    return null;
  }
}

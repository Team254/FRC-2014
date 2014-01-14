package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;
import com.team254.frc2014.ChezyRobot;

public class ThreeBallAuto extends AutoMode {

  protected void routine() {
    driveAndCoast(5, 6);

    driveAtHeadingToX(35, 4.0, 15);

    System.out.println("nav: " + ChezyRobot.driveController.navigator.toString());

    dimeStop();

    driveAtHeadingToY(0, 15.0, 15);

    System.out.println("nav: " + ChezyRobot.driveController.navigator.toString());

    dimeStop();

  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.frc2014.auto;

import com.team254.frc2014.AutoMode;

/**
 *
 * @author spinkerton
 */
public class TestUltrasonicAuto extends AutoMode {

  protected void routine() {
    driveToUltrasonicRange(4);
    System.out.println("done ultrasonic");
  }

}

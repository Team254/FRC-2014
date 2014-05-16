package com.team254.frc2014;

import edu.wpi.first.wpilibj.Joystick;

/**
 * BorkedJoystick.java
 * 
 * One of our joysticks is borked. This is a hack.
 *
 * @author Tom Bottiglieri (tom@team254.com)
 */
public class BorkedJoystick extends Joystick {
  
  public BorkedJoystick(int port) {
    super(port);
  }
  
  public double getBorkedY() {
    double y = getY();
    if (y < -.95) {
      y = -1.0;
    }
    else if (y > 0.95) {
      y = 1.0;
    }
    return y;
  }

}

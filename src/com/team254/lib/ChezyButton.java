package com.team254.lib;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Watches a button and indicates if the button was just pressed or released.
 *
 * @author Mani Gnanasivam
 * @author Art Kalb
 */
public class ChezyButton {
  int buttonPort;
  private boolean wasPressed;
  private boolean wasReleased;
  private boolean isDown;
  private Joystick stick;
  
  public ChezyButton(Joystick stick, int buttonPort) {
    this.buttonPort = buttonPort;
    this.stick = stick;
  }
  
  public boolean wasPressed() {
    return wasPressed;
  }
  
  public boolean wasReleased() {
    return wasReleased;
  }

  public boolean get() {
    return isDown;
  }

  public void update() {
    boolean pressed = stick.getRawButton(buttonPort);
    wasPressed = pressed && !isDown;
    wasReleased = !pressed && isDown;
    isDown = pressed;
  }
  
}

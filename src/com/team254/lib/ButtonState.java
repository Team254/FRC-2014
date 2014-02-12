package com.team254.lib;

import com.team254.frc2014.ChezyRobot;

/**
 * Watches a button and indicates if the button was just pressed or released.
 *
 * @author Mani Gnanasivam
 * @author Art Kalb
 */
public class ButtonState {
  int buttonPort;
  private boolean wasPressed;
  private boolean wasReleased;
  private boolean isDown;
  
  public ButtonState(int buttonPort) {
    this.buttonPort = buttonPort;
  }
  
  public boolean wasPressed() {
    return wasPressed;
  }
  
  public boolean wasReleased() {
    return wasReleased;
  }
  public void update() {
    boolean pressed = ChezyRobot.operatorJoystick.getRawButton(buttonPort);
    wasPressed = pressed && !isDown;
    wasReleased = !pressed && isDown;
    isDown = pressed;
  }
  
}

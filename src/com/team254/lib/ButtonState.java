package com.team254.lib;

/**
 * Watches a button and indicates iff the button was just pressed or released.
 *
 * @author Mani Gnanasivam
 * @author Art Kalb
 */
public class ButtonState {
  private boolean wasPressed;
  private boolean wasReleased;
  private boolean isDown;
  
  public boolean wasPressed() {
    return wasPressed;
  }
  
  public boolean wasReleased() {
    return wasReleased;
  }
  public void update(boolean pressed) {
    wasPressed = pressed && !isDown;
    wasReleased = !pressed && isDown;
    isDown = pressed;
  }
  
}

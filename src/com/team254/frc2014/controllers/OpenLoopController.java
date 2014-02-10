package com.team254.frc2014.controllers;

import com.team254.lib.ControlOutput;
import com.team254.lib.Controller;

/**
 * OpenLoopController.java
 *
 * @author tombot
 */
public class OpenLoopController extends Controller {

  ControlOutput output;
  private double pwm;
  
  public void set(double pwm) {
    this.pwm = pwm;
  }
  
  public void update() {
    output.set(pwm);
  }

  public void reset() {
    this.pwm = 0;
  }
}

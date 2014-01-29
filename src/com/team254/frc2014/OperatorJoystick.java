package com.team254.frc2014;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Operator Joystick defines all of the buttons the operator uses on the control panel.
 * @author bg
 */
public class OperatorJoystick extends Joystick {
  
  public boolean getShooterState() {
    return this.getZ() < 0.0;
  }
  
  public boolean getPopperOnState() {
    return this.getRawButton(5);
  }
 
  // Intake 
  public boolean getIntakeButtonState() {
    return this.getRawButton(7);
  }
  public boolean getExhaustButtonState() {
    return this.getRawButton(8);
  }
  public boolean getIntakeUpSwitchState() {
    return getIntakePositionSwitch() == 1;
  }

  public boolean getIntakeDownSwitchState() {
    return getIntakePositionSwitch() == -1;
  }
  
  
  public boolean getAutoIntakeButtonState() {
    return this.getRawButton(1);
  }
  
  public int getIntakePositionSwitch() {
    boolean axis = (this.getRawAxis(4) < 0.1);
    boolean button = this.getRawButton(Constants.intakeDownSwitchPort.getInt());
    if (axis) {
      return 1; // up
    }
    if (button) {
      return -1; // down
    }
    return 0; // floating
  }

  public OperatorJoystick(int port) {
    super(port);
  }
}

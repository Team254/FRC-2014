package com.team254.frc2014;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author bg
 */
public class OperatorJoystick extends Joystick {
  
  public boolean getShooterState() {
    return this.getRawAxis(2) < 0.0;
  }
  
  public boolean getPopperOnState() {
    return this.getRawButton(2);
  }
 
  // Intake 
  public boolean getIntakeState() {
    return this.getRawButton(8);
  }
  public boolean getExhaustState() {
    return this.getRawButton(7);
  }
  public boolean getIntakeSolenoidOffStat() {
    return this.getRawButton(9);
  }
  public boolean getIntakeSolenoidOnState() {
    return this.getRawButton(10);
  }
  public OperatorJoystick(int port) {
    super(port);
  }
}

package com.team254.frc2014;

import com.team254.lib.ChezyButton;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Operator Joystick defines all of the buttons the operator uses on the control
 * panel.
 *
 * @author bg
 */
public class OperatorJoystick extends Joystick {

  public OperatorJoystick(int port) {
    super(port);
  }

  public boolean getAutoCatchButton() {
    return getRawButton(1);
  }

  public boolean getInboundButton() {
    return getRawButton(2);
  }
  
  public ChezyButton inboundButton = new ChezyButton(this, 2);

  public boolean getShooterOffButton() {
    return getRawButton(3);
  }

  public boolean getPreset1Button() {
    return getRawButton(4);
  }

  public boolean getPreset2Button() {
    return getRawButton(5);
  }

  public boolean getPreset3Button() {
    return getRawButton(6);
  }

  public boolean getExhaustButton() {
    return getRawButton(9);
  }

  public boolean getIntakeButton() {
    return getRawButton(10);
  }

  public boolean getPassRearButton() {
    return getRawButton(7);
  }

  public boolean getPassFrontButton() {
    return getRawButton(8);
  }

  public boolean getAutoIntakeFrontButton() {
    return getRawButton(11);
  }

  public boolean getAutoIntakeRearButton() {
    return getRawButton(12);
  }

  public boolean getAutoIntakeOff() {
    return getRawAxis(4) < 0.1;
  }

  public boolean getPreset4Button() {
    return getZ() < 0;
  }

  public boolean getPreset5Button() {
    return getY() < 0;
  }

  public boolean getPreset6Button() {
    return getX() < 0;
  }
}

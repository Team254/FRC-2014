
package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.lib.Subsystem;
import com.team254.lib.util.Util;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import java.util.Hashtable;

/**
 *
 * @author bg
 */
public class Shooter extends Subsystem {

  private Solenoid popper = new Solenoid(Constants.popperSolenoidPort.getInt());
  private Talon shooterWheel = new Talon(Constants.shooterWheelPort.getInt());

  public void setShooter(double power) {
    shooterWheel.set(Util.limit(power, 1.0));
  }

  public void setPopper(boolean on) {
    popper.set(on);
  }
  public Shooter() {
    super("Shooter");
  }

  public Hashtable serialize() {
    return new Hashtable();
  }
}

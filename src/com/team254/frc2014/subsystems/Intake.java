
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
public class Intake extends Subsystem {
  private Talon intakeRoller = new Talon(Constants.intakeRollerPort.getInt());
  private Solenoid intakeSolenoid = new Solenoid(Constants.intakeSolenoidPort.getInt());
  
  public void setIntakeRoller(double power) {
    power = Util.limit(power, 1.0);
    intakeRoller.set(power);
  }
  public void setSolenoid(boolean state) {
    intakeSolenoid.set(state);
  }
  
  public boolean getSolenoidState() {
    return intakeSolenoid.get();
  }
  public double getIntakeRollerSpeed() {
    return intakeRoller.get();
  }
  
  public Intake() {
    super("Intake");
  }
  public Hashtable serialize() {
    return new Hashtable();
  }

}

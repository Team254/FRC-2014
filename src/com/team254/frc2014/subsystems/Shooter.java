package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.frc2014.ShooterGains;
import com.team254.frc2014.controllers.FlywheelController;
import com.team254.lib.ControlOutput;
import com.team254.lib.ControlSource;
import com.team254.lib.Loopable;
import com.team254.lib.Subsystem;
import com.team254.lib.util.Util;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import java.util.Hashtable;


/**
 *
 * @author bg
 */
public class Shooter extends Subsystem implements Loopable, ControlOutput, ControlSource {
  private Solenoid popper = new Solenoid(Constants.popperSolenoidPort.getInt());
  private Talon shooterA = new Talon(Constants.shooterWheelPort.getInt());
  private Talon shooterB = new Talon(Constants.shooterWheelPortB.getInt());
  FlywheelController controller = new FlywheelController("ShooterController", this, this, ShooterGains.getGains()[0]);

  public void setShooter(double power) {
    power = Util.limit(power, 1.0);
    shooterA.set(power);
    shooterB.set(-power);
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

  public void update() {
    controller.update();
  }
  
  public void setVelocityGoal(double v) {
    controller.setVelocityGoal(v);
  }

  public void set(double value) {
  }

  public double get() {
    return 0;
  }

  public void updateFilter() {
  }

  public boolean getLowerLimit() {
    return false;
  }

  public boolean getUpperLimit() {
    return false;
  }
}

package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.frc2014.ShooterGains;
import com.team254.frc2014.controllers.FlywheelController;
import com.team254.lib.ControlOutput;
import com.team254.lib.ControlSource;
import com.team254.lib.Loopable;
import com.team254.lib.Subsystem;
import com.team254.lib.util.ThrottledPrinter;
import com.team254.lib.util.Util;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;

/**
 * This represents the electrical components of the shooter.
 * @author bg
 */
public class Shooter extends Subsystem implements Loopable, ControlOutput, ControlSource {

  private Solenoid popper = new Solenoid(Constants.popperSolenoidPort.getInt());
  private Talon shooterA = new Talon(Constants.leftShooterWheelPort.getInt());
  private Talon shooterB = new Talon(Constants.rightShooterWheelPort.getInt());
  FlywheelController controller = new FlywheelController("ShooterController", this, this, ShooterGains.getGains()[0]);
  public Counter counter = new Counter(Constants.shootEncoderPort.getInt());
  private ThrottledPrinter p = new ThrottledPrinter(.1);

  private void setShooter(double power) {
    power = Util.limit(power, 1.0);
    shooterA.set(power);
    shooterB.set(-power);
  }

  public void setPopper(boolean on) {
    popper.set(on);
  }

  public Shooter() {
    super("Shooter");
    controller.enable();
    counter.start();
  }

  public Hashtable serialize() {
    return new Hashtable();
  }

  public void update() {
    controller.update();
  }
  double rpmGoal = 0;
  public void setVelocityGoal(double v) {
    rpmGoal = v;
    controller.setVelocityGoal((v * Math.PI * 2.0) / 60.0);
  }

  public double getRpmGoal() {
    return rpmGoal;
  }
  public void set(double value) {
    //setShooter(rpmGoal > 0 ? 1.0 : 0.0);
    setShooter(value);
  }
  public double lastRpm = 0;

  public double get() {
    int kCountsPerRev = 1;
    double period = counter.getPeriod();
    double rpm = 60.0 / (period * (double) kCountsPerRev);
    if (rpm < 20000) {
      lastRpm = rpm;
    }
    double ret = (lastRpm * Math.PI * 2.0) / 60.0;
    //System.out.println("" + Timer.getFPGATimestamp() + ", " + shooterA.get() + ", " + lastRpm + ", 0.01");
    return ret;
  }

  public void updateFilter() {
  }

  public boolean getLowerLimit() {
    return false;
  }

  public boolean getUpperLimit() {
    return false;
  }
  
  public double getLastRpm() {
    return lastRpm;
  }
}

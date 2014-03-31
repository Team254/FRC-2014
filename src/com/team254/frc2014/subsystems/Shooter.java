package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.frc2014.controllers.RpmFlywheelController;
import com.team254.lib.ControlOutput;
import com.team254.lib.ControlSource;
import com.team254.lib.Loopable;
import com.team254.lib.Subsystem;
import com.team254.lib.TimedBoolean;
import com.team254.lib.util.ThrottledPrinter;
import com.team254.lib.util.Util;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Hashtable;

/**
 * This represents the electrical components of the shooter.
 * @author bg
 */
public class Shooter extends Subsystem implements Loopable, ControlOutput, ControlSource {

  private Solenoid hood = new Solenoid(Constants.hoodSolenoidPort.getInt());
  private Solenoid catcher = new Solenoid(Constants.catcherSolenoidPort.getInt());
  private Talon shooterA = new Talon(Constants.leftShooterWheelPort.getInt());
  private Talon shooterB = new Talon(Constants.rightShooterWheelPort.getInt());
  public Counter counter = new Counter(Constants.shooterReflectorPort.getInt());
  public DigitalOutput shooterLed = new DigitalOutput(Constants.shooterLed.getInt());
  public Relay shooterLedRelay = new Relay(Constants.shooterLedRelay.getInt());
  private TimedBoolean doBrakeReverse = new TimedBoolean(1.35);
  private TimedBoolean doBrakeForward = new TimedBoolean(1.0);
  

  // desired action flags
  public boolean wantCatcherOpen = false;
  private ThrottledPrinter p = new ThrottledPrinter(.1);
  
  private void setShooterPower(double power) {
    
    double goal = ((RpmFlywheelController)controller).getVelocityGoal();
    boolean reverse = goal < 0;
    power = Util.limit(power, 1.0);
    if (reverse) {
      power *= -1.0;
    }
    if (doBrakeForward.get()) {
      power = 0.085;
    } else if (doBrakeReverse.get()) {
      power = -.085;
    }
    if (!controller.enabled()) {
      power = 0;
    }
    SmartDashboard.putNumber("ShooterPWM", power);
    shooterA.set(-power);
    shooterB.set(power);
  }

  public Shooter() {
    super("Shooter");
    if (controller != null) {
      controller.enable();
    }
    counter.start();
  }

  public Hashtable serialize() {
    return new Hashtable();
  }

  public void set(double value) {
    setShooterPower(value);
  }
  
  public void setHood(boolean up) {
    hood.set(up);
  }
  
  public double lastRpm = 0;
  public double penultimateRpm =  0;
  
  
  public void setCatcher(boolean open) {
    catcher.set(open);
  }

  public double get() {
    double ret = (lastRpm * Math.PI * 2.0) / 60.0;
    return ret;
  }
  
  private double lastGoal = 0;
  public void update() {
    // Update the controller
    updateSensedVelocity();
    super.update();
    catcher.set(wantCatcherOpen);
    if (controller != null) {
      double goal = ((RpmFlywheelController)controller).getVelocityGoal();
      if (goal < 0 && lastGoal >= 0 &&  Math.abs(lastRpm) > 500.0) {
        System.out.println("Triggering Reverse Brake");
        doBrakeReverse.trigger();
      }
      if (goal >= 0 && lastGoal < 0 &&  Math.abs(lastRpm) > 500.0) {
        System.out.println("Triggering Forward Brake");
        doBrakeForward.trigger();
      }
      lastGoal = goal;
    }
    //System.out.println(Timer.getFPGATimestamp() + ", " + Math.abs(shooterA.get()) + ", " + lastRpm + ", 0.01");
  }

  public double updateSensedVelocity() {
    int kCountsPerRev = 1;
    double period = counter.getPeriod();
    double rpm = 60.0 / (period * (double) kCountsPerRev);
    if (rpm < 20000) {
      lastRpm = rpm;
    }
    double ret = (lastRpm * Math.PI * 2.0) / 60.0;
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

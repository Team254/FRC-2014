package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.lib.ControlOutput;
import com.team254.lib.ControlSource;
import com.team254.lib.Loopable;
import com.team254.lib.Subsystem;
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
  
  
  // States
  public final int STATE_CLOSED = 0;
  public final int STATE_WAIT_FOR_RPM_DROP = 1;
  public final int STATE_WAIT_FOR_CATCH = 2;
  public final int STATE_CATCH_OWN_SHOT = 3;
  public final int STATE_CATCH_OTHER_SHOT = 4;
  public final int STATE_WHEEL_SETTLE = 5;
  
  // desired action flags
  public boolean wantCatch = false;
  public boolean wantShotCatch = false;
  
  private boolean firstStateRun;
  private int state = STATE_CLOSED;
  private Timer stateTimer = new Timer();
          
  private ThrottledPrinter p = new ThrottledPrinter(.1);
  
  private void setShooterPower(double power) {
    power = Util.limit(power, 1.0);
    shooterA.set(-power);
    shooterB.set(power);
  }

  public Shooter() {
    super("Shooter");
    if (controller != null) {
      controller.enable();
    }
    counter.start();
    stateTimer.start();
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
  public void setCatcher(boolean down) {
    catcher.set(down);
  }
  public double get() {
    double ret = (lastRpm * Math.PI * 2.0) / 60.0;
    return ret;
  }
  
  public void update() {
    // Update the controller
    updateSensedVelocity();
    SmartDashboard.putNumber("ShooterRPM", lastRpm);
    SmartDashboard.putNumber("BatteryV", DriverStation.getInstance().getBatteryVoltage());
    super.update();
    int nextState = state;
    
    switch (state) {
      case STATE_CLOSED:
        catcher.set(false);
        if (wantShotCatch) {
          nextState = STATE_WAIT_FOR_RPM_DROP;
        } else if (wantCatch) {
          nextState = STATE_CATCH_OTHER_SHOT;
        }
        break;
      case STATE_WAIT_FOR_RPM_DROP:
        
        if (firstStateRun) {
          penultimateRpm = lastRpm;
        }
        if(lastRpm < penultimateRpm - 1000.0) {
           nextState = STATE_WAIT_FOR_CATCH;
        }
        catcher.set(false);
        break;
      case STATE_WAIT_FOR_CATCH:
        if (stateTimer.get() > 0.15) {
          nextState = STATE_CATCH_OWN_SHOT;
        }
        catcher.set(false);
        controller.setReverse(false);
        break;
      case STATE_CATCH_OWN_SHOT:
        // reverse flywheel and open catcher
        catcher.set(stateTimer.get() > .45);
        if (!wantShotCatch) {
          nextState = STATE_CLOSED;
        }
        break;
        
      case STATE_CATCH_OTHER_SHOT:
        // reverse flywheel and open catcher
        catcher.set(stateTimer.get() > .15);
        if (!wantCatch) {
          nextState = STATE_CLOSED;
        }
        break;
        
      case STATE_WHEEL_SETTLE:
        controller.disable();
        catcher.set(false);
        if (stateTimer.get() > 2.0) {
          controller.enable();
          nextState = STATE_CLOSED;
        }
      default:
        break;
    }
    firstStateRun = false;
    if (nextState != state) {
      state = nextState;
      stateTimer.reset();
      firstStateRun = true;
    }
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

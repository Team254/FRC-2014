package com.team254.frc2014.subsystems;

import com.team254.frc2014.ChezyRobot;
import com.team254.frc2014.Constants;
import com.team254.lib.Loopable;
import com.team254.lib.Subsystem;
import com.team254.lib.util.Util;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Hashtable;

/**
 *
 * @author bg
 */
public class Intake extends Subsystem implements Loopable {


  private Talon roller;
  private Solenoid actuator;
  private AnalogChannel bumpSwitch;
  boolean flip = false;
  private static final int kBumpSwitchThreshold = 400;
  private double deliverTime = 1.0;

  public Intake(String name, Talon roller, AnalogChannel bumpSwitch, Solenoid actuator, boolean flip, double deliverTime) {
    super(name);
    this.roller = roller;
    this.bumpSwitch = bumpSwitch;
    this.actuator = actuator;
    this.flip = flip;
    this.deliverTime = deliverTime;
    stateTimer.start();
  }

  double manualRollerPower = 0;
  public static final int STATE_HOLD_POSITION = 1;
  public static final int STATE_GATHER_BALL = 2;
  public static final int STATE_DELIVER_BALL = 3;
  public static final int STATE_MANUAL = 4;
  public static final int STATE_GATHER_EXTRA_BALL = 5;
  public static final int STATE_LETGO_BEFORE_SHOOT = 6;
  public static final int STATE_SHOOTING = 7;
  public static final int STATE_GATHER_LETOFF = 8;

  private int state = STATE_MANUAL;
  public boolean wantGather = false;
  public boolean wantBumperGather = false;
  public boolean wantDown = false;
  public boolean wantShoot = false;

  private void setRollerPower(double power) {
    power = Util.limit(power * (flip ? -1.0 : 1.0), 1.0);
    roller.set(power);
  }

  public void setManualRollerPower(double power) {
    manualRollerPower = power;
  }
  boolean wantAutoIntake = false;

  public void setAutoIntake(boolean autoIntake) {
    wantAutoIntake = autoIntake;
  }

  private void setPositionDown(boolean state) {
    actuator.set(state);
  }
 
 
  public boolean getSolenoidState() {
    return actuator.get();
  }

  public double getIntakeRollerSpeed() {
    return roller.get();
  }

  public boolean getIntakeSensor() {
    return (bumpSwitch.getValue() < kBumpSwitchThreshold);
  }

  public Hashtable serialize() {
    return new Hashtable();
  }
  double rollerGoal = 0;
  Timer stateTimer = new Timer();
  Timer rolloutTimer = new Timer();
  private boolean retryExtra = false;

  int i = 0;
  public void update() {
    int newState = state;
    switch (state) {
      case STATE_MANUAL:
        setPositionDown(wantDown);
        setRollerPower(manualRollerPower); 
        if (wantGather) {
          newState = STATE_GATHER_BALL;
        } else if (wantBumperGather) {
          newState = STATE_GATHER_EXTRA_BALL;
        } else if (wantShoot) {
          newState = STATE_SHOOTING;
        }
        break;
      case STATE_HOLD_POSITION:
        
        if (!getIntakeSensor()) {
          setRollerPower(.75);
        } else {
          setRollerPower(0);
        }
        if (wantShoot) {
          newState = STATE_LETGO_BEFORE_SHOOT;
        }
        
        if (!wantBumperGather) {
          newState = STATE_MANUAL;
        }
        break;
      case STATE_GATHER_BALL:

        setPositionDown(true);
        setRollerPower(1);
        if (getIntakeSensor() || !wantGather) {
          newState = STATE_DELIVER_BALL;
        }
        break;
      case STATE_DELIVER_BALL:

        if (stateTimer.get() < deliverTime) {
          setPositionDown(false);
          setRollerPower(1);
        } else if (wantGather) {
          setPositionDown(false);
          setRollerPower(0);
        } else {
          setPositionDown(false);
          setRollerPower(0);
          newState = STATE_MANUAL;
        }
        break;
      case STATE_GATHER_EXTRA_BALL:
        setPositionDown(true);
        setRollerPower(.75);
        if (getIntakeSensor() || stateTimer.get() > .5) {
          newState = STATE_GATHER_LETOFF;
        } else if (retryExtra && stateTimer.get() < .5) {
          ;
        } else if (!wantBumperGather) {
          setPositionDown(false);
          newState = STATE_MANUAL;
        }
        break;
        
      case STATE_GATHER_LETOFF:
        setRollerPower(-.05);
        if (stateTimer.get() > .2) {
          setRollerPower(0);
          newState = STATE_HOLD_POSITION;
        } else if (stateTimer.get() > .1) {
          setRollerPower(0);
        }
        break;
      case STATE_LETGO_BEFORE_SHOOT:
        setRollerPower(-.2);
        if (stateTimer.get() > .18) {
          setRollerPower(0);
          rollerGoal = 0;
          if (!wantShoot || !wantBumperGather) {
            newState = STATE_HOLD_POSITION;
          }
        }
        break;
        
      case STATE_SHOOTING:
        setRollerPower(Constants.rearRollerShootPower.getDouble());
        SmartDashboard.putNumber("rpmShot", ChezyRobot.shooter.lastRpm);
        if (!wantShoot && stateTimer.get() > 1.0) {
          setRollerPower(0);
          newState = STATE_MANUAL;
        }
        break;
    }

    if (newState != state) {
      state = newState;
      stateTimer.reset();
    }
  }
}

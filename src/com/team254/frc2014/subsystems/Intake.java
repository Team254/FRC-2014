
package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.lib.Controller;
import com.team254.lib.Loopable;
import com.team254.lib.Subsystem;
import com.team254.lib.util.Debouncer;
import com.team254.lib.util.Util;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;

/**
 *
 * @author bg
 */
public class Intake extends Subsystem implements Loopable {
  private Talon intakeRoller = new Talon(Constants.intakeRollerPort.getInt());
  private Solenoid intakeSolenoid = new Solenoid(Constants.intakeSolenoidPort.getInt());
  private DigitalInput intakeSwitch = new DigitalInput(Constants.intakeBumperSwitchPort.getInt());
  
  public Encoder encoder = new Encoder(Constants.frontIntakeEncoderPortA.getInt(), Constants.frontIntakeEncoderPortB.getInt());

  private void setRollerPower(double power) {
    power = Util.limit(power, 1.0);
    intakeRoller.set(power);
  }
  
  double manualRollerPower = 0;
  public static final int STATE_IDLE = 0;
  public static final int STATE_HOLD_POSITION = 1;
  public static final int STATE_GATHER_BALL = 2;
  public static final int STATE_DELIVER_BALL = 3;
  public static final int STATE_MANUAL = 4;
  public static final int STATE_GATHER_EXTRA_BALL = 5;
  public static final int STATE_GATHER_EXTRA_BALL_BACKOFF = 6;
  private int state = STATE_IDLE;
  public boolean wantGather = false;
  public boolean wantManual = false;
  public boolean wantExtraGather = false;

  public void setManualRollerPower(double power) {
    manualRollerPower = power;
    wantManual = true;
    state = STATE_MANUAL;
    stateTimer.reset();
  }

  boolean wantAutoIntake = false;
  
  public void setAutoIntake(boolean autoIntake) {
    wantAutoIntake = autoIntake;
  }
  public void setPositionDown(boolean state) {
    intakeSolenoid.set(state);
  }
  
  public double getIntakePosition() {
    return encoder.get() / 32.0;
  }
  public boolean getSolenoidState() {
    return intakeSolenoid.get();
  }
  public double getIntakeRollerSpeed() {
    return intakeRoller.get();
  }
  
  public boolean getIntakeSensor() {
    return !intakeSwitch.get(); 
  }
  public Intake() {
    super("Intake");
    encoder.start();
    stateTimer.start();
  }
  public Hashtable serialize() {
    return new Hashtable();
  }
  
  
  double rollerGoal = 0;
  
  Timer stateTimer = new Timer();
  Timer rolloutTimer = new Timer();

  private boolean retryExtra = false;
  public void update() {
    int newState = state;
    switch (state) {
      case STATE_MANUAL:
        setRollerPower(manualRollerPower);
        if (!wantManual) {
          newState = STATE_IDLE;
        }
        break;
      case STATE_HOLD_POSITION:
        retryExtra = false;
        double error = getIntakePosition() - rollerGoal;
        double motor = error * -1.2;
        setRollerPower(motor);
        if (!getIntakeSensor() && stateTimer.get() > .5) {
          //retryExtra = true;
          //newState = STATE_GATHER_EXTRA_BALL;
        }
        break;
      case STATE_GATHER_BALL:
        if (!wantGather) {
          newState = STATE_IDLE;
        }
        setPositionDown(true);
        setRollerPower(1);
        if (getIntakeSensor()) {
          encoder.reset();
          newState = STATE_DELIVER_BALL;
        }
        break;
      case STATE_DELIVER_BALL:

        if (stateTimer.get() < .7) {
          setPositionDown(false);
          setRollerPower(1);
        } else if (wantGather) {
          setPositionDown(false);
          setRollerPower(0);
        } else {
          setPositionDown(false);
          setRollerPower(0);
          newState = STATE_IDLE;
        }
        break;
      case STATE_GATHER_EXTRA_BALL:
        setPositionDown(true);
        setRollerPower(.85);
        if (getIntakeSensor()) {
          newState = STATE_GATHER_EXTRA_BALL_BACKOFF;
        } else if (retryExtra && stateTimer.get() < .5) {
           ;
        } else if (!wantExtraGather) {
          setPositionDown(false);
          newState = STATE_IDLE;
        }
        break;
      case STATE_GATHER_EXTRA_BALL_BACKOFF:
        setRollerPower(-.45);
        if(stateTimer.get() > .25) {
          setRollerPower(0);
          encoder.reset();
          rollerGoal = 0;
          newState = STATE_HOLD_POSITION;
        }
        break;
      case STATE_IDLE:
        setRollerPower(0);
        if (wantGather) {
          newState = STATE_GATHER_BALL;
        } else if (wantExtraGather) {
          newState = STATE_GATHER_EXTRA_BALL;
        }
        break;
    }
    
    if (newState != state) {
      state = newState;
      stateTimer.reset();
    }
  }


}

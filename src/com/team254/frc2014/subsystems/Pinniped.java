package com.team254.frc2014.subsystems;

import com.team254.frc2014.ChezyRobot;
import com.team254.frc2014.Constants;
import com.team254.lib.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
/**
 *
 * @author Mani Gnanasivam
 */
public class Pinniped extends Subsystem{
  public static Solenoid frontSolenoid = new Solenoid(Constants.frontClapperSolenoidPort.getInt());
  public static Solenoid rearSolenoid = new Solenoid(Constants.rearClapperSolenoidPort.getInt());
  public static boolean frontIsUp;
  public static boolean rearIsUp;
  public boolean wantShot;
  public boolean wantFront;
  public boolean wantRear;
  public boolean wantTimedShot;
  
  public void setControlLoopsOn() {
    waitForWheel = true;
  }
  
  public void setControlLoopsOff() {
    waitForWheel = false;
  }
  
  private boolean waitForWheel = false;
  public boolean doingRunning = false;
  private final int STATE_DOWN = 0;
  private final int STATE_UP = 1;
  private final int STATE_FRONT_UP = 2;
  private final int STATE_REAR_UP = 3;
  public final int STATE_START_SHOT = 4;
  public final int STATE_TIMED_SHOT_DOWN = 5;
  private int state = STATE_DOWN;
  private Timer stateTimer = new Timer();
  
  public Hashtable serialize() {
    return new Hashtable();
  }
  
  public Pinniped() {
    super("Pinniped");
    stateTimer.start();
  }
  
  public static void clap(){
    setStates(true,true);
  }
  
  public static void antiClap(){
    setStates(false,false);
  }
  
  public void startShot() {
    this.state = STATE_START_SHOT;
  }
  
  public static void setStates(boolean frontUp,boolean rearUp){
    frontSolenoid.set(frontUp);
    rearSolenoid.set(rearUp);
    frontIsUp = frontUp;
    rearIsUp = rearUp;
  }
  
  // This is called at 100Hz and handles the state machine for the ball
  // loading/shooting mechanism.
  public void update() {
    int nextState = state;
    switch(state) {
      case STATE_DOWN:
        rearSolenoid.set(false);
        frontSolenoid.set(false);
        if(wantShot && (doingRunning || !waitForWheel || ChezyRobot.shooterController.onTarget())) {
          nextState = STATE_START_SHOT;
        } else if(wantFront) {
          nextState = STATE_FRONT_UP;
        } else if(wantRear) {
          nextState = STATE_REAR_UP;
        }
        break;
        
       
      case STATE_UP:
        rearSolenoid.set(true);
        frontSolenoid.set(true);
        if (wantShot && wantTimedShot && stateTimer.get() > .5) {
          nextState = STATE_TIMED_SHOT_DOWN;
        } else if(!wantShot && !wantFront && !wantRear && stateTimer.get() > .5) {
          nextState = STATE_DOWN;
        } else if(!wantFront && wantRear) {
          nextState = STATE_REAR_UP;
        } else if(!wantRear && wantFront) {
          nextState = STATE_FRONT_UP;
        } 
        break;
      case STATE_START_SHOT:
        rearSolenoid.set(false);
        frontSolenoid.set(true);
        // Fire the front pinniped first so the ball is forced in to the
        // back of the hood, avoiding pinball action with the flywheel
        if(stateTimer.get() > .06) {
          nextState = STATE_UP;
        }
        break;
      case STATE_FRONT_UP:
        rearSolenoid.set(false);
        frontSolenoid.set(true);
        if(wantFront && wantRear) {
          nextState = STATE_UP; 
        }
         else if(!wantFront) {
          nextState = STATE_DOWN;
        }
        break;
        
      case STATE_REAR_UP:
        rearSolenoid.set(true);
        frontSolenoid.set(false);
        if(wantFront && wantRear) {
          nextState = STATE_UP; 
        } else if(!wantRear) {
          nextState = STATE_DOWN;
        } 
        break;
       
      case STATE_TIMED_SHOT_DOWN:
        rearSolenoid.set(false);
        frontSolenoid.set(false);
        if (!wantShot) {
          nextState = STATE_DOWN;
        }
        break;

      default:
        nextState = STATE_DOWN;
        break;       
    }
    if(nextState != state) {
      state = nextState;
      stateTimer.reset();
    }
  }
}

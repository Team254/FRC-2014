
package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.lib.Controller;
import com.team254.lib.Subsystem;
import com.team254.lib.util.Debouncer;
import com.team254.lib.util.Util;
import edu.wpi.first.wpilibj.DigitalInput;
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
  private DigitalInput intakeSwitch = new DigitalInput(Constants.intakeBumperSwitchPort.getInt());
  public IntakeController controller = new IntakeController(this);
  
  public void setIntakeRoller(double power) {
    power = Util.limit(power, 1.0);
    intakeRoller.set(power);
  }
  
  public void setAutoIntake(boolean autoIntake) {
    if(autoIntake) {
      controller.enable();
    }
    else {
      controller.disable();
      setIntakeRoller(0);
    }
    controller.autoIntake = autoIntake;
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
  
  public boolean getIntakeSensor() {
    return !intakeSwitch.get(); 
  }
  public Intake() {
    super("Intake");
  }
  public Hashtable serialize() {
    return new Hashtable();
  }
  
  public class IntakeController extends Controller {
    Intake i;
    public boolean autoIntake = false;
    public Debouncer debouncer = new Debouncer(.25);
    public void update() {
      boolean stopSucking = i.getIntakeSensor();
    //  System.out.println(i.getIntakeSensor());
      // Intaking Swagger
      if(autoIntake){
        if(stopSucking) {
          i.setIntakeRoller(.15);
        } else {
          i.setIntakeRoller(1 );
        }
      }
    }
    public IntakeController (Intake i) {
      this.i = i;
    }  
  }

}

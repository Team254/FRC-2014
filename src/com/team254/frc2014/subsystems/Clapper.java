package com.team254.frc2014.subsystems;

import com.team254.lib.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
import java.util.Hashtable;
/**
 *
 * @author Mani Gnanasivam
 */
public class Clapper extends Subsystem{
  public static Solenoid frontSolenoid;
  public static Solenoid rearSolenoid;
  public static boolean frontIsUp;
  public static boolean rearIsUp;
  
  public Hashtable serialize() {
    return new Hashtable();
  }
  
  public Clapper() {
    super("Pinniped");
  }
  
  public static void clap(){
    setStates(true,true);
  }
  
  public static void antiClap(){
    setStates(false,false);
  }
  
  public static void setStates(boolean frontUp,boolean rearUp){
    frontSolenoid.set(frontUp);
    rearSolenoid.set(rearUp);
    frontIsUp = frontUp;
    rearIsUp = rearUp;
  }
}

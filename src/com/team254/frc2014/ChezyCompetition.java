package com.team254.frc2014;
/**
 * This is where the magic happens!
 *
 */
import com.team254.frc2014.auto.TestUltrasonicAuto;
import com.team254.frc2014.auto.ThreeBallAuto;
import com.team254.lib.ChezyIterativeRobot;
import com.team254.lib.Server;
import com.team254.lib.util.ThrottledPrinter;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;

public class ChezyCompetition extends ChezyIterativeRobot {

  AutoMode currentAutoMode = new ThreeBallAuto();
  Server s = new Server();
  Thread t;
  ThrottledPrinter p = new ThrottledPrinter(1);
  DriverStationLCD lcd;

  public void robotInit() {
    t = new Thread(s);
    t.start();
    lcd = DriverStationLCD.getInstance();
    ChezyRobot.initRobot();
    ChezyRobot.controlUpdater.start();
  }

  public void autonomousInit() {
    ChezyRobot.drivebase.resetGyro();
    ChezyRobot.driveController.navigator.resetWithReferenceHeading(ChezyRobot.drivebase.getGyroAngle());
    ChezyRobot.driveController.enable();
    System.out.println(currentAutoMode.getClass().toString());
    currentAutoMode.start();
  }

  public void disabledInit() {
    ChezyRobot.driveController.disable();
    currentAutoMode.stop();
  }

  public void teleopInit() {
    currentAutoMode.stop();
    ChezyRobot.driveController.disable();
    // This is just here for testing purposes
  }

  public void teleopPeriodic() {
    double z = ChezyRobot.rightStick.getZ();
    double x = ChezyRobot.rightStick.getX();
    printJoystickValues();
    boolean qt = false;
    double turn = x;
    if (Math.abs(z) > .2 && Math.abs(x) < .4) {
      qt = true;
      turn = z;
    }
    
    // Shooter
    if(ChezyRobot.operatorJoystick.getShooterState()) {
      ChezyRobot.shooter.setShooter(1);
    } else {
      ChezyRobot.shooter.setShooter(0);
    }
    
    if(ChezyRobot.operatorJoystick.getPopperOnState()) {
      ChezyRobot.shooter.setPopper(true);
    } else {
      ChezyRobot.shooter.setPopper(false);
    }
    
    // Intake Roller
    if(ChezyRobot.operatorJoystick.getIntakeButtonState()) {
      ChezyRobot.intake.setIntakeRoller(1);
    } else if (ChezyRobot.operatorJoystick.getExhaustButtonState())
    {
      ChezyRobot.intake.setIntakeRoller(-1);
    } else {
      ChezyRobot.intake.setIntakeRoller(0);
    } 
    
    //Intake solenoid
    if(ChezyRobot.operatorJoystick.getIntakeDownSwitchState()) {
      ChezyRobot.intake.setSolenoid(true);
    } else if(ChezyRobot.operatorJoystick.getIntakeUpSwitchState()) {
      ChezyRobot.intake.setSolenoid(false);
    }
    
    ChezyRobot.cdh.cheesyDrive(-ChezyRobot.leftStick.getY(), turn , qt, true); //ChezyRobot.rightStick.getRawButton(2), true);
  }

  public void disabledPeriodic() {
    // Print the Ultrasonic value - hardware might be broken
    printJoystickValues();
  }
  public void printJoystickValues() {
    lcd.println(DriverStationLCD.Line.kUser2, 1,
             " r X:  " + Math.floor(ChezyRobot.rightStick.getX() * 100) / 100
             + " Y: " + Math.floor(ChezyRobot.rightStick.getY() * 100) / 100
            + " Z: " + Math.floor(ChezyRobot.rightStick.getZ() * 10) / 10);
    lcd.println(DriverStationLCD.Line.kUser1, 1,
             " l X:  " + Math.floor(ChezyRobot.leftStick.getX() * 100) / 100
             + " Y: " + Math.floor(ChezyRobot.leftStick.getY() * 100) / 100
            + " Z: " + Math.floor(ChezyRobot.leftStick.getZ() * 10) / 10);
    lcd.updateLCD();
  }
}
package com.team254.frc2014;

/**
 * This is where the magic happens!
 *
 */
import com.team254.frc2014.auto.TestDriveAuto;
import com.team254.frc2014.auto.TestThreeBallShootAuto;
import com.team254.frc2014.auto.TestUltrasonicAuto;
import com.team254.frc2014.auto.ThreeBallAuto;
import com.team254.lib.ChezyIterativeRobot;
import com.team254.lib.Server;
import com.team254.lib.util.Latch;
import com.team254.lib.util.ThrottledPrinter;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Timer;

public class ChezyCompetition extends ChezyIterativeRobot {
  AutoMode currentAutoMode = null;
  AutoModeSelector selector = new AutoModeSelector();
  Server s = new Server();
  Thread t;
  ThrottledPrinter p = new ThrottledPrinter(.5);
  DriverStationLCD lcd;
  
  public void initAutoModes() {
    selector.addAutoMode(new TestThreeBallShootAuto());
    selector.addAutoMode(new TestDriveAuto());
    selector.addAutoMode(new ThreeBallAuto());
    selector.addAutoMode(new TestUltrasonicAuto());

  }

  public void robotInit() {
    initAutoModes();
    t = new Thread(s);
    t.start();
    lcd = DriverStationLCD.getInstance();
    ChezyRobot.initRobot();
    ChezyRobot.subsystemUpdater100Hz.start();
    ChezyRobot.drivebase.gyro.startCalibrateThread();
    lcdUpdateTimer.start();
  }

  public void autonomousInit() {
    ChezyRobot.drivebase.gyro.stopCalibrating();
    ChezyRobot.drivebase.resetGyro();
    currentAutoMode = selector.currentAutoMode();
    if (currentAutoMode != null) {
      currentAutoMode.start();
    }
  }

  public void disabledInit() {
    Constants.readConstantsFromFile();
    if (currentAutoMode != null) {
      currentAutoMode.stop();
    }
    ChezyRobot.drivebase.turnOffControllers();
    ChezyRobot.drivebase.resetGyro();
    ChezyRobot.drivebase.gyro.startCalibrateThread();
  }

  public void teleopInit() {
    ChezyRobot.drivebase.gyro.stopCalibrating();
    if (currentAutoMode != null) {
      currentAutoMode.stop();
    }
    ChezyRobot.drivebase.turnOffControllers();
    ChezyRobot.frontIntake.setAutoIntake(false);
    // This is just here for testing purposes
  }
  double wantedRpm = 4000;
  Latch upLatch = new Latch();
  Latch downLatch = new Latch();
  Latch autoSelectLatch = new Latch();
  Latch laneSelectLatch = new Latch();

  public void teleopPeriodic() {
    // Update button edges
    ChezyRobot.operatorJoystick.update();

    // Shooter
    ChezyRobot.shooterController.setVelocityGoal(ChezyRobot.operatorJoystick.getShooterState() ? wantedRpm : 0);

    // Intake Roller
    if (ChezyRobot.operatorJoystick.getIntakeButtonState()) {
      ChezyRobot.frontIntake.setManualRollerPower(1);
    } else if (ChezyRobot.operatorJoystick.getExhaustButtonState()) {
      ChezyRobot.frontIntake.setManualRollerPower(-1);
    } else {
      ChezyRobot.frontIntake.wantManual = false;
    }

    //Auto intake
    ChezyRobot.frontIntake.wantGather = ChezyRobot.operatorJoystick.getAutoIntakeButtonState();
    
    //More Auto intake
    ChezyRobot.frontIntake.wantExtraGather = ChezyRobot.operatorJoystick.getRawButton(2);

    //Intake solenoid
    if (ChezyRobot.operatorJoystick.getIntakeDownSwitchState()) {
      ChezyRobot.rearIntake.wantDown = true ;
    } else {
      ChezyRobot.rearIntake.wantDown = false;
    } 
    
    if (ChezyRobot.operatorJoystick.getIntakeUpSwitchState()) {
      ChezyRobot.frontIntake.wantDown = (true);
    } else {
      ChezyRobot.frontIntake.wantDown = false;
    } 
    
    //Clapper Buttons
      ChezyRobot.clapper.wantShot = ChezyRobot.operatorJoystick.getClapperUpButtonState();
      ChezyRobot.clapper.wantFront = ChezyRobot.operatorJoystick.getFrontClapperButtonState();
      ChezyRobot.clapper.wantRear = ChezyRobot.operatorJoystick.getRearClapperButtonState();
    // Gearing
    if(ChezyRobot.rightStick.getRawButton(2)) {
      ChezyRobot.drivebase.setLowgear(true);
    } else {
      ChezyRobot.drivebase.setLowgear(false);
 
    }
    boolean qt = ChezyRobot.rightStick.getTrigger();
    double turn = ChezyRobot.rightStick.getX();
    ChezyRobot.cdh.cheesyDrive(-ChezyRobot.leftStick.getY(), turn, qt, true);
    
    //System.out.println(", " + Timer.getFPGATimestamp() + ", " + (ChezyRobot.operatorJoystick.getShooterState() ? 1.0 : 0.0) + ", " + ChezyRobot.shooter.lastRpm + ", 0.01");
  }
  
  public void allPeriodic() {
    if (downLatch.update(ChezyRobot.operatorJoystick.getRawButton(3))) {
      wantedRpm -= 50;
    }
    if (upLatch.update(ChezyRobot.operatorJoystick.getRawButton(4))) {
      wantedRpm += 50;
    }
    if (wantedRpm > 5500) {
      wantedRpm = 5500;
    } else if (wantedRpm < 0) {
      wantedRpm = 0;
    }
    lcd();
  }

  public void disabledPeriodic() {
    if (autoSelectLatch.update(ChezyRobot.operatorJoystick.getIntakeButtonState())) {
      selector.increment();
    }
    if (laneSelectLatch.update(ChezyRobot.operatorJoystick.getExhaustButtonState())) {
      selector.incrementLane();
    }
  }

  // LCD
  Timer lcdUpdateTimer = new Timer();
  public void lcd() {
    if (lcdUpdateTimer.get() < .1) {
      return;
    }
    lcdUpdateTimer.reset();
    lcd.println(DriverStationLCD.Line.kUser1, 1, selector.getSeletedId() + ") " +  selector.currentAutoMode().getDescription() + "                  ");
    lcd.println(DriverStationLCD.Line.kUser2, 1, "Lane: " +  selector.getLaneName() + "                  ");
    lcd.println(DriverStationLCD.Line.kUser4, 1,
            " r X:  " + Math.floor(ChezyRobot.rightStick.getX() * 100) / 100
            + " Y: " + Math.floor(ChezyRobot.rightStick.getY() * 100) / 100
            + " Z: " + Math.floor(ChezyRobot.rightStick.getZ() * 10) / 10);
    lcd.println(DriverStationLCD.Line.kUser3, 1,
            " l X:  " + Math.floor(ChezyRobot.leftStick.getX() * 100) / 100
            + " Y: " + Math.floor(ChezyRobot.leftStick.getY() * 100) / 100
            + " Z: " + Math.floor(ChezyRobot.leftStick.getZ() * 10) / 10);
    lcd.println(DriverStationLCD.Line.kUser5, 1, "g: " + Math.floor(wantedRpm * 100) / 100 + " m: " + Math.floor(ChezyRobot.shooter.getLastRpm() * 10) / 10);
    lcd.println(DriverStationLCD.Line.kUser6, 1, "l: " + Math.floor(ChezyRobot.drivebase.getLeftEncoderDistance() * 10) / 10 + " r: " + Math.floor(ChezyRobot.drivebase.getRightEncoderDistance() * 10) / 10 + " g: " + Math.floor(ChezyRobot.drivebase.getGyroAngle() * 10) / 10);
    lcd.updateLCD();
  }
}
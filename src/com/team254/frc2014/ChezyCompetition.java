package com.team254.frc2014;

/**
 * This is where the magic happens!
 *
 */
import com.team254.frc2014.auto.TestDriveAuto;
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
    selector.addAutoMode(new ThreeBallAuto());
    selector.addAutoMode(new TestDriveAuto());
    selector.addAutoMode(new TestUltrasonicAuto());
  }

  public void robotInit() {
    initAutoModes();
    t = new Thread(s);
    t.start();
    lcd = DriverStationLCD.getInstance();
    ChezyRobot.initRobot();
    ChezyRobot.controlUpdater.start();
    lcdUpdateTimer.start();
  }

  public void autonomousInit() {
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
  }

  public void teleopInit() {
    if (currentAutoMode != null) {
      currentAutoMode.stop();
    }
    ChezyRobot.drivebase.turnOffControllers();
    ChezyRobot.intake.setAutoIntake(false);
    // This is just here for testing purposes
  }
  double wantedRpm = 6000;
  Latch upLatch = new Latch();
  Latch downLatch = new Latch();
  Latch autoSelectLatch = new Latch();
  Latch laneSelectLatch = new Latch();

  public void teleopPeriodic() {
    double z = ChezyRobot.rightStick.getZ();
    double x = ChezyRobot.rightStick.getX();
    lcd();
    boolean qt = false;
    double turn = x;
    if (Math.abs(z) > .2 && Math.abs(x) < .4) {
      qt = true;
      turn = z;
    }

    // Shooter
    ChezyRobot.shooter.setVelocityGoal(ChezyRobot.operatorJoystick.getShooterState()?wantedRpm:0);

    // Popper
    ChezyRobot.shooter.setPopper(ChezyRobot.operatorJoystick.getPopperOnState());
    

    // Intake Roller
    if (ChezyRobot.operatorJoystick.getIntakeButtonState()) {
      ChezyRobot.intake.setManualRollerPower(1);
    } else if (ChezyRobot.operatorJoystick.getExhaustButtonState()) {
      ChezyRobot.intake.setManualRollerPower(-1);
    } else {
      ChezyRobot.intake.wantManual = false;
    }

    //Auto intake
    ChezyRobot.intake.wantGather = ChezyRobot.operatorJoystick.getAutoIntakeButtonState();
    
    //More Auto intake
    ChezyRobot.intake.wantExtraGather = ChezyRobot.operatorJoystick.getRawButton(2);

    if (downLatch.update(ChezyRobot.operatorJoystick.getRawButton(3))) {
      wantedRpm -= 50;
    }
    if (upLatch.update(ChezyRobot.operatorJoystick.getRawButton(4))) {
      wantedRpm += 50;
    }


    //Intake solenoid
    if (ChezyRobot.operatorJoystick.getIntakeDownSwitchState()) {
      ChezyRobot.intake.setPositionDown(true);
    } else if (ChezyRobot.operatorJoystick.getIntakeUpSwitchState()) {
      ChezyRobot.intake.setPositionDown(false);
    }

    ChezyRobot.cdh.cheesyDrive(-ChezyRobot.leftStick.getY(), turn, qt, true);
  }

  public void disabledPeriodic() {
    // Print the Ultrasonic value - hardware might be broken
    lcd();
    if (autoSelectLatch.update(ChezyRobot.operatorJoystick.getIntakeButtonState())) {
      selector.increment();
    }
    if (laneSelectLatch.update(ChezyRobot.operatorJoystick.getExhaustButtonState())) {
      selector.incrementLane();
    }
    // p.println("enc: " + ChezyRobot.intake.encoder.get());
  }
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
    lcd.println(DriverStationLCD.Line.kUser5, 1, "g: " + Math.floor(wantedRpm * 10) / 10 + " m: " + Math.floor(ChezyRobot.shooter.getLastRpm() * 10) / 10);
    lcd.println(DriverStationLCD.Line.kUser6, 1, "l: " + Math.floor(ChezyRobot.drivebase.getLeftEncoderDistanceInMeters() * 10) / 10 + " r: " + Math.floor(ChezyRobot.drivebase.getRightEncoderDistanceInMeters() * 10) / 10 + " g: " + Math.floor(ChezyRobot.drivebase.getGyroAngleInRadians() * 10) / 10);
    lcd.updateLCD();
  }
}
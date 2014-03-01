package com.team254.frc2014;

/**
 * This is where the magic happens!
 *
 */
import com.team254.frc2014.auto.TestThreeBallShootAuto;
import com.team254.frc2014.auto.AerialAssistAuto;
import com.team254.frc2014.paths.AutoPaths;
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
    AutoPaths.loadPaths();
    selector.addAutoMode(new AerialAssistAuto());
    selector.addAutoMode(new TestThreeBallShootAuto());
  }

  public void robotInit() {
    initAutoModes();
    t = new Thread(s);
    t.start();
    lcd = DriverStationLCD.getInstance();
    ChezyRobot.initRobot();
    ChezyRobot.shooterController.enable();
    ChezyRobot.subsystemUpdater100Hz.start();
    //ChezyRobot.drivebase.gyro.startCalibrateThread();
    lcdUpdateTimer.start();
  }

  public void autonomousInit() {
    //ChezyRobot.drivebase.gyro.stopCalibrating();
    ChezyRobot.drivebase.resetGyro();
    ChezyRobot.shooterController.enable();
    currentAutoMode = selector.currentAutoMode();
    if (currentAutoMode != null) {
      currentAutoMode.start();
    }
    System.out.println("cfs:auto_start");
  }

  public void disabledInit() {
    Constants.readConstantsFromFile();
    if (currentAutoMode != null) {
      currentAutoMode.stop();
    }
    ChezyRobot.drivebase.turnOffControllers();
    ChezyRobot.drivebase.resetGyro();
    //ChezyRobot.drivebase.gyro.startCalibrateThread();
    ChezyRobot.clapper.wantFront = false;
    ChezyRobot.clapper.wantRear = false;
    System.out.println("cfs:disable_start");
  }

  public void teleopInit() {
    //ChezyRobot.drivebase.gyro.stopCalibrating();
    if (currentAutoMode != null) {
      currentAutoMode.stop();
    }
    ChezyRobot.drivebase.turnOffControllers();
    ChezyRobot.frontIntake.setAutoIntake(false);
    ChezyRobot.drivebase.resetEncoders();
    ChezyRobot.shooter.setHood(false);
    ChezyRobot.shooterController.setVelocityGoal(4300);
    System.out.println("cfs:teleop_start");
    // This is just here for testing purposes
  }
  double wantedRpm = 4000;
  Latch upLatch = new Latch();
  Latch downLatch = new Latch();
  Latch autoSelectLatch = new Latch();
  Latch laneSelectLatch = new Latch();
  Latch numBallsSelectLatch = new Latch();
  Latch doDekeLatch = new Latch();

  public void teleopPeriodic() {
    // Update button edges
    ChezyRobot.operatorJoystick.update();

    // Intake Roller
    if (ChezyRobot.operatorJoystick.getIntakeButtonState()) {
      ChezyRobot.frontIntake.setManualRollerPower(1);
      ChezyRobot.rearIntake.setManualRollerPower(1);
    } else if (ChezyRobot.operatorJoystick.getExhaustButtonState()) {
      ChezyRobot.frontIntake.setManualRollerPower(-1);
      ChezyRobot.rearIntake.setManualRollerPower(-1);
    } else {
      // Run rollers in reverse if clapper button is pressed (for pass)
      ChezyRobot.frontIntake.setManualRollerPower(ChezyRobot.operatorJoystick.getRearClapperButtonState() ? -1.0 : 0);
      ChezyRobot.rearIntake.setManualRollerPower(ChezyRobot.operatorJoystick.getFrontClapperButtonState() ? -1.0 : 0);
    }

    //Auto intake
    ChezyRobot.frontIntake.wantGather = ChezyRobot.operatorJoystick.getAutoIntakeButtonState();

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
 
    // Shooter presets
    // ChezyRobot.shooterController.setVelocityGoal(ChezyRobot.operatorJoystick.getShooterState() ? wantedRpm : 0);
    if (ChezyRobot.operatorJoystick.getRawButton(3)) {
      ChezyRobot.shooterController.setVelocityGoal(4300);
      ChezyRobot.shooter.setHood(false);
    }
    if (ChezyRobot.operatorJoystick.getRawButton(4)) {
      ChezyRobot.shooterController.setVelocityGoal(3500);
      ChezyRobot.shooter.setHood(true);
    }
    if (ChezyRobot.operatorJoystick.getRawButton(2)) {
      ChezyRobot.shooterController.setVelocityGoal(5000);
      ChezyRobot.shooter.setHood(false);
    }
    
    
    if (ChezyRobot.operatorJoystick.getShooterState()) {
      ChezyRobot.shooterController.enable();
    } else {
      ChezyRobot.shooterController.disable();
    }
    
    // Shooting Buttons
    //ChezyRobot.clapper.wantShot = ChezyRobot.operatorJoystick.getShotButtonState() || ChezyRobot.operatorJoystick.getTrussShotButtonState();
    ChezyRobot.clapper.wantShot = ChezyRobot.clapper.wantTimedShot =  ChezyRobot.leftStick.getRawButton(2) || ChezyRobot.leftStick.getRawButton(1) || ChezyRobot.operatorJoystick.getShotButtonState();
    //ChezyRobot.clapper.wantTimedShot = ChezyRobot.operatorJoystick.getTrussShotButtonState();
    
    // Pass buttons
    ChezyRobot.clapper.wantFront = ChezyRobot.operatorJoystick.getFrontClapperButtonState();
    ChezyRobot.clapper.wantRear = ChezyRobot.operatorJoystick.getRearClapperButtonState();
    
    // Run the rear roller in reverse if needed
    //ChezyRobot.rearIntake.wantShoot = ChezyRobot.operatorJoystick.getShotButtonState() || ChezyRobot.operatorJoystick.getTrussShotButtonState();
    ChezyRobot.rearIntake.wantShoot = ChezyRobot.clapper.wantShot;

    ChezyRobot.shooter.wantShotCatch = ChezyRobot.operatorJoystick.getTrussShotButtonState() && ChezyRobot.operatorJoystick.getShooterState();
    ChezyRobot.shooter.wantCatch = ChezyRobot.operatorJoystick.getTrussShotButtonState() && !ChezyRobot.operatorJoystick.getShooterState();

 
    // Gearing
    if(ChezyRobot.rightStick.getRawButton(2)) {
      ChezyRobot.drivebase.setLowgear(true);
    } else {
      ChezyRobot.drivebase.setLowgear(false);
    }
    
    
    boolean qt = ChezyRobot.rightStick.getTrigger();
    double turn = ChezyRobot.rightStick.getX();
    if (qt) {
      boolean turnNeg = turn < 0.0;
      turn = Math.abs(turn * turn) * (turnNeg ? -1.0 : 1.0);
    }
    
    ChezyRobot.cdh.cheesyDrive(-ChezyRobot.leftStick.getY(), turn, qt, !ChezyRobot.rightStick.getRawButton(2));

  }
  
  public void allPeriodic() {
    if (downLatch.update(ChezyRobot.operatorJoystick.getRawButton(3))) {
      wantedRpm -= 50;
    }
    if (upLatch.update(ChezyRobot.operatorJoystick.getRawButton(4))) {
      wantedRpm += 50;
    }
    if (wantedRpm > 10000) {
      wantedRpm = 10000;
    } else if (wantedRpm < 0) {
      wantedRpm = 0;
    }
    lcd();
  }

  public void disabledPeriodic() {
    if (autoSelectLatch.update(ChezyRobot.operatorJoystick.getIntakeButtonState())) {
      selector.increment();
    }
    if (laneSelectLatch.update(ChezyRobot.operatorJoystick.getExhaustButtonState() || ChezyRobot.rightStick.getRawButton(2))) {
      selector.incrementLane();
    }
    if (numBallsSelectLatch.update(ChezyRobot.leftStick.getRawButton(1))) {
      selector.decrementNumBalls();
    }
    if (doDekeLatch.update(ChezyRobot.rightStick.getRawButton(1))) {
      selector.toggleDoDeke();
    }
    
    // Hot goal detector updating
    ChezyRobot.hotGoalDetector.updateDisabled();
  }

  // LCD
  Timer lcdUpdateTimer = new Timer();
  public void lcd() {
    if (lcdUpdateTimer.get() < .1) {
      return;
    }
    lcdUpdateTimer.reset();
    lcd.println(DriverStationLCD.Line.kUser1, 1, "M:" + selector.currentAutoMode().getDescription() + "                  ");
    lcd.println(DriverStationLCD.Line.kUser2, 1, "Pos: Center | #B:" +  selector.getNumBallsWithPreference() + "        ");
    lcd.println(DriverStationLCD.Line.kUser3, 1, "L: " +  selector.getLaneName() + " | Deke:" + (selector.getDoDeke() ? "Yes" : "No") + "        ");
    lcd.println(DriverStationLCD.Line.kUser5, 1, "LE: " + Math.floor(ChezyRobot.drivebase.getLeftEncoderDistance() * 10) / 10 + " RE: " + Math.floor(ChezyRobot.drivebase.getRightEncoderDistance() * 10) / 10);
    lcd.println(DriverStationLCD.Line.kUser4, 1, "F:" + ChezyRobot.frontIntake.getIntakeSensor() + " R:" + ChezyRobot.rearIntake.getIntakeSensor());
   // lcd.println(DriverStationLCD.Line.kUser5, 1, "g: " + Math.floor(wantedRpm * 100) / 100 + " m: " + Math.floor(ChezyRobot.shooter.getLastRpm() * 10) / 10);
    lcd.println(DriverStationLCD.Line.kUser6, 1,  Math.floor(Timer.getFPGATimestamp() * 10) / 10 +  " gyro: " + Math.floor(ChezyRobot.drivebase.getGyroAngle() * 10) / 10 + "        ");
    lcd.updateLCD();
  }
}

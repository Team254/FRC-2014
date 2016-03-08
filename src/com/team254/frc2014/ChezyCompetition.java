package com.team254.frc2014;

/**
 * This is where the magic happens!
 *
 */
import com.team254.frc2014.auto.AerialAssistAuto;
import com.team254.frc2014.auto.DoNothingAuto;
import com.team254.frc2014.auto.TwoBallHotAutoMode;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.ChezyIterativeRobot;
import com.team254.lib.ChezyHTTPServer;
import com.team254.lib.util.Latch;
import com.team254.lib.util.ThrottledPrinter;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;

public class ChezyCompetition extends ChezyIterativeRobot {
  AutoMode currentAutoMode = null;
  AutoModeSelector selector = new AutoModeSelector();
  ChezyHTTPServer s = new ChezyHTTPServer();
  ThrottledPrinter p = new ThrottledPrinter(.5);
  DriverStationLCD lcd;

  public void initAutoModes() {
    AutoPaths.loadPaths();
    selector.addAutoMode(new AerialAssistAuto());
    selector.addAutoMode(new TwoBallHotAutoMode());
    selector.addAutoMode(new DoNothingAuto());
  }

  public void robotInit() {
    initAutoModes();
    lcd = DriverStationLCD.getInstance();
    ChezyRobot.initRobot();
    ChezyRobot.shooterController.enable();
    ChezyRobot.subsystemUpdater100Hz.start();
    lcdUpdateTimer.start();
  }

  public void autonomousInit() {
    ChezyRobot.drivebase.resetGyro();
    
    ChezyRobot.shooterController.enable();
    ChezyRobot.pinniped.setControlLoopsOff();
    currentAutoMode = selector.currentAutoMode();
    ChezyRobot.pinniped.doingRunning = false;
    if (currentAutoMode != null) {
      currentAutoMode.start();
    }
    System.out.println("cfs:auto_start");
    ChezyRobot.drivebase.setLowgear(false);
  }

  public void disabledInit() {
    Constants.readConstantsFromFile();
    if (currentAutoMode != null) {
      currentAutoMode.stop();
    }
    ChezyRobot.drivebase.turnOffControllers();
    ChezyRobot.drivebase.resetGyro();
    ChezyRobot.pinniped.wantFront = false;
    ChezyRobot.pinniped.wantRear = false;
    ChezyRobot.frontIntake.wantBumperGather = false;
    ChezyRobot.rearIntake.wantBumperGather = false;
    ChezyRobot.bannerHotGoalDetector.reset();
    ChezyRobot.pinniped.doingRunning = false;
    System.out.println("cfs:disable_start");
  }

  public void teleopInit() {
    if (currentAutoMode != null) {
      currentAutoMode.stop();
    }
    ChezyRobot.drivebase.turnOffControllers();
    ChezyRobot.frontIntake.setAutoIntake(false);
    ChezyRobot.drivebase.resetEncoders();
    ChezyRobot.shooter.setHood(false);
    ChezyRobot.shooterController.disable();
    ChezyRobot.shooterController.setVelocityGoal(0);
    ChezyRobot.pinniped.doingRunning = false;
    System.out.println("cfs:teleop_start");
    // This is just here for testing purposes
  }
  double wantedRpm = 4000;
  Latch upLatch = new Latch();
  Latch downLatch = new Latch();
  Latch autoSelectLatch = new Latch();
  Latch laneSelectLatch = new Latch();
  Latch numBallsSelectLatch = new Latch();
  Latch startOnLeftLatch = new Latch();
  Latch endCloseLatch = new Latch();
  Latch gyroInitLatch = new Latch();

  public void teleopPeriodic() {
    double frontRollerPower = 0;
    double rearRollerPower = 0;
    // Intake Roller
    if (ChezyRobot.operatorJoystick.getIntakeButton()) {
      frontRollerPower = rearRollerPower = 1;
    } else if (ChezyRobot.operatorJoystick.getExhaustButton()) {
      frontRollerPower = rearRollerPower = -1;
    } else if (ChezyRobot.operatorJoystick.getPassFrontButton()) {
      frontRollerPower = -1;
    } else if (ChezyRobot.operatorJoystick.getPassRearButton()) {
      rearRollerPower = -1;
    }
  
    // Intakes
    if (ChezyRobot.operatorJoystick.getAutoIntakeOff()) { // manual intake
      ChezyRobot.pinniped.setControlLoopsOff();
      ChezyRobot.frontIntake.wantGather = false;
      ChezyRobot.rearIntake.wantGather = false;
      if (ChezyRobot.operatorJoystick.getAutoIntakeFrontButton()) {
        frontRollerPower = 1;
      } else if (ChezyRobot.operatorJoystick.getAutoIntakeRearButton()) {
        rearRollerPower = 1;
      }
      ChezyRobot.frontIntake.wantDown = ChezyRobot.operatorJoystick.getAutoIntakeFrontButton();
      ChezyRobot.rearIntake.wantDown = ChezyRobot.operatorJoystick.getAutoIntakeRearButton();
    } else { // auto intake
      ChezyRobot.pinniped.setControlLoopsOn();
      ChezyRobot.frontIntake.wantGather = ChezyRobot.operatorJoystick.getAutoIntakeFrontButton();
      ChezyRobot.rearIntake.wantGather = ChezyRobot.operatorJoystick.getAutoIntakeRearButton();
    }
    

    // Shooter presets
    // Off
    if (ChezyRobot.operatorJoystick.getShooterOffButton()) {
      ChezyRobot.shooterController.disable();
      ChezyRobot.shooterController.setVelocityGoal(0);
      ChezyRobot.pinniped.doingRunning = false;
      ChezyRobot.shooterController.setNarrowOnTargetWindow();
    }
    // Running close
    if (ChezyRobot.operatorJoystick.getPreset1Button()) {
      ChezyRobot.pinniped.doingRunning = true;
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.cheekyPassPreset.getDouble());
      ChezyRobot.shooter.setHood(false);
      ChezyRobot.shooterController.setWideOnTargetWindow();
    }
    // static close
    if (ChezyRobot.operatorJoystick.getPreset2Button()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.staticClosePreset.getDouble());
      ChezyRobot.shooter.setHood(true);
      ChezyRobot.pinniped.doingRunning = false;
      ChezyRobot.shooterController.setNarrowOnTargetWindow();
    }
    // static Far
    if (ChezyRobot.operatorJoystick.getPreset3Button()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.staticFarPreset.getDouble());
      ChezyRobot.shooter.setHood(false);
      ChezyRobot.pinniped.doingRunning = false;
      ChezyRobot.shooterController.setNarrowOnTargetWindow();
    }
    // running far
    if (ChezyRobot.operatorJoystick.getPreset4Button()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.runningFarPreset.getDouble());
      ChezyRobot.shooter.setHood(false);
      ChezyRobot.pinniped.doingRunning = true;
      ChezyRobot.shooterController.setWideOnTargetWindow();
    }
    // Hella Far
    if (ChezyRobot.operatorJoystick.getPreset6Button()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.hellaFarPreset.getDouble());
      ChezyRobot.shooter.setHood(false);
      ChezyRobot.pinniped.doingRunning = true;
      ChezyRobot.shooterController.setWideOnTargetWindow();
    }
    // HP Shot
    if (ChezyRobot.operatorJoystick.getPreset5Button()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.hpShotPreset.getDouble());
      ChezyRobot.shooter.setHood(false);
      ChezyRobot.pinniped.doingRunning = true;
      ChezyRobot.shooterController.setWideOnTargetWindow();
    }
    
    // Inbounding
    ChezyRobot.operatorJoystick.autonInboundButton.update();
    boolean wantCatcherOpen = ChezyRobot.operatorJoystick.autonInboundButton.get() || ChezyRobot.operatorJoystick.getNoMotorInboundButton();
    ChezyRobot.shooter.wantCatcherOpen = wantCatcherOpen;
    if (wantCatcherOpen) {
      ChezyRobot.shooter.setHood(false);
    }
    if (ChezyRobot.operatorJoystick.autonInboundButton.wasPressed()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.inboundRpmPreset.getDouble());
      ChezyRobot.shooter.setHood(false);
      ChezyRobot.pinniped.doingRunning = false;
    } else if (ChezyRobot.operatorJoystick.autonInboundButton.wasReleased()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(0);
      ChezyRobot.pinniped.doingRunning = false;
      ChezyRobot.shooterController.setNarrowOnTargetWindow();
    }


    
    // Shooting Buttons
    ChezyRobot.pinniped.wantShot = ChezyRobot.pinniped.wantTimedShot =  ChezyRobot.leftStick.getRawButton(1);
    
    // Pass buttons
    ChezyRobot.pinniped.wantFront = ChezyRobot.operatorJoystick.getPassRearButton();
    ChezyRobot.pinniped.wantRear = ChezyRobot.operatorJoystick.getPassFrontButton();
    
    // Run the rear roller in reverse if needed
    ChezyRobot.rearIntake.wantShoot = ChezyRobot.pinniped.wantShot;

    // Gearing
    boolean lowGear = ChezyRobot.leftStick.getRawButton(5);
    if (lowGear) {
      ChezyRobot.drivebase.setLowgear(true);
    } else {
      ChezyRobot.drivebase.setLowgear(false);
    }
    
    // Cheesy Drive
    boolean qt = ChezyRobot.leftStick.getRawButton(6);
    double turn = ChezyRobot.leftStick.getZ();
    if (qt) {
      // Square the inputs on turn in quick turn
      boolean turnNeg = turn < 0.0;
      turn = Math.abs(turn * turn) * (turnNeg ? -1.0 : 1.0);
    }
    
    ChezyRobot.cdh.cheesyDrive(-ChezyRobot.leftStick.getY(), turn, qt, !lowGear);

    
    // Set rollers
    ChezyRobot.frontIntake.setManualRollerPower(frontRollerPower);
    ChezyRobot.rearIntake.setManualRollerPower(rearRollerPower);
 
    ChezyRobot.settler.set(ChezyRobot.leftStick.getRawButton(2));
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
    
    if(ChezyRobot.shooterController.onTarget() && ChezyRobot.shooterController.enabled() && Math.abs(ChezyRobot.shooterController.getVelocityGoal()) > 0) {
      ChezyRobot.shooter.shooterLed.set(true);
      ChezyRobot.shooter.shooterLedRelay.set(Relay.Value.kForward);
    } else {
      ChezyRobot.shooter.shooterLed.set(false);
      ChezyRobot.shooter.shooterLedRelay.set(Relay.Value.kOff);
    }
  }

  public void disabledPeriodic() {
    if (autoSelectLatch.update(ChezyRobot.operatorJoystick.getShooterOffButton() && ChezyRobot.operatorJoystick.getPreset2Button() && ChezyRobot.operatorJoystick.getNoMotorInboundButton())) { //secrets!
      selector.increment();
    }
    if (laneSelectLatch.update(ChezyRobot.operatorJoystick.getIntakeButton())) {
      selector.incrementLane();
    }
    if (numBallsSelectLatch.update(ChezyRobot.operatorJoystick.getPassFrontButton())) {
      selector.decrementNumBalls();
    }
    if (startOnLeftLatch.update(ChezyRobot.operatorJoystick.getExhaustButton())) {
      selector.toggleStartOnLeft();
    }
    if (endCloseLatch.update(ChezyRobot.operatorJoystick.getPassRearButton())) {

    }
    
    if (gyroInitLatch.update(ChezyRobot.operatorJoystick.getPreset6Button())) {
      ChezyRobot.drivebase.gyro.initGyro();
    }
    ChezyRobot.drivebase.setLowgear(false);
  }

  // LCD
  Timer lcdUpdateTimer = new Timer();
  public void lcd() {
    if (lcdUpdateTimer.get() < .1) {
      return;
    }
    lcdUpdateTimer.reset();
    lcd.println(DriverStationLCD.Line.kUser1, 1, "M:" + selector.currentAutoMode().getDescription() + "                  ");
    lcd.println(DriverStationLCD.Line.kUser2, 1, "#B:" +  selector.getNumBallsWithPreference() +  "            ");
    lcd.println(DriverStationLCD.Line.kUser3, 1, "Path: " +  selector.getPathName() + "           ");
    lcd.println(DriverStationLCD.Line.kUser4, 1, "Side:" + (selector.getStartOnLeft() ? "Left" : "Right") + " | VIZ:" + (ChezyRobot.visionHotGoalDetector.hasClientConnection() ? "Yes":"No") + "      ");
    lcd.println(DriverStationLCD.Line.kUser6, 1,  Math.floor(Timer.getFPGATimestamp() * 10) / 10 +  " gyro: " + Math.floor(ChezyRobot.drivebase.getGyroAngle() * 10) / 10 + "        ");
    lcd.println(DriverStationLCD.Line.kUser5, 1, "HOT: " + ChezyRobot.hotGoalDirection + "          "); 
    lcd.updateLCD();
  }
}

package com.team254.frc2014;

/**
 * This is where the magic happens!
 *
 */
import com.team254.frc2014.auto.AerialAssistAuto;
import com.team254.frc2014.auto.TestBumperGather;
import com.team254.frc2014.auto.TestGooglyEyes;
import com.team254.frc2014.auto.TestThreeBallShootAuto;
import com.team254.frc2014.auto.TestVisionHotGoal;
import com.team254.frc2014.auto.TuneDriveAuto;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.ChezyIterativeRobot;
import com.team254.lib.Server;
import com.team254.lib.util.Latch;
import com.team254.lib.util.ThrottledPrinter;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;

public class ChezyCompetition extends ChezyIterativeRobot {
  AutoMode currentAutoMode = null;
  AutoModeSelector selector = new AutoModeSelector();
  Server s = new Server();
  ThrottledPrinter p = new ThrottledPrinter(.5);
  DriverStationLCD lcd;
  
  public void initAutoModes() {
    AutoPaths.loadPaths();
    selector.addAutoMode(new AerialAssistAuto());
    selector.addAutoMode(new TestBumperGather());
    selector.addAutoMode(new TestVisionHotGoal());
    selector.addAutoMode(new TestThreeBallShootAuto());
    selector.addAutoMode(new TuneDriveAuto());
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
    ChezyRobot.clapper.setControlLoopsOff();
    currentAutoMode = selector.currentAutoMode();
    ChezyRobot.clapper.doingRunning = false;
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
    ChezyRobot.clapper.wantFront = false;
    ChezyRobot.clapper.wantRear = false;
    ChezyRobot.frontIntake.wantBumperGather = false;
    ChezyRobot.rearIntake.wantBumperGather = false;
    ChezyRobot.hotGoalDetector.reset();
    ChezyRobot.clapper.doingRunning = false;
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
    ChezyRobot.clapper.doingRunning = false;
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
      ChezyRobot.clapper.setControlLoopsOff();
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
      ChezyRobot.clapper.setControlLoopsOn();
      ChezyRobot.frontIntake.wantGather = ChezyRobot.operatorJoystick.getAutoIntakeFrontButton();
      ChezyRobot.rearIntake.wantGather = ChezyRobot.operatorJoystick.getAutoIntakeRearButton();
    }
    

    // Shooter presets
    // Off
    if (ChezyRobot.operatorJoystick.getShooterOffButton()) {
      ChezyRobot.shooterController.disable();
      ChezyRobot.shooterController.setVelocityGoal(0);
      ChezyRobot.clapper.doingRunning = false;
      ChezyRobot.shooterController.setNarrowOnTargetWindow();
    }
    // Running close
    if (ChezyRobot.operatorJoystick.getPreset1Button()) {
      ChezyRobot.clapper.doingRunning = true;
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.runningClosePreset.getDouble());
      ChezyRobot.shooter.setHood(true);
      ChezyRobot.shooterController.setWideOnTargetWindow();
    }
    // static close
    if (ChezyRobot.operatorJoystick.getPreset2Button()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.staticClosePreset.getDouble());
      ChezyRobot.shooter.setHood(true);
      ChezyRobot.clapper.doingRunning = false;
      ChezyRobot.shooterController.setNarrowOnTargetWindow();
    }
    // static Far
    if (ChezyRobot.operatorJoystick.getPreset3Button()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.staticFarPreset.getDouble());
      ChezyRobot.shooter.setHood(false);
      ChezyRobot.clapper.doingRunning = false;
      ChezyRobot.shooterController.setNarrowOnTargetWindow();
    }
    // running far
    if (ChezyRobot.operatorJoystick.getPreset4Button()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.runningFarPreset.getDouble());
      ChezyRobot.shooter.setHood(false);
      ChezyRobot.clapper.doingRunning = true;
      ChezyRobot.shooterController.setWideOnTargetWindow();
    }
    // Hella Far
    if (ChezyRobot.operatorJoystick.getPreset6Button()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.hellaFarPreset.getDouble());
      ChezyRobot.shooter.setHood(false);
      ChezyRobot.clapper.doingRunning = true;
      ChezyRobot.shooterController.setWideOnTargetWindow();
    }
    // HP Shot
    if (ChezyRobot.operatorJoystick.getPreset5Button()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.hpShotPreset.getDouble());
      ChezyRobot.shooter.setHood(false);
      ChezyRobot.clapper.doingRunning = true;
      ChezyRobot.shooterController.setWideOnTargetWindow();
    }
    
    // Inbounding
    ChezyRobot.operatorJoystick.inboundButton.update();
    ChezyRobot.shooter.wantCatcherOpen = ChezyRobot.operatorJoystick.inboundButton.get();
    if (ChezyRobot.operatorJoystick.inboundButton.wasPressed()) {
      ChezyRobot.shooterController.enable();
      ChezyRobot.shooterController.setVelocityGoal(Constants.inboundRpmPreset.getDouble());
      ChezyRobot.shooter.setHood(false);
      ChezyRobot.clapper.doingRunning = false;
    } else if (ChezyRobot.operatorJoystick.inboundButton.wasReleased()) {
      ChezyRobot.shooterController.disable();
      ChezyRobot.clapper.doingRunning = false;
      ChezyRobot.shooterController.setNarrowOnTargetWindow();
    }


    
    // Shooting Buttons
    ChezyRobot.clapper.wantShot = ChezyRobot.clapper.wantTimedShot =  ChezyRobot.leftStick.getRawButton(1);
    
    // Pass buttons
    ChezyRobot.clapper.wantFront = ChezyRobot.operatorJoystick.getPassRearButton();
    ChezyRobot.clapper.wantRear = ChezyRobot.operatorJoystick.getPassFrontButton();
    
    // Run the rear roller in reverse if needed
    ChezyRobot.rearIntake.wantShoot = ChezyRobot.clapper.wantShot;

    // Gearing
    if(ChezyRobot.rightStick.getRawButton(2)) {
      ChezyRobot.drivebase.setLowgear(true);
    } else {
      ChezyRobot.drivebase.setLowgear(false);
    }
    
    // Cheesy Drive
    boolean qt = ChezyRobot.rightStick.getTrigger();
    double turn = ChezyRobot.rightStick.getX();
    if (qt) {
      // Square the inputs on turn in quick turn
      boolean turnNeg = turn < 0.0;
      turn = Math.abs(turn * turn) * (turnNeg ? -1.0 : 1.0);
    }
    
    ChezyRobot.cdh.cheesyDrive(-ChezyRobot.leftStick.getY(), turn, qt, !ChezyRobot.rightStick.getRawButton(2));
    
    ChezyRobot.popUpPistion.set(ChezyRobot.leftStick.getRawButton(2));
    
    // Set rollers
    ChezyRobot.frontIntake.setManualRollerPower(frontRollerPower);
    ChezyRobot.rearIntake.setManualRollerPower(rearRollerPower);
 
    //ChezyRobot.settler.set(false);
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
    //p.println("left: " + ChezyRobot.hotGoalDetector.getLeft() + " right " + ChezyRobot.hotGoalDetector.getRight());
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
    if (autoSelectLatch.update(ChezyRobot.operatorJoystick.getShooterOffButton() && ChezyRobot.operatorJoystick.getPreset2Button() && ChezyRobot.operatorJoystick.getAutoCatchButton())) { //secrets!
      selector.increment();
    }
    if (laneSelectLatch.update(ChezyRobot.operatorJoystick.getIntakeButton())) {
      selector.incrementLane();
    }
    if (numBallsSelectLatch.update(ChezyRobot.operatorJoystick.getPassFrontButton())) {
      selector.decrementNumBalls();
    }
    if (doDekeLatch.update(ChezyRobot.operatorJoystick.getExhaustButton())) {
     // selector.toggleDoDeke();
    }
    if (endCloseLatch.update(ChezyRobot.operatorJoystick.getPassRearButton())) {
      //selector.toggleEndClose();
      ChezyRobot.hotGoalDetector.toggleCrossEyed();
    }
    
    if (gyroInitLatch.update(ChezyRobot.operatorJoystick.getPreset6Button())) {
      ChezyRobot.drivebase.gyro.initGyro();
    }
  }

  // LCD
  Timer lcdUpdateTimer = new Timer();
  public void lcd() {
    if (lcdUpdateTimer.get() < .1) {
      return;
    }
    lcdUpdateTimer.reset();
    lcd.println(DriverStationLCD.Line.kUser3, 1, "M:" + selector.currentAutoMode().getDescription() + "                  ");
    lcd.println(DriverStationLCD.Line.kUser1, 1, "#B:" +  selector.getNumBallsWithPreference() + " | X'd:" + (ChezyRobot.hotGoalDetector.isCrossEyed() ? "Yes" : "No") +"          ");
    lcd.println(DriverStationLCD.Line.kUser2, 1, "Path: " +  selector.getPathName() + "           ");
    //lcd.println(DriverStationLCD.Line.kUser5, 1, "LE: " + Math.floor(ChezyRobot.drivebase.getLeftEncoderDistance() * 10) / 10 + " RE: " + Math.floor(ChezyRobot.drivebase.getRightEncoderDistance() * 10) / 10);
    lcd.println(DriverStationLCD.Line.kUser4, 1, "F:" + (ChezyRobot.frontIntake.getIntakeSensor() ? "1" : "0") + "R:" + (ChezyRobot.rearIntake.getIntakeSensor() ? "1" : "0") + " L:" + (ChezyRobot.hotGoalDetector.getLeftRaw() ? "1" : "0") + "R:" + (ChezyRobot.hotGoalDetector.getRightRaw() ? "1" : "0") + "     " );
    lcd.println(DriverStationLCD.Line.kUser6, 1,  Math.floor(Timer.getFPGATimestamp() * 10) / 10 +  " gyro: " + Math.floor(ChezyRobot.drivebase.getGyroAngle() * 10) / 10 + "        ");
    lcd.println(DriverStationLCD.Line.kUser5, 1, "" + (ChezyRobot.goLeftAuto ? "L" : "R") + " L" + ChezyRobot.leftCount +"/"+ ChezyRobot.leftTotal + " R" + ChezyRobot.rightCount +"/" + ChezyRobot.rightTotal + " NS:" +( ChezyRobot.hotGoalDetector.getNotSure() ? "1" : "0") ); 
    lcd.updateLCD();
  }
}

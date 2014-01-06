package com.team254.lib;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.communication.FRCControl;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class ChezyIterativeRobot extends RobotBase implements Loopable {

  private boolean m_disabledInitialized;
  private boolean m_autonomousInitialized;
  private boolean m_teleopInitialized;
  private boolean m_testInitialized;
  boolean didDisabledPeriodic = false;
  boolean didAutonomousPeriodic = false;
  boolean didTeleopPeriodic = false;
  boolean didTestPeriodic = false;
  Looper mainRobotLooper = new Looper(this, 1.0 / 100.0);

  public void startCompetition() {
    LiveWindow.setEnabled(false);
    robotInit();
    mainRobotLooper.start();
    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
  }

  public void update() {

    if (isDisabled()) {
      // call DisabledInit() if we are now just entering disabled mode from
      // either a different mode or from power-on
      if (!m_disabledInitialized) {

        disabledInit();
        m_disabledInitialized = true;
        // reset the initialization flags for the other modes
        m_autonomousInitialized = false;
        m_teleopInitialized = false;
        m_testInitialized = false;
      }
      FRCControl.observeUserProgramDisabled();
      disabledPeriodic();
      didDisabledPeriodic = true;
    } else if (isAutonomous()) {
      // call Autonomous_Init() if this is the first time
      // we've entered autonomous_mode
      if (!m_autonomousInitialized) {
        // KBS NOTE: old code reset all PWMs and relays to "safe values"
        // whenever entering autonomous mode, before calling
        // "Autonomous_Init()"
        autonomousInit();
        m_autonomousInitialized = true;
        m_testInitialized = false;
        m_teleopInitialized = false;
        m_disabledInitialized = false;
      }
      getWatchdog().feed();
      FRCControl.observeUserProgramAutonomous();
      autonomousPeriodic();
      didAutonomousPeriodic = true;
    } else {
      // call Teleop_Init() if this is the first time
      // we've entered teleop_mode
      if (!m_teleopInitialized) {
        LiveWindow.setEnabled(false);
        teleopInit();
        m_teleopInitialized = true;
        m_testInitialized = false;
        m_autonomousInitialized = false;
        m_disabledInitialized = false;
      }
      getWatchdog().feed();
      FRCControl.observeUserProgramTeleop();
      teleopPeriodic();
      didTeleopPeriodic = true;
    }

  }

  /* ----------- Overridable initialization code -----------------*/
  /**
   * Robot-wide initialization code should go here.
   *
   * Users should override this method for default Robot-wide initialization
   * which will be called when the robot is first powered on. It will be called
   * exactly 1 time.
   */
  public void robotInit() {
    System.out.println("ChezyIterativeRobot IterativeRobot.robotInit() method... Overload me!");
  }

  /**
   * Initialization code for disabled mode should go here.
   *
   * Users should override this method for initialization code which will be
   * called each time the robot enters disabled mode.
   */
  public void disabledInit() {
    System.out.println("ChezyIterativeRobot IterativeRobot.disabledInit() method... Overload me!");
  }

  /**
   * Initialization code for autonomous mode should go here.
   *
   * Users should override this method for initialization code which will be
   * called each time the robot enters autonomous mode.
   */
  public void autonomousInit() {
    System.out.println("ChezyIterativeRobot IterativeRobot.autonomousInit() method... Overload me!");
  }

  /**
   * Initialization code for teleop mode should go here.
   *
   * Users should override this method for initialization code which will be
   * called each time the robot enters teleop mode.
   */
  public void teleopInit() {
    System.out.println("ChezyIterativeRobot IterativeRobot.teleopInit() method... Overload me!");
  }

  /**
   * Initialization code for test mode should go here.
   *
   * Users should override this method for initialization code which will be
   * called each time the robot enters test mode.
   */
  public void testInit() {
    System.out.println("ChezyIterativeRobot IterativeRobot.testInit() method... Overload me!");
  }

  /* ----------- Overridable periodic code -----------------*/
  private boolean dpFirstRun = true;

  /**
   * Periodic code for disabled mode should go here.
   *
   * Users should override this method for code which will be called
   * periodically at a regular rate while the robot is in disabled mode.
   */
  public void disabledPeriodic() {
    if (dpFirstRun) {
      System.out.println("ChezyIterativeRobot IterativeRobot.disabledPeriodic() method... Overload me!");
      dpFirstRun = false;
    }
    Timer.delay(0.001);
  }
  private boolean apFirstRun = true;

  /**
   * Periodic code for autonomous mode should go here.
   *
   * Users should override this method for code which will be called
   * periodically at a regular rate while the robot is in autonomous mode.
   */
  public void autonomousPeriodic() {
    if (apFirstRun) {
      System.out.println("ChezyIterativeRobot IterativeRobot.autonomousPeriodic() method... Overload me!");
      apFirstRun = false;
    }
    Timer.delay(0.001);
  }
  private boolean tpFirstRun = true;

  /**
   * Periodic code for teleop mode should go here.
   *
   * Users should override this method for code which will be called
   * periodically at a regular rate while the robot is in teleop mode.
   */
  public void teleopPeriodic() {
    if (tpFirstRun) {
      System.out.println("ChezyIterativeRobot IterativeRobot.teleopPeriodic() method... Overload me!");
      tpFirstRun = false;
    }
    Timer.delay(0.001);
  }
}

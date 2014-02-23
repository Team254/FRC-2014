package com.team254.frc2014.auto;

import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.LanedAutoMode;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.trajectory.Path;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Mani Gnanasivam
 * @author EJ Sebathia
 */
public class TestThreeBallShootAuto extends LanedAutoMode {

  public TestThreeBallShootAuto() {
    super("Test Shoooting");
  }
  static Path path = AutoPaths.get("CenterLanePath");
  protected void routine() {
    
    boolean goLeft = false;
    Timer t = new Timer();
    t.start();
    
    // Turn on wheel
    shooterController.setVelocityGoal(4000);
    
    // Grab balls from ground
    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.wantBumperGather = true;
    rearIntake.wantBumperGather = true;
    waitTime(.5);
    

    // Drive to correct place
    if (goLeft)
      path.goLeft();
    else
      path.goRight();
    drivePath(path, 10);
   
    // Hold position
    drivebase.resetEncoders();
    System.out.println("Drive time: " + t.get());
    headingController.setDistance(0);
    headingController.setHeading(Math.toDegrees(Math.PI/6.0) * (goLeft ? 1.0 : -1.0));
    drivebase.useController(headingController);
    
    // Wait for 5 seconds in
    waitTime(.75);
 
    System.out.println("Shooting 1st ball at: " + t.get());
    // Shoot first ball
    rearIntake.wantShoot = frontIntake.wantShoot = true;
    waitTime(.25);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    rearIntake.wantShoot = frontIntake.wantShoot = false;
    
    // Speed up for 2nd and 3rd shots
   shooterController.setVelocityGoal(4300);
    
    // Queue 2nd ball
    rearIntake.wantBumperGather = false;
    rearIntake.wantDown = true;
    rearIntake.setManualRollerPower(1.0);
    waitTime(0.3);
    rearIntake.wantDown = false;
    waitTime(.4);
    rearIntake.setManualRollerPower(0);
    
    // Settle time
    waitTime(.75);
    
    // Shoot second ball
    frontIntake.wantShoot = true;
    waitTime(.25);
    rearIntake.setManualRollerPower(1);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    rearIntake.setManualRollerPower(0);
    waitTime(0.3);
    frontIntake.wantShoot = false;
    
    // Queue 3rd ball
    frontIntake.wantBumperGather = false;
    frontIntake.wantDown = true;
    frontIntake.setManualRollerPower(0.5);
    waitTime(0.3);
    frontIntake.wantDown = false;
    waitTime(0.5);
    frontIntake.setManualRollerPower(0.00);

    
    // Settle time
    waitTime(1.0);
    
    // Shoot thirdball
    rearIntake.setManualRollerPower(1);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    
    // Print out time
    System.out.println(t.get());
    
    // Clean up
    rearIntake.setManualRollerPower(0);
    shooterController.setVelocityGoal(0);
  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLine;
  }
}

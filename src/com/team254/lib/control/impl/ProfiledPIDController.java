package com.team254.lib.control.impl;

import com.team254.lib.control.ControlOutput;
import com.team254.lib.control.ControlSource;
import com.team254.lib.control.MotionProfile;
import com.team254.lib.control.PIDGains;
import com.team254.lib.util.ThrottledPrinter;
import edu.wpi.first.wpilibj.Timer;

/**
 * Represents a PID controller that is driven by a motion profile.
 *
 * @author tom@team254.com (Tom Bottiglieri)
 */
public class ProfiledPIDController extends PIDController {
  private Timer timer = new Timer();
  double origGoal;
  double sign = 1;
  ThrottledPrinter printer = new ThrottledPrinter(.1);
  MotionProfile profile;

  public ProfiledPIDController(String name, PIDGains gains, ControlSource source,
                               ControlOutput output, MotionProfile profile) {
    super(name, gains, source, output);
    this.profile = profile;
  }

  public void update() {
    if (enabled) {
      double t = timer.get();
      super.setGoal(profile.updateSetpoint(getGoal(), source.get(), t));
    }

    super.update();
  }

  public void setGoal(double goal) {
    origGoal = goal;
    timer.reset();
    timer.start();
    profile.setGoal(goal, source.get(), timer.get());
    super.setGoal(source.get());

  }

  public boolean onTarget() {
    boolean done = !enabled || (Math.abs(origGoal - lastSource) < onTargetError)
            && (Math.abs(lastDeltaError) < onTargetDeltaError);
    if (done) {
      System.out.println(name + " DONE");
    }
    return done;
  }

  public void setProfile(MotionProfile profile) {
    this.profile = profile;
    setGoal(source.get());
  }
}

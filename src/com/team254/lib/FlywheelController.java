package com.team254.lib;

import com.team254.lib.util.Debouncer;
import com.team254.lib.util.Matrix;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * Controls a flywheel using full state feedback.
 *
 * @author tom@team254.com (Tom Bottiglieri)
 */
public class FlywheelController extends StateSpaceController {
  ControlOutput output;
  ControlSource sensor;
  double velGoal; // Radians per second
  Matrix y;
  Matrix r;
  double prevPos;
  double goal;
  double curVel;
  double period = 1.0 / 100.0;
  double outputVoltage = 0.0;
  double targetError = 9;

  Debouncer filter = new Debouncer(.15);

  public FlywheelController(String name, ControlOutput output,
          ControlSource sensor, StateSpaceGains gains) {
    this(name, output, sensor, gains, 1 / 100.0);
  }

  public FlywheelController(String name, ControlOutput output,
          ControlSource sensor, StateSpaceGains gains, double period) {
    super(name, 1, 1, 2, gains, period);
    this.output = output;
    this.sensor = sensor;
    this.velGoal = 0.0;
    this.y = new Matrix(1, 1);
    this.r = new Matrix(2, 1);
    this.period = period;
  }

  public void capU() {
    double deltaU = U.get(0);
    double u_max = Umax.get(0);
    double u_min = Umin.get(0);
    double u = Xhat.get(0);

    double upWindow = u_max - outputVoltage;
    double downWindow = u_min - outputVoltage;

    if (deltaU > upWindow) {
      deltaU = upWindow ;
    } else if (deltaU < downWindow) {
      deltaU = downWindow;
    }
    outputVoltage += deltaU;
    U.set(0, deltaU);
  }

  public void update() {
    if (!enabled) {
      return;
    }

    double curSensorVel = sensor.get();
    curVel = curSensorVel;

    this.y.set(0,0, curSensorVel); //flash(new double[]{curSensorVel});

    //r.flash(new double[]{(velGoal * (1 - A.get(1,1)))/ A.get(1,0), velGoal});
    r.set(0,0, (velGoal * (1 - A.get(1,1)))/ A.get(1,0) );
    r.set(1,0, velGoal);


    // Update state space controller
    update(r, y);

    double voltage = DriverStation.getInstance().getBatteryVoltage();
    if (voltage < 4.5)
      voltage = 12.0;

    if (velGoal < 1.0) {
      this.output.set(0.0);
      goal = curSensorVel;
    } else {
      this.output.set((outputVoltage / voltage) * (reverse ? -1.0 : 1.0));
    }

    onTarget = filter.update(onTargetRaw());
  }
  public double getVelocity() {
    return curVel;
  }

  public void setVelocityGoal(double v) {
    velGoal = v;
  }

  public void enable() {
    enabled = true;
  }

  public void disable() {
    enabled = false;
    output.set(0);
    curVel = 0;
  }

  public boolean onTargetRaw() {
    return enabled && Math.abs(Xhat.get(1) - velGoal) < targetError;
  }
  boolean onTarget = false;
  public boolean onTarget() {
    return onTarget;
  }

  public double getVelocityGoal() {
    return velGoal;
  }

  public void setGoal(double goal) {
  }

  public double getGoal() {
    return 0;
  }

  public void reset() {
    velGoal = 0;
    output.set(0);
  }
}

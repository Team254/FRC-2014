package com.team254.lib.control.impl;

import com.team254.lib.control.ControlOutput;
import com.team254.lib.control.ControlSource;
import com.team254.lib.control.Controller;
import com.team254.lib.control.PIDGains;
import com.team254.lib.util.ThrottledPrinter;
import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.parsing.IUtility;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * Controller that uses a PID control scheme.
 *
 * @author richard@team254.com (Richard Lin)
 * @author tom@team254.com (Tom Bottiglieri)
 */
public class PIDController extends Controller implements IUtility, LiveWindowSendable, NamedSendable {
  PIDGains gains;
  ControlSource source;
  ControlOutput output;
  double goal;
  double errorSum;
  double lastError;
  double lastDeltaError;
  double lastSource, lastOut;
  double minIError = 10.0;
  double onTargetError = 2.0, onTargetDeltaError = 0.05;
  ThrottledPrinter printer = new ThrottledPrinter(.1);

  public PIDController(String name, PIDGains gains, ControlSource source, ControlOutput output) {
    super(name);
    this.gains = gains;
    this.source = source;
    this.output = output;
    enabled = true;
    errorSum = 0.0;
    lastError = 0.0;
  }

  public void setGains(PIDGains gains) {
    this.gains = gains;
  }

  public void setEpsilons(double onTargetError, double onTargetDeltaError) {
    this.onTargetError = onTargetError;
    this.onTargetDeltaError = onTargetDeltaError;
  }

  public void update() {
    lastSource = source.get(); // get the source
    // perform calculations on the source
    double out = 0;
    double error = goal - lastSource;
    double p = gains.getP() * error;
    if (Math.abs(error) < minIError) {
      errorSum += error;
    }
    double i = gains.getI() * errorSum;
    double dError = error - lastError;
    double d = gains.getD() * dError;
    lastError = error;
    double ff = gains.getF() * goal;
    if (enabled) {
      out = ff + p + i + d;
      output.set(out); // set output
      lastOut = out;
    }
    lastDeltaError = dError;
  }

  public void setGoal(double goal) {
    errorSum = 0;
    this.goal = goal;
    output.set(0);
  }

  public void setGoalRaw(double goal) {
    this.goal = goal;
  }

  public double getGoal() {
    return this.goal;
  }

  void setErrorBounds(double onTargetError, double onTargetDeltaError) {
    this.onTargetDeltaError = onTargetDeltaError;
    this.onTargetError = onTargetError;
  }

  void setMinI(double minI) {
    this.minIError = minI;
  }

  private ITableListener listener = new ITableListener() {
    public void valueChanged(ITable table, String key, Object value, boolean isNew) {
      if (key.equals("p") || key.equals("i") || key.equals("d") || key.equals("f")) {
        if (gains.getP() != table.getNumber("p", 0.0) || gains.getI() != table.getNumber("i", 0.0) ||
          gains.getD() != table.getNumber("d", 0.0)) {
          System.out.println("Got new PID gains!");
          gains.set(table.getNumber("p", 0.0), table.getNumber("i", 0.0), table.getNumber("d", 0.0));
        }
      } else if (key.equals("setpoint")) {
        if (goal != ((Double) value).doubleValue()) {
          setGoal(((Double) value).doubleValue());
        }
      } else if (key.equals("enabled")) {
          if (isEnabled() != ((Boolean) value).booleanValue()) {
            if (((Boolean) value).booleanValue()) {
              enable();
            } else {
             disable();
            }
          }
      }
    }
  };

  private ITable table;
  public void initTable(ITable table) {
    if(this.table!=null) {
      this.table.removeTableListener(listener);
    }
    this.table = table;
    if(table!=null){
      table.putNumber("p", gains.getP());
      table.putNumber("i", gains.getI());
      table.putNumber("d", gains.getD());
      table.putNumber("f", gains.getF());
      table.putNumber("goal", goal);
      table.putNumber("source", source.get());
      table.putBoolean("enabled", isEnabled());
      table.addTableListener(listener, false);
    }
  }

  public ITable getTable() {
    return table;
  }

  public String getSmartDashboardType() {
    return "PIDController";
  }

  public void updateTable() {
  }

  public void startLiveWindowMode() {
    disable();
  }

  public void stopLiveWindowMode() {
  }

  public boolean onTarget() {
    boolean done = !enabled || (Math.abs(goal - lastSource) < onTargetError) &&
            (Math.abs(lastDeltaError) < onTargetDeltaError);
    if (done) {
      System.out.println(name + " DONE");
    }
    return done;
  }
}

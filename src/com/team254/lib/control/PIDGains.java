package com.team254.lib.control;

import com.team254.lib.util.ConstantsBase.Constant;

/**
 * Keeps track of the proportional, integral, and derivative
 * terms of a PID controller.
 *
 * @author richard@team254.com (Richard Lin)
 */
public class PIDGains {
  Constant kP;
  Constant kI;
  Constant kD;
  Constant kF;

  public PIDGains(Constant p, Constant i, Constant d, Constant f) {
    set(p, i, d, f);
  }

  public PIDGains(Constant p, Constant i, Constant d) {
    set(p, i, d);
  }

  public final void set(Constant p, Constant i, Constant d) {
    set(p, i, d, new Constant("nullFF", 0));
  }

  public final void set(Constant p, Constant i, Constant d, Constant f) {
    Constant nullConstant = new Constant("nullPID", 0);
    if (p != null) {
      kP = p;
    }
    else {
      kP = nullConstant;
    }

    if (i != null) {
      kI = i;
    }
    else {
      kI = nullConstant;
    }

    if (d != null) {
      kD = d;
    }
    else {
      kD = nullConstant;
    }

    if (f != null) {
      kF = f;
    }
    else {
      kF = nullConstant;
    }
  }

  public final void set(double p, double i, double d) {
    kP.setVal(p);
    kI.setVal(i);
    kD.setVal(d);
  }

  public double getP() {
    return kP.getDouble();
  }

  public double getI() {
    return kI.getDouble();
  }

  public double getD() {
    return kD.getDouble();
  }

  public double getF() {
    return kF.getDouble();
  }
}

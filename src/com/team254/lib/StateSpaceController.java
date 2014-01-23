package com.team254.lib;

import com.team254.lib.util.Matrix;


/**
 * Contains the control logic for a State Space Controller.
 * 
 * @author tom@team254.com (Tom Bottiglieri)
 * Based on work by Austin Schuh and Parker Schuh
 */
public abstract class StateSpaceController extends Controller {
  public int numInputs;
  public int numOutputs;
  public int numStates;
  boolean initialized = false;
  StateSpaceGains gains;
  public double period = 1.0/100.0;

  //the state matrices, calculated and imported from matlab
  protected Matrix A;
  protected Matrix B;
  protected Matrix C;
  protected Matrix D;
  protected Matrix L;
  public Matrix K;

  // Other state matrices
  protected Matrix X;
  public Matrix Xhat;
  protected Matrix U;
  public Matrix Uuncapped;
  protected Matrix Umin;
  protected Matrix Umax;


  public StateSpaceController(String name, int nIn, int nOut, int nStates,
          StateSpaceGains ssg, double period) {
    //super(name);
    numInputs = nIn;
    numOutputs = nOut;
    numStates = nStates;
    gains = ssg;
    this.period = period;

    // Size the matrices
    A = new Matrix(numStates, numStates);
    B = new Matrix(numStates, numOutputs);
    C = new Matrix(numOutputs, numStates);
    D = new Matrix(numOutputs, numOutputs);
    L = new Matrix(numStates, numOutputs);
    K = new Matrix(numOutputs, numStates);
    X = new Matrix(numStates, 1);
    Xhat = new Matrix(numStates, 1);
    U = new Matrix(numOutputs, 1);
    Uuncapped = new Matrix(numOutputs, 1);
    Umin = new Matrix(numOutputs, 1);
    Umax = new Matrix(numOutputs, 1);
    updateGains();

  }

  private void updateGains() {
    A.flash(gains.getA());
    B.flash(gains.getB());
    C.flash(gains.getC());
    D.flash(gains.getD());
    K.flash(gains.getK());
    L.flash(gains.getL());
    Umax.flash(gains.getUmax());
    Umin.flash(gains.getUmin());
  }
  public Matrix r1 = new Matrix(1,1);
  public void update(Matrix R, Matrix Y) {
    if (gains.updated()){
      updateGains();
    }

    r1 = Matrix.subtract(R, Xhat);
    U = Matrix.multiply(K, r1);
    Uuncapped.flash(U.getData());
    capU();

    // X_hat = (A - L * C) * X_hat + L * Y + B * U;
    Matrix b_u = Matrix.multiply(B, U);
    Matrix l_y = Matrix.multiply(L, Y);
    Matrix l_c = Matrix.multiply(L, C);
    Matrix a_lc = Matrix.subtract(A, l_c);
    Matrix alc_xhat = Matrix.multiply(a_lc, Xhat);
    Matrix xhatp1 = Matrix.add(alc_xhat, l_y);
    Xhat = Matrix.add(xhatp1, b_u);
  }

  public Matrix getXhat() {
    return Xhat;
  }

  public void capU() {
    for(int i=0; i < numOutputs; i++) {
      double u_i = U.get(i);
      double u_max = Umax.get(i);
      double u_min = Umin.get(i);
      if (u_i > u_max) {
        u_i = u_max;
      } else if (u_i < u_min) {
        u_i = u_min;
      }
      U.set(i, u_i);
    }
  }
}

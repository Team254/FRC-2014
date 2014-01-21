package com.team254.frc2014;

// Auto generated from control loops project
import com.team254.lib.StateSpaceGains;

public class ShooterGains {
  static public StateSpaceGains[] getGains() {
    return new StateSpaceGains[] {
        new StateSpaceGains(
            new double[] {1.000000, 0.000000, 1.231733, 0.977433},  // A
            new double[] {1.000000, 0.000000},  // B
            new double[] {0.000000, 1.000000},  // C
            new double[] {0.000000},  // D
            new double[] {0.040593, 0.427433},  // L
            new double[] {0.957433, 0.148079},  // K
            new double[] {12.000000},  // Umax
            new double[] {-2.000000}),  // Umin
        };
  }
}

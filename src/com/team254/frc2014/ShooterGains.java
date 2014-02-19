package com.team254.frc2014;

import com.team254.lib.StateSpaceGains;

public class ShooterGains {
  static public StateSpaceGains[] getGains() {
    return new StateSpaceGains[] {
        new StateSpaceGains(
            new double[] {1.000000, 0.000000, 0.605377, 0.988446},  // A
            new double[] {1.000000, 0.000000},  // B
            new double[] {0.000000, 1.000000},  // C
            new double[] {0.000000},  // D
            new double[] {0.082593, 0.438446},  // L
            new double[] {0.968446, 0.318496},  // K
            new double[] {12.000000},  // Umax
            new double[] {-0.5000000}),  // Umin
        };
  }
}
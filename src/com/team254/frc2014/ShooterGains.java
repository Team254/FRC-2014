package com.team254.frc2014;

import com.team254.lib.StateSpaceGains;

public class ShooterGains {
  static public StateSpaceGains[] getGains() {
    return new StateSpaceGains[] {
        new StateSpaceGains(
            new double[] {1.000000, 0.000000, 0.691279, 0.988930},  // A
            new double[] {1.000000, 0.000000},  // B
            new double[] {0.000000, 1.000000},  // C
            new double[] {0.000000},  // D
            new double[] {0.072330, 0.438930},  // L
            new double[] {0.968930, 0.279589},  // K
            new double[] {12.000000},  // Umax
            new double[] {-0.5000000}),  // Umin
        };
  }
}

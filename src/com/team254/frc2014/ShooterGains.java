package com.team254.frc2014;

import com.team254.lib.StateSpaceGains;

public class ShooterGains {
  static public StateSpaceGains[] getGains() {
    return new StateSpaceGains[] {
        new StateSpaceGains(
            new double[] {1.000000, 0.000000, 0.911103, 0.986559},  // A
            new double[] {1.000000, 0.000000},  // B
            new double[] {0.000000, 1.000000},  // C
            new double[] {0.000000},  // D
            new double[] {0.054879, 0.436559},  // L
            new double[] {0.966559, 0.209646},  // K
            new double[] {12.000000},  // Umax
            new double[] {-2.000000}),  // Umin
        };
  }
}
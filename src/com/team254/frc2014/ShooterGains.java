package com.team254.frc2014;

import com.team254.lib.StateSpaceGains;

public class ShooterGains {
  static public StateSpaceGains[] getGains() {
    return new StateSpaceGains[] {
        new StateSpaceGains(
            new double[] {1.000000, 0.000000, 0.624638, 0.987714},  // A
            new double[] {1.000000, 0.000000},  // B
            new double[] {0.000000, 1.000000},  // C
            new double[] {0.000000},  // D
            new double[] {0.080046, 0.437714},  // L
            new double[] {0.967714, 0.307554},  // K
            new double[] {12.000000},  // Umax
            new double[] {-2.000000}),  // Umin
        };
  }
}
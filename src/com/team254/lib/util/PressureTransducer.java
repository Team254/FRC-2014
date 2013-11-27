package com.team254.lib.util;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * Analog input wrapper class for the Honeywell PX2AN2XX150PAAAX pressure transducer.
 *
 * @author pat@team254.com (Patrick Fairbank)
 */
public class PressureTransducer {
  private AnalogChannel sensor;

  public PressureTransducer(int port) {
    sensor = new AnalogChannel(port);
  }

  public double getPsi() {
    // Voltage scales linearly
    // This number came from taking sensor readings and plotting data.
    return 38.823 * sensor.getVoltage() - 32.976;
  }
}

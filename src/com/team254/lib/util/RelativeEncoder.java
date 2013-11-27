package com.team254.lib.util;

import edu.wpi.first.wpilibj.Encoder;

/**
 * Allows for a encoder to be measured relative to a certain encoder point.
 *
 * @author tom@team254.com (Tom Bottiglieri)
 */
public class RelativeEncoder {
  private Encoder encoder;
  int val;

  public RelativeEncoder(Encoder e) {
    this.encoder = e;
    val = 0;
  }

  public void reset() {
    val = encoder.get();
  }

  public void resetAbsolute() {
    encoder.reset();
    reset();
  }

  public int get() {
    return encoder.get() - val;
  }

  public void start() {
    encoder.start();
  }
}

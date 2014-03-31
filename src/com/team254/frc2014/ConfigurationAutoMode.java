package com.team254.frc2014;

import com.team254.frc2014.AutoModeSelector.Configuration;

/**
 * LanedAutoMode.java
 * LanedAutoMode defines all autonomi which utilize lanes.
 * @author tombot
 */
public abstract class ConfigurationAutoMode extends AutoMode {

  public ConfigurationAutoMode(String d) {
    super(d);
  }

  protected Configuration config;

  public void setConfiguration(Configuration config) {
    this.config = config;
  }
}
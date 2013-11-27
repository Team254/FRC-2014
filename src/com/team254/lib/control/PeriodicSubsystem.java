package com.team254.lib.control;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Represents a subsystem that can be updated periodically.
 *
 * @author richard@team254.com (Richard Lin)
 */
public abstract class PeriodicSubsystem extends Subsystem implements Updatable {

  public PeriodicSubsystem() {
    ControlUpdater.getInstance().add(this);
  }

  public abstract void update();
}

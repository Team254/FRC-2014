package com.team254.lib.control;

/**
 * Interface for subsystem inputs.
 *
 * @author richard@team254.com (Richard Lin)
 */
public interface ControlSource {
  public double get();
  public void updateFilter();
  public boolean getLowerLimit();
  public boolean getUpperLimit();
}

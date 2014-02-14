package com.team254.lib.trajectory;

/**
 * Factory class for creating Trajectories.
 *
 * @author Jared341
 */
public class TrajectoryGenerator {

  ///// INNER CLASSES /////
  public static class Config {

    public double dt;
    public double max_vel;
    public double max_acc;
    public double max_jerk;
  }

  public static class Strategy {

    // J2ME Enum pattern
    private final String value_;

    private Strategy(String value) {
      value_ = value;
    }

    public String toString() {
      return value_;
    }
  }

  private static class IntegrationMethod {

    // J2ME Enum pattern
    private final String value_;

    private IntegrationMethod(String value) {
      value_ = value;
    }

    public String toString() {
      return value_;
    }
  }

  ///// CONSTANTS /////
  // Move from the start to the goal at a constant velocity.  Acceleration and
  // jerk limits are ignored, and start and goal vel are ignored (since the
  // velocity at all times will be max_vel).
  public static final Strategy StepStrategy = new Strategy("StepStrategy");

  // Move from the start to the goal with a trapezoidal speed profile.
  // Jerk limits are ignored.
  public static final Strategy TrapezoidalStrategy
          = new Strategy("TrapezoidalStrategy");

  // Move from the start tot he goal with a S-curve speed profile.  All limits
  // are obeyed, but only point-to-point moves (start_vel = goal_vel = 0) are
  // currently supported.
  public static final Strategy SCurvesStrategy
          = new Strategy("SCurvesStrategy");

  // Choose one of the above strategies based on the inputs.
  public static final Strategy AutomaticStrategy
          = new Strategy("AutomaticStrategy");

  private static final IntegrationMethod RectangularIntegration
          = new IntegrationMethod("RectangularIntegration");

  private static final IntegrationMethod TrapezoidalIntegration
          = new IntegrationMethod("TrapezoidalIntegration");

  ///// METHODS /////
  /**
   * Generate a trajectory from a start state to a goal state.
   *
   * Read the notes on each of the Strategies defined above, as certain
   * arguments are ignored for some strategies.
   *
   * @param config Definition of constraints and sampling rate (WARNING: Some
   * may be ignored)
   * @param strategy Which generator to use
   * @param start_vel The starting velocity (WARNING: May be ignored)
   * @param start_heading The starting heading
   * @param goal_pos The goal position
   * @param goal_vel The goal velocity (WARNING: May be ignored)
   * @param goal_heading The goal heading
   * @return A Trajectory that satisfies the relevant constraints and end
   * conditions.
   */
  public static Trajectory generate(
          Config config,
          Strategy strategy,
          double start_vel,
          double start_heading,
          double goal_pos,
          double goal_vel,
          double goal_heading) {
    // Choose an automatic strategy.
    if (strategy == AutomaticStrategy) {
      strategy = chooseStrategy(start_vel, goal_vel, config.max_vel);
    }

    Trajectory traj;
    if (strategy == StepStrategy) {
      double impulse = (goal_pos / config.max_vel) / config.dt;

      // Round down, meaning we may undershoot by less than max_vel*dt.
      // This is due to discretization and avoids a strange final
      // velocity.
      int time = (int) (Math.floor(impulse));
      traj = secondOrderFilter(1, 1, config.dt, config.max_vel,
              config.max_vel, impulse, time, TrapezoidalIntegration);

    } else if (strategy == TrapezoidalStrategy) {
      // How fast can we go given maximum acceleration and deceleration?
      double start_discount = .5 * start_vel * start_vel / config.max_acc;
      double end_discount = .5 * goal_vel * goal_vel / config.max_acc;

      double adjusted_max_vel = Math.min(config.max_vel,
              Math.sqrt(config.max_acc * goal_pos - start_discount
                      - end_discount));
      double t_rampup = (adjusted_max_vel - start_vel) / config.max_acc;
      double x_rampup = start_vel * t_rampup + .5 * config.max_acc
              * t_rampup * t_rampup;
      double t_rampdown = (adjusted_max_vel - goal_vel) / config.max_acc;
      double x_rampdown = adjusted_max_vel * t_rampdown - .5
              * config.max_acc * t_rampdown * t_rampdown;
      double x_cruise = goal_pos - x_rampdown - x_rampup;

      // The +.5 is to round to nearest
      int time = (int) ((t_rampup + t_rampdown + x_cruise
              / adjusted_max_vel) / config.dt + .5);

      // Compute the length of the linear filters and impulse.
      int f1_length = (int) Math.ceil((adjusted_max_vel
              / config.max_acc) / config.dt);
      double impulse = (goal_pos / adjusted_max_vel) / config.dt
              - start_vel / config.max_acc / config.dt
              + start_discount + end_discount;
      traj = secondOrderFilter(f1_length, 1, config.dt, start_vel,
              adjusted_max_vel, impulse, time, TrapezoidalIntegration);

    } else if (strategy == SCurvesStrategy) {
      // How fast can we go given maximum acceleration and deceleration?
      double adjusted_max_vel = Math.min(config.max_vel,
              (-config.max_acc * config.max_acc + Math.sqrt(config.max_acc
                      * config.max_acc * config.max_acc * config.max_acc
                      + 4 * config.max_jerk * config.max_jerk * config.max_acc
                      * goal_pos)) / (2 * config.max_jerk));

      // Compute the length of the linear filters and impulse.
      int f1_length = (int) Math.ceil((adjusted_max_vel
              / config.max_acc) / config.dt);
      int f2_length = (int) Math.ceil((config.max_acc
              / config.max_jerk) / config.dt);
      double impulse = (goal_pos / adjusted_max_vel) / config.dt;
      int time = (int) (Math.ceil(f1_length + f2_length + impulse));
      traj = secondOrderFilter(f1_length, f2_length, config.dt, 0,
              adjusted_max_vel, impulse, time, TrapezoidalIntegration);

    } else {
      return null;
    }

    // Now assign headings by interpolating along the path.
    // Don't do any wrapping because we don't know units.
    double total_heading_change = goal_heading - start_heading;
    for (int i = 0; i < traj.getNumSegments(); ++i) {
      traj.segments_[i].heading = start_heading + total_heading_change
              * (traj.segments_[i].pos)
              / traj.segments_[traj.getNumSegments() - 1].pos;
    }

    return traj;
  }

  private static Trajectory secondOrderFilter(
          int f1_length,
          int f2_length,
          double dt,
          double start_vel,
          double max_vel,
          double total_impulse,
          int length,
          IntegrationMethod integration) {
    if (length <= 0) {
      return null;
    }
    Trajectory traj = new Trajectory(length);

    Trajectory.Segment last = new Trajectory.Segment();
    // First segment is easy
    last.pos = 0;
    last.vel = start_vel;
    last.acc = 0;
    last.jerk = 0;
    last.dt = dt;

    // f2 is the average of the last f2_length samples from f1, so while we
    // can recursively compute f2's sum, we need to keep a buffer for f1.
    double[] f1 = new double[length];
    f1[0] = (start_vel / max_vel) * f1_length;
    double f2;
    for (int i = 0; i < length; ++i) {
      // Apply input
      double input = Math.min(total_impulse, 1);
      if (input < 1) {
        // The impulse is over, so decelerate
        input -= 1;
        total_impulse = 0;
      } else {
        total_impulse -= input;
      }

      // Filter through F1
      double f1_last;
      if (i > 0) {
        f1_last = f1[i - 1];
      } else {
        f1_last = f1[0];
      }
      f1[i] = Math.max(0.0, Math.min(f1_length, f1_last + input));

      f2 = 0;
      // Filter through F2
      for (int j = 0; j < f2_length; ++j) {
        if (i - j < 0) {
          break;
        }

        f2 += f1[i - j];
      }
      f2 = f2 / f1_length;

      // Velocity is the normalized sum of f2 * the max velocity
      traj.segments_[i].vel = f2 / f2_length * max_vel;

      if (integration == RectangularIntegration) {
        traj.segments_[i].pos = traj.segments_[i].vel * dt + last.pos;
      } else if (integration == TrapezoidalIntegration) {
        traj.segments_[i].pos = (last.vel
                + traj.segments_[i].vel) / 2.0 * dt + last.pos;
      }
      traj.segments_[i].x = traj.segments_[i].pos;
      traj.segments_[i].y = 0;

      // Acceleration and jerk are the differences in velocity and
      // acceleration, respectively.
      traj.segments_[i].acc = (traj.segments_[i].vel - last.vel) / dt;
      traj.segments_[i].jerk = (traj.segments_[i].acc - last.acc) / dt;
      traj.segments_[i].dt = dt;

      last = traj.segments_[i];
    }

    return traj;
  }

  public static Strategy chooseStrategy(double start_vel, double goal_vel,
          double max_vel) {
    Strategy strategy;
    if (start_vel == goal_vel && start_vel == max_vel) {
      strategy = StepStrategy;
    } else if (start_vel == goal_vel && start_vel == 0) {
      strategy = SCurvesStrategy;
    } else {
      strategy = TrapezoidalStrategy;
    }
    return strategy;
  }
}

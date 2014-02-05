/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team254.lib.trajectory;

import com.team254.lib.trajectory.Trajectory.Segment;
import com.team254.lib.util.ChezyMath;

/**
 * Generate a smooth Trajectory from a Path.
 * 
 * @author Art Kalb
 * @author Stephen Pinkerton
 * @author Jared341
 */
public class PathGenerator {
  public static Trajectory generateFromPath(Path path,
          TrajectoryGenerator.Config config) {
    if (path.getNumWaypoints() < 2) {
      return null;
    }
    
    // Compute the total length of the path by creating splines for each pair
    // of waypoints.
    Spline[] splines = new Spline[path.getNumWaypoints() - 1];
    double[] spline_lengths = new double[splines.length];
    double total_distance = 0;
    for (int i = 0; i < splines.length; ++i) {
      splines[i] = new Spline();
      if (!Spline.reticulateSplines(path.getWaypoint(i), 
              path.getWaypoint(i+1), splines[i], Spline.CubicHermite)) {
        return null;
      }
      spline_lengths[i] = splines[i].calculateLength();
      total_distance += spline_lengths[i];
    }
    
    // Generate a smooth trajectory over the total distance.
    Trajectory traj = TrajectoryGenerator.generate(config, TrajectoryGenerator.SCurvesStrategy, 0.0,
            path.getWaypoint(0).theta, total_distance, 0.0,
            path.getWaypoint(0).theta);
    
    // Assign headings based on the splines.
    int cur_spline = 0;
    double cur_spline_start_pos = 0;
    double length_of_splines_finished = 0;
    for (int i = 0; i < traj.getNumSegments(); ++i) {
      double cur_pos = traj.getSegment(i).pos;
      
      boolean found_spline = false;
      while (!found_spline) {
        double cur_pos_relative = cur_pos - cur_spline_start_pos;
        if (cur_pos_relative <= spline_lengths[cur_spline]) {
          double percentage = cur_pos_relative / spline_lengths[cur_spline];
          traj.getSegment(i).heading = splines[cur_spline].angleAt(percentage);
          traj.getSegment(i).delta_heading =
                  splines[cur_spline].angleChangeAt(percentage);
          double[] coords = splines[cur_spline].getXandY(percentage);
          traj.getSegment(i).x = coords[0];
          traj.getSegment(i).y = coords[1];
          found_spline = true;
        } else if (cur_spline < splines.length - 1) {
          length_of_splines_finished += spline_lengths[cur_spline];
          cur_spline_start_pos = length_of_splines_finished;
          ++cur_spline;
        } else {
          traj.getSegment(i).heading = splines[splines.length-1].angleAt(1.0);
          traj.getSegment(i).delta_heading = 
                  splines[splines.length-1].angleChangeAt(1.0);
          double[] coords = splines[splines.length-1].getXandY(1.0);
          traj.getSegment(i).x = coords[0];
          traj.getSegment(i).y = coords[1];
          found_spline = true;
        }
      }
    }
    
    return traj;
  }
  
  public static Trajectory[] makeLeftAndRightTrajectories(Trajectory input,
          double wheelbase_width) {
    Trajectory[] output = new Trajectory[2];
    output[0] = input.copy();
    output[1] = input.copy();
    Trajectory left = output[0];
    Trajectory right = output[1];
    
    for (int i = 0; i < input.getNumSegments(); ++i) {      
      Segment current = input.getSegment(i);
      double delta_heading = current.delta_heading;

      // TODO(Jared341): We can refactor away all of the radius based scaling
      //   and just base everything off the x, y coords computed below.
      double radius, scaling_inner, scaling_outer;
      if (Math.abs(delta_heading) > 1E-6) {
        radius = current.vel * current.dt / Math.abs(delta_heading);
        scaling_inner = (radius - wheelbase_width/2) / radius;
        scaling_outer = (radius + wheelbase_width/2) / radius;
      } else {
        scaling_inner = 1;
        scaling_outer = 1;
      }
      
      double scaling_left, scaling_right;
      if (delta_heading > 0) {
        scaling_right = scaling_outer;
        scaling_left = scaling_inner;
      } else {
        scaling_right = scaling_inner;
        scaling_left = scaling_outer;
      }
      
      double cos_angle = Math.cos(current.heading);
      double sin_angle = Math.sin(current.heading);
      
      Segment s_left = left.getSegment(i);
      s_left.x = current.x - wheelbase_width/2*sin_angle;
      s_left.y = current.y + wheelbase_width/2*cos_angle;
      if (i > 0) {
        // Get distance between current and last segment
        double dist = Math.sqrt((s_left.x-left.getSegment(i-1).x)*
                (s_left.x-left.getSegment(i-1).x) + 
                (s_left.y-left.getSegment(i-1).y)*
                        (s_left.y-left.getSegment(i-1).y));
        s_left.pos = left.getSegment(i-1).pos + dist;
        s_left.vel = dist / s_left.dt;
        s_left.acc = (s_left.vel - left.getSegment(i-1).vel) / s_left.dt;
        s_left.jerk = (s_left.acc - left.getSegment(i-1).acc) / s_left.dt;
      }

      Segment s_right = right.getSegment(i);
      s_right.x = current.x + wheelbase_width/2*sin_angle;
      s_right.y = current.y - wheelbase_width/2*cos_angle;
      if (i > 0) {
        // Get distance between current and last segment
        double dist = Math.sqrt((s_right.x-right.getSegment(i-1).x)*
                (s_right.x-right.getSegment(i-1).x) + 
                (s_right.y-right.getSegment(i-1).y)*
                        (s_right.y-right.getSegment(i-1).y));
        s_right.pos = right.getSegment(i-1).pos + dist;
        s_right.vel = dist / s_right.dt;
        s_right.acc = (s_right.vel - right.getSegment(i-1).vel) / s_right.dt;
        s_right.jerk = (s_right.acc - right.getSegment(i-1).acc) / s_right.dt;
      }
    }
    
    return output;
  }
}

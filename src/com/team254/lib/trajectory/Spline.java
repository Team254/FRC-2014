package com.team254.lib.trajectory;

import com.team254.lib.util.ChezyMath;

/**
 * Do cubic spline interpolation between points.
 * @author Art Kalb
 * @author Jared341
 */
public class Spline {
  public static class Type {
    private final String value_;
    
    private Type(String value) {
      this.value_ = value;
    }
    
    public String toString() {
      return value_;
    }
  }
  
  public static final Type CubicHermite = new Type("CubicHermite");
  
  double a_;  // ax^3
  double b_;  // + bx^2
  double c_;  // + cx
  // + d (but d is always 0 in our formulation)
  
  // The offset from the world frame to the spline frame.
  // Add these to the output of the spline to obtain world coordinates.
  double y_offset_;
  double x_offset_;
  double x_distance_;
  double theta_offset_;
  
  Spline() {
    // All splines should be made via the static interface
  }
  
  private static boolean almostEqual(double x, double y) {
    return Math.abs(x-y) < 1E-6;
  }
  
  public static boolean reticulateSplines(Path.Waypoint start,
          Path.Waypoint goal, Spline result, Type type) {
    return reticulateSplines(start.x, start.y, start.theta, goal.x, goal.y,
            goal.theta, result, type);
  }
  
  public static boolean reticulateSplines(double x0, double y0, double theta0,
          double x1, double y1, double theta1, Spline result, Type type) {
    System.out.println("Reticulating splines...");
    
    // Transform x to the origin
    result.x_offset_ = x0;
    result.y_offset_ = y0;
    double x1_hat = Math.sqrt((x1-x0)*(x1-x0) + (y1-y0)*(y1-y0));
    if (x1_hat == 0) {
      return false;
    }
    result.x_distance_ = x1_hat;
    result.theta_offset_ = ChezyMath.atan2(y1-y0, x1-x0);
    
    if (type == CubicHermite) {
      double theta0_hat = ChezyMath.getDifferenceInAngleRadians(
              result.theta_offset_, theta0);
      double theta1_hat = ChezyMath.getDifferenceInAngleRadians(
              result.theta_offset_, theta1);

      // We cannot handle vertical slopes in our rotated, translated basis.
      // This would mean the user wants to end up 90 degrees off of the straight
      // line between p0 and p1.
      if (almostEqual(Math.abs(theta0_hat), Math.PI/2) ||
              almostEqual(Math.abs(theta1_hat), Math.PI/2)) {
        return false;
      }
      // We also cannot handle the case that the end angle is facing towards the
      // start angle (total turn > 90 degrees).
      if (Math.abs(ChezyMath.getDifferenceInAngleRadians(theta0_hat, 
              theta1_hat)) 
              >= Math.PI/2) {
        return false;
      }

      // Turn angles into derivatives (slopes)
      double yp0_hat = Math.tan(theta0_hat);
      double yp1_hat = Math.tan(theta1_hat);

      // Calculate the cubic spline coefficients
      result.a_ = (yp1_hat + yp0_hat) / (x1_hat*x1_hat);
      result.b_ = -(2*yp0_hat + yp1_hat) / x1_hat;
      result.c_ = yp0_hat;
    }

    return true;
  }
  
  public double calculateLength() {
    final int kNumSamples = 1000;
    double arc_length = 0;
    for (int i = 0; i <= kNumSamples; ++i) {
      double x = ((double)i) / kNumSamples * x_distance_;
      arc_length += Math.sqrt(9*a_*a_*x*x*x*x+12*a_*b_*x*x*x + 6*a_*c_*x*x +
              4*b_*b_*x*x + 4*b_*c_*x + c_*c_+1);
    }
    arc_length /= (kNumSamples + 1);
    arc_length *= x_distance_;
    return arc_length;
  }
  
  public double[] getXandY(double percentage) {
    double[] result = new double[2];
    
    percentage = Math.max(Math.min(percentage, 1), 0);
    double x_hat = percentage*x_distance_;
    double y_hat = a_*x_hat*x_hat*x_hat + b_*x_hat*x_hat + c_*x_hat;
    
    double cos_theta = Math.cos(theta_offset_);
    double sin_theta = Math.sin(theta_offset_);
    
    result[0] = x_hat * cos_theta - y_hat * sin_theta + x_offset_;
    result[1] = x_hat * sin_theta + y_hat * cos_theta + y_offset_;
    return result;
  }
  
  public double valueAt(double percentage) {
    percentage = Math.max(Math.min(percentage, 1), 0);
    double x_hat = percentage*x_distance_;
    double y_hat = a_*x_hat*x_hat*x_hat + b_*x_hat*x_hat + c_*x_hat;
    
    double cos_theta = Math.cos(theta_offset_);
    double sin_theta = Math.sin(theta_offset_);
    
    double value = x_hat * sin_theta + y_hat * cos_theta + y_offset_;
    return value;
  }
  
  private double derivativeAt(double percentage) {
    percentage = Math.max(Math.min(percentage, 1), 0);
    
    double x_hat = percentage*x_distance_;
    double yp_hat = 3*a_*x_hat*x_hat + 2*b_*x_hat + c_;
    
    return yp_hat;
  }
  
  private double secondDerivativeAt(double percentage) {
    percentage = Math.max(Math.min(percentage, 1), 0);
    
    double x_hat = percentage*x_distance_;
    double ypp_hat = 6*a_*x_hat + 2*b_;
    
    return ypp_hat;
  }
  
  public double angleAt(double percentage) {
    double angle = ChezyMath.boundAngle0to2PiRadians(
            ChezyMath.atan(derivativeAt(percentage)) + theta_offset_);
    return angle;
  }
  
  public double angleChangeAt(double percentage) {
    return ChezyMath.boundAngleNegPiToPiRadians(
            ChezyMath.atan(secondDerivativeAt(percentage)));
  }
  
  public String toString() {
    return "a=" + a_ + "; b=" + b_ + "; c=" + c_;
  }
}

package com.team254.frc2014;

/**
 * FieldPosition.java
 * This describes a point on the field with a heading.
 * @author tombot
 */
public class FieldPosition {
  private double x, y , heading;
  private String description;
  
  public FieldPosition(double x, double y, double heading, String description) {
    this.x = x;
    this.y = y;
    this.heading = heading;
    this.description = description;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getHeading() {
    return heading;
  }
  public static final FieldPosition centeredOnLine = new FieldPosition(Constants.fieldWidth / 2.0,
          Constants.distanceToWhiteLine,
          0.0,
          "Center of field, on line.");
  public static final FieldPosition centeredOnLineOffsetByBall = new FieldPosition(Constants.fieldWidth / 2.0, 
          Constants.distanceToWhiteLine,
          0.0,
          "Center of field, 1 ball behind line.");
}

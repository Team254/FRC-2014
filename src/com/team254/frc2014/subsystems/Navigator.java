package com.team254.frc2014.subsystems;

import com.team254.lib.Loopable;
import com.team254.lib.Subsystem;
import java.util.Hashtable;

/**
 * Navigator.java
 * This controls the positioning system
 * @author Tom Bottiglieri
 */
public class Navigator extends Subsystem implements Loopable {

  double x = 0, y = 0, heading = 0;
  double lastL, lastR;
  double gyroReference = 0;
  Drivebase drive;
  public Navigator(Drivebase drive) {
    super("Navigator");
    this.drive = drive;
  }

  public void resetWithReferenceHeading(double reference) {
    x = y = heading = 0;
    gyroReference = reference;
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

  private double degreesToRadians(double deg) {
    return (deg * Math.PI) / 180.0;
  }

  public void update(double left, double right, double gyroAngle) {
    double diffL = left - lastL;
    double diffR = right - lastR;
    lastL = left;
    lastR = right;
    heading = gyroAngle - gyroReference;
    double magnitude = (diffL + diffR) / 2.0;
    x += magnitude * Math.sin(degreesToRadians(heading));
    y += magnitude * Math.cos(degreesToRadians(heading));
  }

  public String toString() {
    return "X: " + x + " Y: " + y + " Heading: " + heading;
  }

  public Hashtable serialize() {
    return new Hashtable();
  }

  public void update() {
  }
}

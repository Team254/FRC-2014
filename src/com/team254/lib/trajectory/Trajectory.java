package com.team254.lib.trajectory;

import com.team254.lib.util.ChezyMath;

/**
 * Implementation of a Trajectory using arrays as the underlying storage
 * mechanism.
 *
 * @author Jared341
 */
public class Trajectory {
  
  public static class Pair {
    public Pair(Trajectory left, Trajectory right) {
      this.left = left;
      this.right = right;
    }

    public Trajectory left;
    public Trajectory right;
  }

  public static class Segment {

    public double pos, vel, acc, jerk, heading, dt, x, y;

    public Segment() {
    }

    public Segment(double pos, double vel, double acc, double jerk,
            double heading, double dt, double x, double y) {
      this.pos = pos;
      this.vel = vel;
      this.acc = acc;
      this.jerk = jerk;
      this.heading = heading;
      this.dt = dt;
      this.x = x;
      this.y = y;
    }

    public Segment(Segment to_copy) {
      pos = to_copy.pos;
      vel = to_copy.vel;
      acc = to_copy.acc;
      jerk = to_copy.jerk;
      heading = to_copy.heading;
      dt = to_copy.dt;
      x = to_copy.x;
      y = to_copy.y;
    }

    public String toString() {
      return "pos: " + pos + "; vel: " + vel + "; acc: " + acc + "; jerk: "
              + jerk + "; heading: " + heading;
    }
  }

  Segment[] segments_ = null;
  boolean inverted_y_ = false;

  public Trajectory(int length) {
    segments_ = new Segment[length];
    for (int i = 0; i < length; ++i) {
      segments_[i] = new Segment();
    }
  }
  
  public Trajectory(Segment[] segments) {
    segments_ = segments;
  }
  
  public void setInvertedY(boolean inverted) {
    inverted_y_ = inverted;
  }

  public int getNumSegments() {
    return segments_.length;
  }

  public Segment getSegment(int index) {
    if (index < getNumSegments()) {
      if (!inverted_y_) {
        return segments_[index];
      } else {
        Segment segment = new Segment(segments_[index]);
        segment.y *= -1.0;
        segment.heading *= -1.0;
        return segment;
      }
    } else {
      return new Segment();
    }
  }
  
  public void setSegment(int index, Segment segment) {
    if (index < getNumSegments()) {
      segments_[index] = segment;
    }
  }

  public void scale(double scaling_factor) {
    for (int i = 0; i < getNumSegments(); ++i) {
      segments_[i].pos *= scaling_factor;
      segments_[i].vel *= scaling_factor;
      segments_[i].acc *= scaling_factor;
      segments_[i].jerk *= scaling_factor;
    }
  }

  public void append(Trajectory to_append) {
    Segment[] temp = new Segment[getNumSegments()
            + to_append.getNumSegments()];

    for (int i = 0; i < getNumSegments(); ++i) {
      temp[i] = new Segment(segments_[i]);
    }
    for (int i = 0; i < to_append.getNumSegments(); ++i) {
      temp[i + getNumSegments()] = new Segment(to_append.getSegment(i));
    }

    this.segments_ = temp;
  }

  public Trajectory copy() {
    Trajectory cloned
            = new Trajectory(getNumSegments());
    cloned.segments_ = copySegments(this.segments_);
    return cloned;
  }
  
  private Segment[] copySegments(Segment[] tocopy) {
    Segment[] copied = new Segment[tocopy.length];
    for (int i = 0; i < tocopy.length; ++i) {
      copied[i] = new Segment(tocopy[i]);
    }
    return copied;
  }

  public String toString() {
    String str = "Segment\tPos\tVel\tAcc\tJerk\tHeading\n";
    for (int i = 0; i < getNumSegments(); ++i) {
      Trajectory.Segment segment = getSegment(i);
      str += i + "\t";
      str += segment.pos + "\t";
      str += segment.vel + "\t";
      str += segment.acc + "\t";
      str += segment.jerk + "\t";
      str += segment.heading + "\t";
      str += "\n";
    }

    return str;
  }

  public String toStringProfile() {
    return toString();
  }

  public String toStringEuclidean() {
    String str = "Segment\tx\ty\tHeading\n";
    for (int i = 0; i < getNumSegments(); ++i) {
      Trajectory.Segment segment = getSegment(i);
      str += i + "\t";
      str += segment.x + "\t";
      str += segment.y + "\t";
      str += segment.heading + "\t";
      str += "\n";
    }

    return str;
  }
}

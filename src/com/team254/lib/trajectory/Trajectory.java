package com.team254.lib.trajectory;

/**
 * Implementation of a Trajectory using arrays as the underlying storage
 * mechanism.
 *
 * @author Jared341
 */
public class Trajectory {

  public static class Segment {

    public double pos, vel, acc, jerk, heading, delta_heading, dt, x, y;

    public Segment() {
    }

    public Segment(Segment to_copy) {
      pos = to_copy.pos;
      vel = to_copy.vel;
      acc = to_copy.acc;
      jerk = to_copy.jerk;
      heading = to_copy.heading;
      delta_heading = to_copy.delta_heading;
      dt = to_copy.dt;
      x = to_copy.x;
      y = to_copy.y;
    }

    public String toString() {
      return "pos: " + pos + "; vel: " + vel + "; acc: " + acc + "; jerk: "
              + jerk + "; heading: " + heading + "; delta_heading: " + 
              delta_heading;
    }
  }

  Segment[] segments_ = null;

  Trajectory(int length) {
    segments_ = new Segment[length];
    for (int i = 0; i < length; ++i) {
      segments_[i] = new Segment();
    }
  }

  public int getNumSegments() {
    return segments_.length;
  }

  public Segment getSegment(int index) {
    if (index < getNumSegments()) {
      return segments_[index];
    } else {
      return new Segment();
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
    for (int i = 0; i < getNumSegments(); ++i) {
      cloned.segments_[i] = new Segment(segments_[i]);
    }
    return cloned;
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
      str += segment.delta_heading + "\t";
      str += "\n";
    }

    return str;
  }
  
  public String toStringProfile() {
    return toString();
  }
  
  public void print() {
    for (int i = 0; i < getNumSegments(); ++i) {
      Trajectory.Segment segment = getSegment(i);
      String str = "";
      str += i + ", ";
      str += segment.pos + ", ";
      str += segment.vel + ", ";
      str += segment.acc + ", ";
      str += segment.jerk + ", ";
      str += segment.heading + ", ";
      str += segment.delta_heading;
      System.out.println(str);
 
    }
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
  
  public void flip() {
    for (int i = 0; i < getNumSegments(); ++i) {
      Trajectory.Segment segment = getSegment(i);
      segment.heading = segment.heading * -1.0;
      segment.delta_heading = segment.delta_heading * -1.0;
    }
  }
}

package com.team254.lib.util;

import java.util.Vector;

/**
 * Map that stores keys and values.
 *
 * @author tom@team254.com (Tom Bottiglieri)
 */
public class Interpolator {

  Vector points = new Vector();

  private class Node {

    public double key;
    public double value;

    public Node(double key, double value) {
      this.key = key;
      this.value = value;
    }
  }

  private Node getFloor(double k) {
    for (int i = 0; i < points.size(); ++i) {
      Node c = (Node) points.elementAt(i);
      if (c.key > k) {
        return ((Node) points.elementAt(i - 1 < 0 ? 0 : i - 1));
      }
    }
    Node first = ((Node) points.firstElement());
    return (first == null) ? new Node(0, 0) : first;
  }

  private Node getCeiling(double k) {
    for (int i = 0; i < points.size(); ++i) {
      Node c = (Node) points.elementAt(i);
      if (c.key > k) {
        return ((Node) points.elementAt(i));
      }
    }
    Node last = ((Node) points.lastElement());
    return (last == null) ? new Node(0, 0) : last;
  }

  public void add(double key, double value) {
    Node n = new Node(key, value);
    if (points.isEmpty()) {
      points.addElement(n);
      return;
    }
    for (int i = 0; i < points.size(); ++i) {
      Node c = (Node) points.elementAt(i);
      if (c.key > key) {
        points.insertElementAt(n, i);
        return;
      }
    }
    points.addElement(n);
  }

  public double get(double k) {
    Node floor = getFloor(k);
    Node ceiling = getCeiling(k);
    if (ceiling.equals(floor)) {
      return floor.value;
    }
    double val = (((floor.value - ceiling.value) / (floor.key - ceiling.key)) *
            (k - floor.key)) + floor.value;
    return val;
  }

  public void clear() {
    points.removeAllElements();
  }
}

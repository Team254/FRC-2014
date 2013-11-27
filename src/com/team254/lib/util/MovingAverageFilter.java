package com.team254.lib.util;

/**
 * Filter that returns the average of the inputs as an output.
 *
 * @author richard@team254.com (Richard Lin)
 */
public class MovingAverageFilter implements Filter {
  private double[] inputs;
  private int index;

  public MovingAverageFilter(int size) {
    inputs = new double[size];
    index = 0;
  }

  public double calculate(double input) {
    index++;
    if(index == inputs.length) {
      index = 0;
    }
    inputs[index] = input;
    double sum = 0;
    for(int i = 0; i < inputs.length; i++) {
      sum += inputs[i];
    }
    return sum / (inputs.length * 1.0);
  }
}

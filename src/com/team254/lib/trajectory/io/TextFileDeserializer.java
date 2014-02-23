package com.team254.lib.trajectory.io;

import com.team254.lib.trajectory.Path;
import com.sun.squawk.util.StringTokenizer;
import com.team254.lib.trajectory.Trajectory;

/**
 *
 * @author Jared341
 */
public class TextFileDeserializer implements IPathDeserializer {

  public Path deserialize(String serialized) {
    StringTokenizer tokenizer = new StringTokenizer(serialized, "\n");
    
    String name = tokenizer.nextToken();
    int num_elements = Integer.parseInt(tokenizer.nextToken());
    
    Trajectory left = new Trajectory(num_elements);
    for (int i = 0; i < num_elements; ++i) {
      Trajectory.Segment segment = new Trajectory.Segment();
      StringTokenizer line_tokenizer = new StringTokenizer(
              tokenizer.nextToken(), " ");
      
      segment.pos = Double.parseDouble(line_tokenizer.nextToken());
      segment.vel = Double.parseDouble(line_tokenizer.nextToken());
      segment.acc = Double.parseDouble(line_tokenizer.nextToken());
      segment.jerk = Double.parseDouble(line_tokenizer.nextToken());
      segment.heading = Double.parseDouble(line_tokenizer.nextToken());
      segment.dt = Double.parseDouble(line_tokenizer.nextToken());
      segment.x = Double.parseDouble(line_tokenizer.nextToken());
      segment.y = Double.parseDouble(line_tokenizer.nextToken());
      
      left.setSegment(i, segment);
    }
    Trajectory right = new Trajectory(num_elements);
    for (int i = 0; i < num_elements; ++i) {
      Trajectory.Segment segment = new Trajectory.Segment();
      StringTokenizer line_tokenizer = new StringTokenizer(
              tokenizer.nextToken(), " ");
      
      segment.pos = Double.parseDouble(line_tokenizer.nextToken());
      segment.vel = Double.parseDouble(line_tokenizer.nextToken());
      segment.acc = Double.parseDouble(line_tokenizer.nextToken());
      segment.jerk = Double.parseDouble(line_tokenizer.nextToken());
      segment.heading = Double.parseDouble(line_tokenizer.nextToken());
      segment.dt = Double.parseDouble(line_tokenizer.nextToken());
      segment.x = Double.parseDouble(line_tokenizer.nextToken());
      segment.y = Double.parseDouble(line_tokenizer.nextToken());
      
      left.setSegment(i, segment);
    }
    
    return new Path(name, new Trajectory.Pair(left, right));
  }
  
}

package com.team254.frc2014.paths;

import com.team254.lib.TextFileReader;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.io.TextFileDeserializer;
import java.util.Hashtable;

/**
 * Load all autonomous mode paths.
 * 
 * @author Jared341
 * @author Stephen Pinkerton
 */
public class AutoPaths {
  static String[] kPathNames = {"CenterLanePath", "StraightAheadPath"};
  
  static Hashtable paths_ = new Hashtable();
  
  public static void loadPaths() {
    TextFileDeserializer deserializer = new TextFileDeserializer();
    for (int i = 0; i < kPathNames.length; ++i) {
      
      TextFileReader reader = new TextFileReader("file://" + kPathNames[i] + 
              ".txt");
      
      Path path = deserializer.deserialize(reader.readWholeFile());
      paths_.put(kPathNames[i], path);
    }
  }
  
  public static Path get(String name) {
    return (Path)paths_.get(name);
  }
}

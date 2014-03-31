package com.team254.frc2014.paths;

import com.team254.lib.TextFileReader;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.io.TextFileDeserializer;
import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;

/**
 * Load all autonomous mode paths.
 * 
 * @author Jared341
 * @author Stephen Pinkerton
 */
public class AutoPaths {
  // Make sure these match up!
  public final static String[] kPathNames = { "InsideLanePathFar",
                                              "InsideLanePathClose", 
                                              "CenterLanePathFar",
                                              "StraightAheadPath",
                                              "WallLanePath"};
  public final static String[] kPathDescriptions = { "Inside Lane, Far", 
                                                     "Inside Lane, Close",
                                                     "Middle Lane",
                                                     "Straight ahead",
                                                     "Wall Lane"};
  static Hashtable paths_ = new Hashtable();
  
  public static void loadPaths() {
    Timer t = new Timer();
    t.start();
    TextFileDeserializer deserializer = new TextFileDeserializer();
    for (int i = 0; i < kPathNames.length; ++i) {
      
      TextFileReader reader = new TextFileReader("file://" + kPathNames[i] + 
              ".txt");
      
      Path path = deserializer.deserialize(reader.readWholeFile());
      paths_.put(kPathNames[i], path);
    }
    System.out.println("Parsing paths took: " + t.get());
  }
  
  public static Path get(String name) {
    return (Path)paths_.get(name);
  }
  
  public static Path getByIndex(int index) {
    return (Path)paths_.get(kPathNames[index]);
  }
}

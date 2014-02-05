package com.team254.frc2014.path;

import com.team254.lib.trajectory.Path;
import java.util.Hashtable;

/**
 * PathFactory.java
 *
 * @author tombot
 */
public class AutoPathFactory {
  private static Hashtable paths = new Hashtable();
  
  public static AutoPath get(Class pathClass) {
    AutoPath path = null;
    if (!paths.containsKey(pathClass)) {
      try {
        path = (AutoPath) pathClass.newInstance();
        path.generatePaths();
      } catch (InstantiationException ex) {
        ex.printStackTrace();
      } catch (IllegalAccessException ex) {
        ex.printStackTrace();
      }
      paths.put(pathClass, path);
    } else {
      path = (AutoPath) paths.get(pathClass);
    }
    return path;
  }
}

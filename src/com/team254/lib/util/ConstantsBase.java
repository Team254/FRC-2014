package com.team254.lib.util;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.Connector;

/**
 * Manages constant values used everywhere in the robot code.
 *
 * @author brandon.gonzalez.451@gmail.com (Brandon Gonzalez)
 */
public abstract class ConstantsBase {
  private static final Vector constants = new Vector();
  private static final String CONSTANTS_FILE_PATH = "Constants.txt";

   /**
   * Reads the constants file and overrides the values in this class for any constants it contains.
   */
  public static void readConstantsFromFile() {
    DataInputStream constantsStream;
    FileConnection constantsFile;
    byte[] buffer = new byte[255];
    String content = "";

    try {
      // Read everything from the file into one string.
      constantsFile = (FileConnection)Connector.open("file:///" + CONSTANTS_FILE_PATH,
                                                     Connector.READ);
      constantsStream = constantsFile.openDataInputStream();
      while (constantsStream.read(buffer) != -1) {
        content += new String(buffer);
      }
      constantsStream.close();
      constantsFile.close();

      // Extract each line separately.
      String[] lines = Util.split(content, "\n");
      for (int i = 0; i < lines.length; i++) {
        // Extract the key and value.
        String[] line = Util.split(lines[i], "=");
        if (line.length != 2) {
          System.out.println("Error: invalid constants file line: " +
                            (lines[i].length() == 0 ? "(empty line)" : lines[i]));
          continue;
        }

        boolean found = false;
        // Search through the constants until we find one with the same name.
        for (int j = 0; j < constants.size(); j++) {
          Constant constant = (Constant)constants.elementAt(j);
          if (constant.getName().compareTo(line[0]) == 0) {
            System.out.println("Setting " + constant.getName() + " to " + Double.parseDouble(line[1]));
            constant.setVal(Double.parseDouble(line[1]));
            found = true;
            break;
          }
        }

        if (!found)
          System.out.println("Error: the constant doesn't exist: " + lines[i]);
      }
    } catch (IOException e) {
      System.out.println("Constants.txt not found. Not overriding constants.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


 /**
  * Handles an individual value used in the Constants class.
  */
  public static class Constant {
    private String name;
    private double value;

    public Constant(String name, double value) {
      this.name = name;
      this.value = value;
      constants.addElement(this);
    }

    public String getName(){
      return name;
    }

    public double getDouble() {
      return value;
    }

    public int getInt() {
      return (int)value;
    }

    public void setVal(double value){
      this.value = value;
    }

    public String toString(){
      return name + ": " + value;
    }
  }
}

package com.team254.lib.util;

import java.io.IOException;
import java.util.Vector;

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
    try {
      String file = Util.getFile(CONSTANTS_FILE_PATH);
      if (file.length() < 1) {
        throw new IOException("Not over riding constants");
      }
      // Extract each line separately.
      String[] lines = Util.split(file, "\n");
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
    } catch(IOException e ) {
      System.out.println(e);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static String generateHtmlSwagger() {
    String str = "<html><form method=\"post\">";
    for(int i = 0; i < constants.size(); ++i) {
      str+= ((Constant) constants.elementAt(i)).toHtml();
    }
    str += "<input type=\"submit\" value=\"Submit\">";
    str += "</form>";
    str +=  "</html>";
    return str;
  }
  
  public static void writeConstant(String name, double value) {
    Constant constant;
    for (int i = 0; i < constants.size(); i++) {
      constant = ((Constant) constants.elementAt(i));
      if (constant.name.equals(name)) {
        constant.setVal(value);
      }
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
    public String toHtml() {
      String str = "<html>" +
              this.name + ": " 
              + "<input type='text' value=\""+this.value+"\" name=\"" + this.name
              + "\"> <br/>";
      
      return str;
    }

    public String toString(){
      return name + ": " + value;
    }
  }
}

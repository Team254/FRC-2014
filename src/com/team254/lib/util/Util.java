package com.team254.lib.util;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.Connector;

/**
 * Contains basic functions that are used often.
 *
 * @author richard@team254.com (Richard Lin)
 * @author brandon.gonzalez.451@gmail.com (Brandon Gonzalez)
 */
public class Util {
  // Prevent this class from being instantiated.
  private Util() {
  }

  /**
   * Limits the given input to the given magnitude.
   */
  public static double limit(double v, double limit) {
    return (Math.abs(v) < limit) ? v : limit * (v < 0 ? -1 : 1);
  }
  
  public static String getFile(String fileName) {
    DataInputStream constantsStream;
    FileConnection constantsFile;
    byte[] buffer = new byte[255];
    String content = "";
    try {
      constantsFile = (FileConnection)Connector.open("file:///" + fileName,
                                                      Connector.READ);
      constantsStream = constantsFile.openDataInputStream();
      while (constantsStream.read(buffer) != -1) {
        content += new String(buffer);
      }
      constantsStream.close();
      constantsFile.close();
      
    } catch (IOException e) {
      System.out.println(fileName + " was not found!");
    }
      return content;
  }

  /**
   * Returns the array of substrings obtained by dividing the given input string at each occurrence
   * of the given delimiter.
   */
  public static String[] split(String input, String delimiter) {
    Vector node = new Vector();
    int index = input.indexOf(delimiter);
    while (index >= 0) {
      node.addElement(input.substring(0, index));
      input = input.substring(index + delimiter.length());
      index = input.indexOf(delimiter);
    }
    node.addElement(input);

    String[] retString = new String[node.size()];
    for (int i = 0; i < node.size(); ++i) {
      retString[i] = (String) node.elementAt(i);
    }

    return retString;
  }
}

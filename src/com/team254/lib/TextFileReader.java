package com.team254.lib;

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.microedition.io.Connector;

/**
 * Read a text file into a string.
 *
 * @author Jared341
 */
public class TextFileReader {

  private FileConnection file_connection_ = null;
  private BufferedReader reader_ = null;

  public TextFileReader(String uri) {
    try {
      // Open the new file
      file_connection_ = (FileConnection) Connector.open(uri);
      if (!file_connection_.exists()) {
        System.err.println("Could not find specified file!");
        return;
      }

      // Make an I/O adapter sandwich to actually get some text out
      reader_ = new BufferedReader(
              new InputStreamReader(file_connection_.openInputStream()));
      
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("Could not open file connection!");
      closeFile();
    }
  }

  private void closeFile() {
    try {
      // If we have a file open, close it
      if (file_connection_ != null) {
        if (reader_ != null) {
          reader_.close();
        }
        if (file_connection_.isOpen()) {
          file_connection_.close();
        }
      }
    } catch (IOException e) {
      System.err.println("Could not close file");
    }
  }

  // Returns null at end of file
  public String readLine() {
    String line = null;
    try {
      line = reader_.readLine();
    } catch (IOException e) {
      e.printStackTrace();
      closeFile();
    }
    return line;
  }
  
  public String readWholeFile() {
    StringBuffer buffer = new StringBuffer();
    String line;
    while ((line = readLine()) != null) {
      buffer.append(line);
      buffer.append("\n");
    }
    return buffer.toString();
  }
}

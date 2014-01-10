package com.team254.lib.util;

import com.team254.lib.Subsystem;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

public class HtmlResponse {
  static String OK = "HTTP/1.x 200 OK\n\n";
  static String PAGE_NOT_FOUND = "HTTP/1.x 404 Not Found\n\n ";
  static String ERROR = "HTTP/1.x 400 Bad Request\n\n";
  static Hashtable subsystems;

  public static void route(String req, OutputStream os) {

    int end = req.indexOf('\n');
    end = end > 0 ? end : req.length();

    String header = req.substring(0, end);
    String[] reqParams = Util.split(header, " ");
    String type = reqParams[0];
    String path = reqParams[1];
    try {
      if (type.equals("GET")) {
        if (path.startsWith("/subsystem")) {
          String subsystem = Util.split(Util.split(reqParams[1], "?")[1], "=")[1];
          os.write(createResponse(getSubystem(subsystem)).getBytes());

        } else if (path.startsWith("/state")) {
          // Implement state grabber
        } else {
          // Returns a file
          os.write(Util.getFile(path).getBytes());
        }
      } else if(type.equals("POST")) {
        // TODO: Implement parsing and what not
      } else {
        os.write(ERROR.getBytes());
      }
    } catch (IOException e) {
      System.out.println("Exception was caught !!!" + e);
    }
  }

  public static String createResponse(String res) {
    return OK + res;
  }
  public static void addSubsystem(String name, Subsystem system) {
    subsystems.put(name, system);
  }
  public static String getSubystem(String name) {
    if(subsystems.containsKey(name)) {
      return ((Subsystem) subsystems.get(name)).serialize();
    } else {
      return null;
    }
  }
  
  public static String test() {
    return OK + "<h1> Test.html page </h1>";
  }
}

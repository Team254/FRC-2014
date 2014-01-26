package com.team254.lib;
/*
 * @author bg
 */

import com.team254.frc2014.Constants;
import com.team254.lib.util.ConstantsBase;
import com.team254.lib.util.HtmlResponse;
import com.team254.lib.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;

public class Server implements Runnable {

  private final int PORT;
  private static Hashtable subsystems;
  private Vector connections;

  public void pushData(String message) {
    for (int i = 0; i < connections.size(); ++i) {
      try {
        OutputStream os = ((SocketConnection) connections.elementAt(i)).openOutputStream();
        os.write(message.getBytes());
        //System.out.println("pushing data");
        os.close();
      } catch (IOException e) {
        connections.removeElementAt(i);
        System.out.println(e);
      }
    }
  }

  /* 
   * Spawns a new thread to handle requests and responses 
   */
  private class ConnectionHandler implements Runnable {

    SocketConnection connection;

    public ConnectionHandler(SocketConnection c) {
      connection = c;
    }

    public void run() {
      try {
        InputStream is = connection.openInputStream();
        //System.out.println("New connection from: " + connection.getAddress());

        int ch = 0;
        byte[] b = new byte[2048];
        String req = "";
        String x;
        while (is.available() > 0) {
          int read = is.read(b);
          req += new String(b);
        }
        //System.out.println("req: " + req);
        OutputStream os = connection.openOutputStream();

        route(req, os);

        os.close();
        is.close();
        connection.close();

      } catch (IOException e) {
      }
    }
  }

  public void run() {
    ServerSocketConnection s = null;
    try {
      s = (ServerSocketConnection) Connector.open("serversocket://:" + PORT);
      Vector threads = new Vector();
      while (true) {
        SocketConnection connection = (SocketConnection) s.acceptAndOpen();
        Thread t = new Thread(new ConnectionHandler(connection));
        t.start();
        connections.addElement(connection);
      }
    } catch (IOException e) {
      System.out.println("There was an error el oh el" + e);
    }
  }

  public Server() {
    PORT = 41234;
    connections = new Vector();
  }

  public Server(int port) {
    PORT = port;
    connections = new Vector();
  }

  public static void route(String req, OutputStream os) {

    int end = req.indexOf('\n');
    end = end > 0 ? end : req.length();
    
    String header = req.substring(0, end);
    String[] reqParams = Util.split(header, " ");
    if (reqParams.length < 2) {
      return;
    }
    String type = reqParams[0];
    String path = reqParams[1];
    try {
      if (type.equals("GET")) {
        if (path.startsWith("/constants")) {
          HtmlResponse res = new HtmlResponse(Constants.generateHtmlSwagger());
          os.write(res.toString().getBytes());
        } else if (path.startsWith("/subsystem")) {
          String subsystem = Util.split(Util.split(reqParams[1], "?")[1], "=")[1];
          os.write(getSubsystemResponse(subsystem).toString().getBytes());
        } else if (path.startsWith("/state")) {
          // Implement state grabber
        } else if (path.equals("/")) {
          os.write(Util.getFile("/www/index.html").getBytes());
        } else {
          // Returns a file
          HtmlResponse res = new HtmlResponse(Util.getFile("/www" + path));
          os.write(res.toString().getBytes());
        }
      } else if (type.equals("POST")) {
        if (path.startsWith("/constants")) {
          String[] reqLines = Util.split(req, "\n");
          System.out.println("reqLines length = " + reqLines.length);
          String postLine = null;
          for (int i = 0; i < reqLines.length-1; i++) {
            if (reqLines[i].trim().equals("")) {
              postLine = reqLines[i+1];
              break;
            } 
          }
          System.out.println("postLine = " + postLine);
          String[] args = Util.split(postLine, "&");
          String firstKey = postLine.substring(0, postLine.indexOf("="));
          System.out.println(firstKey + " " + postLine.substring(postLine.indexOf("=")+1, postLine.indexOf("&")));
          double firstVal = Double.parseDouble(postLine.substring(postLine.indexOf("=")+1, postLine.indexOf("&")));
          ConstantsBase.writeConstant(firstKey, firstVal);
          for (int i = 0; i < args.length; i++) {
            String[] arg = Util.split(args[i], "=");
            //System.out.println(arg[0] + " => " + arg[1]);
            try {
              ConstantsBase.writeConstant(arg[0], Double.parseDouble(arg[1]));
            } catch (Exception e) {
              System.out.println("Cast exception for: " + arg[0] + ", " + arg[1]);
            }
          }
          os.write("done. should redirect".getBytes());
        }
      } else {
        os.write(HtmlResponse.ERROR.getBytes());
      }
    } catch (IOException e) {
      System.out.println("Exception was caught: " + e.toString());
    }
  }

   public static HtmlResponse getSubsystemResponse(String n) {
     Subsystem s = SubsystemLister.getSubsystemLister().get(n);
     if (s == null) {
       return HtmlResponse.createError("No subsystem with name " + n);
     }
     return new HtmlResponse(Util.toJson(s.serialize()));
   }

}

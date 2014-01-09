package com.team254.lib;
/*
 * @author bg
 */
import com.team254.lib.util.HtmlResponse;
import com.team254.lib.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;

public class Server implements Runnable {

  private final int PORT;
  private Vector subsystems;
  private Vector connections;

  public void pushData(String message) {
    for (int i = 0; i < connections.size(); ++i) {
      try {
        OutputStream os = ((SocketConnection) connections.elementAt(i)).openOutputStream();
        os.write(message.getBytes());
        os.close();
      } catch (IOException e) {
        connections.removeElementAt(i);
        System.out.println(e);
      }
    }
  }
  
  public void pushAllSubsystems() {
    //Prints out the serilized data of all subsystems/sensors
    for(int i = 0; i< subsystems.size(); ++i) {
      Subsystem s = (Subsystem) subsystems.elementAt(i);
      pushData(s.serialize());
    }
  }
  
  public void addSubsystem(Subsystem s)
  {
    subsystems.addElement(s);
    System.out.println("System added!");
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
        System.out.println("New connection from: " + connection.getAddress());

        int ch = 0;
        byte[] b = new byte[2048];
        String req = "";
        String x;
        while(is.available() > 0) {
          int read = is.read(b);
          req += new String(b);
        }
        
        int end = req.indexOf('\n');
        end = end > 0 ? end : req.length();
        String line = req.substring(0, end);
        String[] reqParams = Util.split(line, " ");
        
        if(reqParams.length > 1 && reqParams[1].equals("/test.html")) {
          x = HtmlResponse.test();
        } else if(reqParams[1].equals("/drivebase")) {
          x = HtmlResponse.drivebase();
          
        } else
          x = HtmlResponse.createResponse("<h1> not hello there</h1>");
        

        OutputStream os = connection.openOutputStream();
        
        os.write(x.getBytes());
        os.close();
        is.close();
        connection.close();
                System.out.println("req: " + req);
      } catch(IOException e) {}
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
}

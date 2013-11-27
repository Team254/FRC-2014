package com.team254.lib.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;

/**
 * TCP witchcraft.
 * cRIO JVM no gusta UDP.
 *
 * @author richard@team254.com (Richard Lin)
 */
public class PIDTuner implements Runnable {
  private static PIDTuner instance = null;
  private final int PORT_NUMBER = 41234;
  private Vector connections = new Vector();
  Thread thread;
  boolean running = false;

  public void start() {
    if (thread == null) {
      running = true;
      thread = new Thread(this);
      thread.start();
    }
  }

  public static PIDTuner getInstance() {
    if (instance == null) {
      instance = new PIDTuner();
    }
    return instance;
  }

  synchronized public void pushData(double setpoint, double value, double control) {
    String msg = "{\"S\":" + setpoint + ", \"V\":"  + value + ", \"C\":" + control + "}";
    for (int i = 0; i < connections.size(); ++i) {
      Connection c = (Connection) connections.elementAt(i);
      try {
        c.sendData(msg);
      } catch (IOException ex) {
        ex.printStackTrace();
        connections.removeElementAt(i);
      }
    }
  }

  public void run() {
    ServerSocketConnection socket = null;

    try {
      socket = (ServerSocketConnection) Connector.open("serversocket://:" + PORT_NUMBER);
      while(true) {
        SocketConnection socketConnect = (SocketConnection) socket.acceptAndOpen();
        System.out.println("Got a new socket connection");
        Connection c = new Connection(socketConnect);
        connections.addElement(c);
      }
    } catch (IOException e) {
       System.out.println("ERROR: " + e.getMessage());
    }
  }

  private class Connection {
    private SocketConnection client;
    OutputStream output = null;

    public Connection(SocketConnection client) {
      this.client = client;
      try {
        output = client.openOutputStream();
        System.out.println(output);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

    synchronized public void sendData(String data) throws IOException  {
      if (output == null) {
        System.out.println("null");
        return;
      }
      System.out.println(data);
      output.write(data.getBytes());
      output.flush();
    }
  }
}

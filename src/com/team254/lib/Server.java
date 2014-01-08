package com.team254.lib;
/*
 * @author bg
 */
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;

public class Server implements Runnable {

  private final int PORT;
  private static Vector subsystems;
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
  
  public static void addSubsystem(Subsystem s)
  {
    subsystems.addElement(s);
  }
  
  public void run() {
    ServerSocketConnection s = null;
    try {
      s = (ServerSocketConnection) Connector.open("serversocket://:" + PORT);
      while (true) {
        SocketConnection connection = (SocketConnection) s.acceptAndOpen();
        System.out.println("New connection from: " + connection.getAddress());
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

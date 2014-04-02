package com.team254.frc2014.hotgoal;
/*
 * @author tombot
 */

import com.team254.frc2014.ChezyRobot;
import edu.wpi.first.wpilibj.Timer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;

public class VisionHotGoalDetector implements Runnable, HotGoalDetector {

  private final int listenPort;
  private static Hashtable subsystems;
  private Vector connections;
  private boolean voting = false;
  private int leftVotes = 0, rightVotes = 0, totalVotes = 0;
  double lastHeartbeatTime = -1;
  
  public boolean hasClientConnection() {
    return lastHeartbeatTime > 0 && (Timer.getFPGATimestamp() - lastHeartbeatTime) < 3.0; 
  }

  private void vote(boolean left, boolean right) {
    if (voting) {
      leftVotes += left ? 1 : 0;
      rightVotes += right ? 1 : 0;
      totalVotes++;
    }
  }

  public boolean probablySawGoalChange() {
    return Math.abs(leftVotes - rightVotes) > 3;
  }

  public void startSampling() {
    voting = true;
  }

  public void stopSampling() {
    voting = false;
  }

  public boolean getNotSure() {
    return Math.abs(leftVotes - rightVotes) < 6;
  }

  public void reset() {
    leftVotes = rightVotes = totalVotes = 0;
  }

  public boolean goLeft() {
    System.out.println("left: " + leftVotes + "right " + rightVotes);
    boolean goLeft = leftVotes < rightVotes;
    ChezyRobot.leftCount = leftVotes;
    ChezyRobot.rightCount = rightVotes;
    ChezyRobot.goLeftAuto = goLeft;
    return goLeft;
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

        int ch = 0;
        byte[] b = new byte[1024];
        double timeout = 10.0;
        double lastHeartbeat = Timer.getFPGATimestamp();
        VisionHotGoalDetector.this.lastHeartbeatTime = lastHeartbeat;
        while (Timer.getFPGATimestamp() < lastHeartbeat + timeout) {
          boolean gotData = false;
          while (is.available() > 0) {
            gotData = true;
            int read = is.read(b);
            for (int i = 0; i < read; ++i) {
              byte reading = b[i];
              boolean leftGoal = (reading & (1 << 1)) > 0;
              boolean rightGoal = (reading & (1 << 0)) > 0;
              VisionHotGoalDetector.this.vote(leftGoal, rightGoal);
            }
            lastHeartbeat = Timer.getFPGATimestamp();
            VisionHotGoalDetector.this.lastHeartbeatTime = lastHeartbeat;
          }

          try {
            Thread.sleep(50); // sleep a bit
          } catch (InterruptedException ex) {
            ex.printStackTrace();
          }
        }
        is.close();
        connection.close();

      } catch (IOException e) {
      }
    }
  }

  public void run() {
    ServerSocketConnection s = null;
    try {
      s = (ServerSocketConnection) Connector.open("serversocket://:" + listenPort);
      Vector threads = new Vector();
      while (true) {
        SocketConnection connection = (SocketConnection) s.acceptAndOpen();
        Thread t = new Thread(new VisionHotGoalDetector.ConnectionHandler(connection));
        t.start();
        connections.addElement(connection);
        try {
          Thread.sleep(100);
        } catch (InterruptedException ex) {
          ex.printStackTrace();
        }
      }
    } catch (IOException e) {
      System.out.println("There was an error el oh el" + e);
    }
  }

  public VisionHotGoalDetector() {
    this(1180);
  }

  public VisionHotGoalDetector(int port) {
    listenPort = port;
    connections = new Vector();
  }
}

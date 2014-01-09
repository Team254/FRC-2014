/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.lib.util;

import com.team254.frc2014.ChezyRobot;

/**
 *
 * @author bg
 */
public class HtmlResponse {
  static String header = "HTTP/1.x 200 OK\n\n";
  public static String createResponse(String res) {
    return header + res;
  }
  public static String test() {
    return header + "<h1> Test.html page </h1>";
  }
  public static String drivebase() {
    return header + ChezyRobot.drivebase.serialize();
  }
}

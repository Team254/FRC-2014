/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.lib.util;

import com.team254.lib.Subsystem;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author spinkerton
 */
public class Json {
  
  public static String format(Hashtable data) {
    String res = "{";
    System.out.println("Starting: " + data.size());

    // TODO: This might still be broken, but I didn't have time to test
    for (Enumeration en = data.keys(); en.hasMoreElements(); ) {
      String key = (String) en.nextElement();
      res += "\"" + key + "\" : ";
      
      String value = data.get(key).toString();
      System.out.println(key + " => " + value);

      if (Util.isNumber(value)) res += value;
      else res += "\"" + value + "\"";
      res += ",\n";
      System.out.println("finished a loopy");
    }
    res += "}";
    System.out.println("JSON = " + res);
    return res;
  }
}

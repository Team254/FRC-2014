/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.lib.util;

import java.util.Hashtable;

/**
 *
 * @author spinkerton
 */
public class Json {
  
  public static String format(Hashtable data) {
    String res = "{";
    
    while (data.keys().hasMoreElements()) {
      Object key = data.keys().nextElement();
      res += "\"" + key.toString() + "\" : ";
      
      String value = data.get(key).toString();
      if (Util.isNumber(value)) res += value;
      else res += "\"" + value + "\"";
      res += ",\n";
    }
    res += "}";
    return res;
  }
}

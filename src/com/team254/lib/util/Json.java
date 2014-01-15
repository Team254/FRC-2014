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
    String res = "{\n";

    for (Enumeration en = data.keys(); en.hasMoreElements(); ) {
      String key = (String) en.nextElement();
      res += "\t\"" + key + "\" : ";
      
      String value = data.get(key).toString();

      if (Util.isNumber(value)) res += value;
      else res += "\"" + value + "\"";
      res += ",\n";
    }
    res += "}";
    return res;
  }
}

package com.team254.lib;

import java.util.Hashtable;

public abstract class Subsystem  {
  protected String name;
  protected Hashtable data;

  public abstract Hashtable serialize();
    
  public Subsystem(String name){
    this.name = name;
    this.data = new Hashtable();
    data.put("subsystem", name);
    SubsystemLister.getSubsystemLister().addSubsystem(name, this);
  }

  public String toString() {
    return name;
  }
}

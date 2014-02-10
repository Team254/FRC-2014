package com.team254.lib;

import java.util.Hashtable;
/**
 * This class provides a model for a subsystem
 * @author akalb
 * @author dchan
 */
public abstract class Subsystem  {
  protected String name;
  protected Hashtable data;
  protected Controller controller;

  public abstract Hashtable serialize();
  
    public void useController(Controller c) {
    if (controller != null) {
      controller.disable();
    }
    controller = c;
    if (controller != null) {
      controller.enable();
    }
  }
  
  public void turnOffControllers() {
    useController(null);
  }

  public void update() {
    if (controller != null) {
      controller.update();
    }
  }
    
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

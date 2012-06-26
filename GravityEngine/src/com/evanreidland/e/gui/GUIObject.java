package com.evanreidland.e.gui;

import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.Settings;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.phys.Rect3;

public class GUIObject {
	private String name;
	public String getName() {
		return name;
	}
	private GUI gui;
	public GUI getGUI() {
		return gui;
	}
	public boolean setGUI(GUI gui) { 
		if ( this.gui == null ) {
			this.gui = gui;
			for ( int i = 0; i < children.size(); i++ ) {
				gui.addObject(children.get(i));
			}
			return true;
		}
		return false;
	}
	public Settings settings;
	public Rect3 rect;
	public boolean bFocused, bClicked;
	public GUIObject parent;
	
	private Vector<GUIObject> children;
	private HashMap<String, GUIObject> childrenMap;
	
	public GUIObject addObject(GUIObject object) {
		 childrenMap.put(object.getName(), object);
		 if ( gui != null ) {
			 gui.addObject(object);
		 }
		 return object;
	}
	public GUIObject getObject(String name) {
		return childrenMap.get(name);
	}
	public GUIObject removeObject(GUIObject object) {
		children.remove(object);
		childrenMap.remove(object.getName());
		if ( gui != null ) {
			gui.removeObject(object);
		}
		return object;
	}
	
	public void onRender() {
		for ( int i = 0; i < children.size(); i++ ) {
			children.get(i).onRender();
		}
	}
	public void onUpdate() {
		for ( int i = 0; i < children.size(); i++ ) {
			children.get(i).onUpdate();
		}
	}
	//Note: Returning true means that this object absorbs the event.
	public boolean onClick(double x, double y) {
		return false;
	}
	public void onType(char key) {
	}
	
	
	public GUIObject(String name) { 
		this.name = name;
		settings = new Settings();
		rect = new Rect3(Vector3.Zero(), Vector3.Zero());
		parent = null;
		
		settings.set("name", name);
		settings.set("hidden", "0");
		
		children = new Vector<GUIObject>();
		childrenMap = new HashMap<String, GUIObject>();
		
		gui = null;
	}
}

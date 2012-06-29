package com.evanreidland.e.gui;

import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.Settings;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.graphics.Quad;
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
				GUIObject object = children.get(i);
				gui.addObject(object);
				object.setGUI(gui);
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
			 object.setGUI(gui);
		 }
		 return object;
	}
	public GUIObject getObject(String name) {
		GUIObject object = childrenMap.get(name);
		if ( object != null ) {
			return object;
		} else {
			for ( int i = 0; i < children.size(); i++ ) {
				object = children.get(i).getObject(name);
				if ( object != null ) {
					return object;
				}
			}
		}
		return null;
	}
	public GUIObject removeObject(GUIObject object) {
		children.remove(object);
		childrenMap.remove(object.getName());
		if ( gui != null ) {
			gui.removeObject(object);
		}
		return object;
	}
	
	public void Render() {
		onRender();
		for ( int i = 0; i < children.size(); i++ ) {
			children.get(i).Render();
		}
	}
	public void Update() {
		onUpdate();
		for ( int i = 0; i < children.size(); i++ ) {
			children.get(i).Update();
		}
	}
	
	public void onRender() {
		
	}
	public void onUpdate() {
		
	}
	
	//Note: Returning true means that this object absorbs the event.
	public boolean onClick(double x, double y) {
		return false;
	}
	public void onType(char key) {
		
	}
	
	public void renderQuadOnRect(Quad quad, Rect3 rect) {
		quad.vert[0].pos = rect.topLeft();
		quad.vert[1].pos = rect.topRight();
		quad.vert[2].pos = rect.bottomRight();
		quad.vert[3].pos = rect.bottomLeft();
		quad.pass();
	}
	
	public void renderQuadOnRect(Quad quad) {
		renderQuadOnRect(quad, rect);
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

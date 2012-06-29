package com.evanreidland.e.gui;

import java.util.HashMap;
import java.util.Vector;

//Note: Although GUIObjects can have child objects, all objects are added to the GUI the object is a part of.
public class GUI {
	private Vector<GUIObject> objects;
	private HashMap<String, GUIObject> objectsMap;
	
	public GUIObject addObject(GUIObject object) {
		if ( !objects.contains(object) ) {
			objects.add(object);
			objectsMap.put(object.getName(), object);
		}
		return object;
	}
	
	public void Render() {
		for ( int i = 0; i < objects.size(); i++ ) {
			GUIObject object = objects.get(i);
			if ( object.parent == null ) {
				object.Render();
			}
		}
	}
	public void Update() {
		for ( int i = 0; i < objects.size(); i++ ) {
			GUIObject object = objects.get(i);
			if ( object.parent == null ) {
				object.Update();
			}
		}
	}
	
	public GUIObject getObject(String name) {
		return objectsMap.get(name);
	}
	
	public GUIObject removeObject(GUIObject object) {
		if ( object != null ) {
			objects.remove(object);
			objectsMap.remove(object.getName());
		}
		return object;
	}
	
	public GUIObject removeObject(String name) {
		return removeObject(objectsMap.get(name));
	}
	
	public Vector<GUIObject> getObjectChildren(String parentName) {
		Vector<GUIObject> children = new Vector<GUIObject>();
		for ( int i = 0; i < objects.size(); i++ ) {
			GUIObject object = objects.get(i);
			if ( object.parent != null && object.parent.getName().equals(parentName) ) {
				children.add(object);
			}
		}
		return children;
	}
	
	public void changeSetting(String objectName, String name, String newValue) {
		GUIObject object = getObject(objectName);
		if ( object != null ) {
			object.settings.set(name, newValue);
		}
	}
	
	public void changeSettingForChildren(String objectName, String name, String newValue) {
		Vector<GUIObject> children = getObjectChildren(objectName);
		for ( int i = 0; i < children.size(); i++ ) {
			GUIObject object = children.get(i);
			object.settings.set(name, newValue);
			changeSettingForChildren(object.getName(), name, newValue);
		}
	}
	
	public void layoutObject(GUIObject object) {
		//TODO code.
	}
	
	public void Layout() {
		for ( int i = 0; i < objects.size(); i++ ) {
			GUIObject object = objects.get(i);
			if ( object.parent != null ) {
				layoutObject(object);
			}
		}
	}
	
	public boolean onClick(double x, double y) {
		for ( int i = 0; i < objects.size(); i++ ) {
			GUIObject object = objects.get(i);
			if ( object.parent == null ) {
				object.onClick(x, y);
			}
		}
		return false;
	}
	public void onType(char key) {
		for ( int i = 0; i < objects.size(); i++ ) {
			objects.get(i).onType(key);
		}
	}
	
	public GUI() {
		objects = new Vector<GUIObject>();
		objectsMap = new HashMap<String, GUIObject>();
	}
}

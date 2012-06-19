package com.evanreidland.e.graphics;

import java.util.Vector;

// Potential break. Maybe SceneObjects should use an ID system so that they can't add recursively?
public class Scene extends SceneObject {
	private Vector<SceneObject> objects;
	
	public boolean autoOrder;
	
	public int getObjectCount() {
		return objects.size();
	}
	public void addObject(SceneObject object) {
		double dist = object.calcRoughDistance();
		for ( int i = 0; i < objects.size(); i++ ) {
			double otherDist = objects.get(i).getRoughDistance();
			if ( dist < otherDist ) {
				objects.insertElementAt(object, i);
				return;
			}
		}
		objects.add(object);
	}
	
	public void Position() {
		for ( int i = 0; i < objects.size(); i++ ) {
			objects.get(i).Position();
		}
	}
	public void Render() {
		for ( int i = 0; i < objects.size(); i++ ) {
			objects.get(i).Render();
		}
	}
	
	public void Order() {
		Vector<SceneObject> currentObjects = new Vector<SceneObject>(objects);
		
		objects = new Vector<SceneObject>();
		for ( int i = 0; i < currentObjects.size(); i++ ) {
			SceneObject object = currentObjects.get(i);
			addObject(object);
			object.Order();
		}
		
		
	}
	
	public Scene() {
		objects = new Vector<SceneObject>();
		autoOrder = true;
	}
}

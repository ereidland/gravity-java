package com.evanreidland.e.graphics;

import java.util.Vector;

import com.evanreidland.e.Flags.State;
import com.evanreidland.e.ent.Entity;

// Potential break. Maybe SceneObjects should use an ID system so that they can't add recursively?
public class Scene extends SceneObject {
	private Vector<SceneObject> objects;
	
	public boolean autoRemove, autoPosition, autoOrder;
	
	public int getObjectCount() {
		return objects.size();
	}
	public SceneObject addObject(SceneObject object) {
		double dist = object.calcRoughDistance();
		for ( int i = 0; i < objects.size(); i++ ) {
			double otherDist = objects.get(i).getRoughDistance();
			if ( dist > otherDist ) {
				objects.insertElementAt(object, i);
				return object;
			}
		}
		objects.add(object);
		return object;
	}
	
	public SceneObject addObject(SceneObject object, Entity parent, AnchorType anchorType) {
		addObject(object);
		object.setParentEntity(parent);
		object.anchorType = anchorType;
		return object;
	}
	
	public SceneObject addObject(SceneObject object, Entity parent) {
		return addObject(object, parent, AnchorType.POS_ANGLE);
	}
	
	public SceneObject addObject(SceneObject object, SceneObject parent, AnchorType anchorType) {
		parent.child.addObject(object);
		object.setParentObject(parent);
		object.anchorType = anchorType;
		return object;
	}
	
	public SceneObject addObject(SceneObject object, SceneObject parent) {
		return addObject(object, parent, AnchorType.POS_ANGLE);
	}
	
	public void Position() {
		for ( int i = 0; i < objects.size(); i++ ) {
			objects.get(i).Position();
		}
	}
	public void Render() {
		if ( autoRemove ) {
			bringOutTheDead();
		}
		if ( autoPosition ) {
			Position();
		}
		if ( autoOrder ) {
			Order();
		}
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
	
	public void bringOutTheDead() {
		Vector<SceneObject> dead = new Vector<SceneObject>();
		for ( int i = 0; i < objects.size(); i++ ) {
			SceneObject object = objects.get(i);
			if ( object.getParentEntity() != null ) {
				if ( object.getParentEntity().flags.getState("dead") == State.True ) {
					dead.add(object);
				}
			}
		}
		
		for ( int i = 0; i < dead.size(); i++ ) {
			objects.remove(dead.get(i));
		}
	}
	
	public Scene() {
		super(false);
		objects = new Vector<SceneObject>();
		autoRemove = true;
		autoPosition = true;
		autoOrder = true;
		child = this;
	}
}

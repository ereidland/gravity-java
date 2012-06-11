package com.evanreidland.e.ent;

import com.evanreidland.e.Flags.State;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;

public class Entity extends EObject {
	public Vector3 pos, vel, angle;
	
	public float radius, mass;
	
	public boolean bStatic;
	
	public EventList events;
	
	public void onThink() {
		State f = flags.getState("dead");
		if ( f == State.False || f == State.Undef || f == State.Either ) {
			pos.add(vel.multipliedBy(engine.getDelta()));
		}
	}
	
	public float getGravity(Vector3 source, float sourceMass) {
		float radius = source.getDistance(pos);
		return (mass*sourceMass)/(radius*radius);
	}
	
	public float getGravity(Entity other) {
		return getGravity(other.pos, other.mass);
	}
	
	public void applyGravity(Vector3 source, float sourceMass, float delta) {
		vel.add(source.minus(pos).getNormal().multiply(getGravity(source, sourceMass)*delta));
	}
	
	public void applyGravity(Entity other, float delta) {
		applyGravity(other.pos, other.mass, delta);
	}
	
	public void onRender() {
		
	}
	public void onRenderHUD() {
		
	}
	public void Spawn() {
		
	}
	
	public Entity(String className, long id) {
		super(className, id);
		pos = Vector3.Zero();
		vel = Vector3.Zero();
		bStatic = false;
	}
}

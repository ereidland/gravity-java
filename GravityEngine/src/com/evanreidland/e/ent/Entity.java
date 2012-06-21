package com.evanreidland.e.ent;

import com.evanreidland.e.Flags.State;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;

public class Entity extends EObject {
	public static boolean debug = false;
	public Vector3 pos, vel, angle, angleVel;
	
	public double radius, mass;
	
	public boolean bStatic;
	
	public EventList events;
	
	public double relativity; // Possibly going to change visibility and make this a properly used variable.
	
	public void Kill() {
		flags.setState("dead", State.True);
	}
	
	public void onThink() {
		State f = flags.getState("dead");
		if ( !bStatic && (f == State.False || f == State.Undef || f == State.Either) ) {
			pos.add(vel.multipliedBy(engine.getDelta()*relativity));
			angle.add(angleVel.multipliedBy(engine.getDelta()*relativity));
			angle.clipAngle();
		}
	}
	
	public double getOrbitalVelocity(double distance, double sourceMass) {
		if ( distance == 0 ) return 0; // Better than returning infinity.
		return (double)Math.sqrt(sourceMass/distance);
	}
	
	public double getOrbitalVelocity(Entity other) {
		return getOrbitalVelocity(pos.getDistance(other.pos), other.mass);
	}
	
	public double getGravity(double distance, double sourceMass) {
		if ( distance == 0 ) return 0;
		return (mass*sourceMass)/(distance*distance);
	}
	
	public double getGravity(Vector3 source, double sourceMass) {
		double radius = source.getDistance(pos);
		return (mass*sourceMass)/(radius*radius);
	}
	
	public double getGravity(Entity other) {
		return getGravity(other.pos, other.mass);
	}
	
	public void applyGravity(Vector3 source, double sourceMass, double delta) {
		if ( mass != 0 ) {
			vel.add(source.minus(pos).getNormal().multiply((getGravity(source, sourceMass)*delta*relativity)/mass));
		}
	}
	
	public void applyGravity(Entity other, double delta) {
		applyGravity(other.pos, other.mass, delta);
	}
	
	public void onRender() {
		if ( debug ) {
			graphics.unbindTexture();
			graphics.drawLine(pos, pos.plus(vel), 1, 1, 0, 0, 0.5f);
			
			graphics.drawLine(pos, pos.plus(ents.list.getGravitationalInfluence(this).multiply(10/mass)), 1, 0, 0, 1, 0.5f);
		}
	}
	public void onRenderHUD() {
		
	}
	public void Spawn() {
		
	}
	
	public Entity(String className, long id) {
		super(className, id);
		pos = Vector3.Zero();
		vel = Vector3.Zero();
		angle = Vector3.Zero();
		angleVel = Vector3.Zero();
		bStatic = false;
		relativity = 1;
		
		flags.setState("dead", State.False);
	}
}

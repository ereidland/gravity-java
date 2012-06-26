package com.evanreidland.e.gravity;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
import com.evanreidland.e.client.ent.Ship;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.graphics.generate;

public class TestInterceptor extends Ship {
	public double lifeRemaining;
	public void onThink() {
		super.onThink();
		angle = vel.getAngle();
		lifeRemaining -= Game.getDelta();
		if ( lifeRemaining <= 0 ) {
			Kill();
		}
		
		if ( input.getKeyState(key.KEY_0) ) {
			Kill();
		}
	}
	public void onDie() {
		Entity ent = ents.Create("explosion", new Double[] { 0d, 10d, 0.5d, 1d, 0d });
		if ( ent != null ) {
			ent.pos.setAs(pos);
			ent.vel.setAs(vel);
			ent.mass = mass;
			ent.Spawn();
		}
	}
	public TestInterceptor(long id) {
		super(id);
		
		lifeRemaining = 2;
		
		model = generate.Cube(Vector3.Zero(), new Vector3(0.005, 0.005, 0.01), Vector3.Zero());
		model.tex = engine.loadTexture("shiptest1.png");
		mass = 0.000000001;
	}
}

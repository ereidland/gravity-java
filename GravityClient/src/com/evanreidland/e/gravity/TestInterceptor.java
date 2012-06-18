package com.evanreidland.e.gravity;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
import com.evanreidland.e.client.ent.Ship;
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
	public TestInterceptor(long id) {
		super(id);
		
		lifeRemaining = 20;
		
		model = generate.Cube(Vector3.Zero(), new Vector3(0.01, 0.01, 0.02), Vector3.Zero());
		model.tex = engine.loadTexture("shiptest1.png");
		mass = 0.000000001;
	}
}

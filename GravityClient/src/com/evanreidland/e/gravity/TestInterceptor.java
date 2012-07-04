package com.evanreidland.e.gravity;
import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.graphics.BillboardSceneObject;
import com.evanreidland.e.graphics.SceneObject.AnchorType;
import com.evanreidland.e.graphics.Sprite;
import com.evanreidland.e.graphics.graphics;

public class TestInterceptor extends Entity {
	public double lifeRemaining;
	
	public Vector3 target;
	public double force, thrust;
	public void onThink() {
		super.onThink();
		lifeRemaining -= Game.getDelta()*relativity;
		if ( lifeRemaining <= 0 ) {
			Kill();
		}
		
		if ( input.getKeyState(key.KEY_0) ) {
			Kill();
		}
		
		if ( target != null ) {
			if ( target.minus(pos).getLength() < radius ) {
				Kill();
			} else {
				Vector3 targetVel = target.minus(pos).Normalize().multipliedBy(thrust);
				vel.add(targetVel.minus(vel).Normalize().multipliedBy(Game.getDelta()*10));
			}
		}
		
		angle = vel.getAngle();
	}
	public void onDie() {
		Entity ent = ents.Create("explosion", new Double[] { 0d, 0.5d, 0.5d, 1d, 0d });
		if ( ent != null ) {
			ent.pos.setAs(pos);
			ent.vel.setAs(vel);
			ent.mass = mass;
			ent.flags.setState("enemy", flags.getState("enemy"));
			ent.Spawn();
		}
	}
	public void Setup(Object[] args) {
		pos.setAs((Vector3)getArg(args, 0, Vector3.Zero()));
		vel.setAs((Vector3)getArg(args, 1, Vector3.Zero()));
		flags.setState("enemy", (Boolean)getArg(args, 2, Boolean.FALSE));
		target = (Vector3)getArg(args, 3, null);
		thrust = (Double)getArg(args, 4, 10d);
		force = (Double)getArg(args, 5, 10d);
		lifeRemaining = (Double)getArg(args, 6, 10d);
	}
	public void onSpawn() {
//		Model model = generate.Cube(Vector3.Zero(), new Vector3(0.005, 0.005, 0.01), Vector3.Zero());
//		model.tex = engine.loadTexture("shiptest1.png");
//		graphics.scene.addObject(new ModelSceneObject(model), this);
		Sprite sprite = new Sprite(0.05, 0.05, engine.loadTexture("laser1.png"));
		sprite.cr = 0.5;
		sprite.cg = 1;
		sprite.cb = 0.5;
		sprite.ca = 1.0;
		graphics.scene.addObject(
				new BillboardSceneObject(
						sprite,
						true),
				this,
				AnchorType.POS);
	}
	public TestInterceptor(long id) {
		super("missile", id);
		
		lifeRemaining = 10;
		mass = 0.000001;
		target = null;
		
		force = 10;
		thrust = 1; // Not relative to mass.
		radius = 0.1;
	}
}

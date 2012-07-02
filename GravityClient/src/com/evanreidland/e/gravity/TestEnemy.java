package com.evanreidland.e.gravity;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.SearchData;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.graphics.ModelSceneObject;
import com.evanreidland.e.graphics.generate;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.phys.phys;

public class TestEnemy extends Entity {
	long nextShot = 0;
	public void onThink() {
		super.onThink();
		angle.setAs(vel.getAngle());
		SearchData data = ents.findNearest(pos, 10000000, new Flags("player targetable"));
		if ( data.isPositive ) {
			Vector3 targetVel = data.ent.pos.minus(pos).Normalize().multipliedBy(1);
			vel.add(targetVel.minus(vel).Normalize().multipliedBy(Game.getDelta()*2));
			
			if ( Game.getTime() > nextShot && data.length < 50 ) {
				nextShot = Game.getTime() + 100;
				
				double shotSpeed = 2;
				Vector3 targetPoint = phys.getTargetPoint(pos, vel, data.ent.pos, data.ent.vel, shotSpeed);
				
				Entity ent = ents.Create("missile",
						new Object[] {
							pos.plus(angle.getForward().multipliedBy(0.01)),
							vel.plus(targetPoint.minus(pos).Normalize().multipliedBy(shotSpeed)),
							true,
							null,
							20d,
							10d,
						});
				ent.Spawn();
			}
		}
	}
	
	public void onSpawn() {
		Model model = generate.Sphere(Vector3.Zero(), new Vector3(0.01, 0.01, 0.02), Vector3.Zero(), 10, 10);
		model.tex = engine.loadTexture("shiptest1.png");
		graphics.scene.addObject(new ModelSceneObject(model), this);
	}

	public TestEnemy(long id) {
		super("enemy", id);
		
		bStatic = false;
		mass = 0.00001;
	}

}

package com.evanreidland.e.client;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.roll;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.SearchData;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.graphics.ModelSceneObject;
import com.evanreidland.e.graphics.generate;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.phys.phys;
import com.evanreidland.e.phys.phys.Target;

public class TestEnemy extends Entity
{
	long nextShot = 0;
	
	public void onThink()
	{
		super.onThink();
		angle.setAs(vel.getAngle());
		SearchData data = ents.findNearest(pos, 10000000, new Flags(
				"player targetable"));
		if (data.isPositive)
		{
			// Vector3 targetVel =
			// data.ent.pos.minus(pos).Normalize().multipliedBy(1);
			// vel.add(targetVel.minus(vel).Normalize().multipliedBy(Game.getDelta()*2));
			vel.setAs(0, 0, 0);
			
			if (Game.getTime() > nextShot && data.length < 50)
			{
				nextShot = Game.getTime() + 1000;
				
				double shotSpeed = roll.randomDouble(0.5, 1);
				Vector3 launchPos = pos.plus(angle.getForward().multipliedBy(
						0.01));
				Target target = phys.getTarget(launchPos, vel, data.ent.pos,
						data.ent.vel, shotSpeed);
				
				Entity ent = ents.Create(
						"missile",
						new Object[]
						{
								launchPos,
								vel.plus(target.pos.minus(pos).Normalize()
										.multiply(shotSpeed)), true, null, 20d,
								10d, target.time,
						});
				ent.Spawn();
			}
		}
		
		flags.setState("self", true);
		data = ents.findNearest(pos, 1, new Flags("enemy targetable !self"));
		flags.setState("self", false);
		if (data.isPositive)
		{
			if (data.length < radius)
			{
				pos.setAs(data.ent.pos.plus(pos.minus(data.ent.pos).Normalize()
						.multipliedBy(radius)));
			}
		}
	}
	
	public void onSpawn()
	{
		Model model = generate.Sphere(Vector3.Zero(), new Vector3(0.01, 0.01,
				0.02), Vector3.Zero(), 10, 10);
		model.tex = engine.loadTexture("shiptest1.png");
		graphics.scene.addObject(new ModelSceneObject(model), this);
	}
	
	public TestEnemy(long id)
	{
		super("enemy", id);
		
		bStatic = true;
		mass = 0.00001;
		radius = 0.5;
		
		flags.add("enemy targetable");
	}
	
}

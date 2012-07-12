package com.evanreidland.e.server.ent;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.SearchData;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.phys.phys;
import com.evanreidland.e.phys.phys.Target;

public class ServerEnemy extends ServerEntity
{
	long nextShot;
	
	public void onThink()
	{
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
				nextShot = Game.getTime() + 500;
				
				double shotSpeed = 4;
				Vector3 launchPos = pos.plus(angle.getForward().multipliedBy(
						0.01));
				Target target = phys.getTarget(launchPos, vel, data.ent.pos,
						data.ent.vel, shotSpeed);
				
				Entity ent = ents.Create("laser");
				ent.vel = vel.plus(target.pos.minus(pos).getNormal()
						.multipliedBy(shotSpeed));
				ent.pos = launchPos;
			}
		}
	}
	
	public ServerEnemy(long id)
	{
		super("enemy", id);
		nextShot = 0;
	}
}

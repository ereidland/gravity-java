package com.evanreidland.e.server.ent;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.EntityList;
import com.evanreidland.e.ent.SearchData;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.phys.phys;
import com.evanreidland.e.phys.phys.Target;
import com.evanreidland.e.script.Variable;

public class ServerEnemy extends ServerEntity
{
	long nextShot;
	
	public void onThink()
	{
		super.onThink();
		SearchData data = ents.findNearest(pos, vars.get("range").toDouble(5),
				new Flags("player targetable"));
		if (data.isPositive)
		{
			if (Game.getTime() > nextShot)
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
				ent.flags.add("enemy");
			}
		}
	}
	
	public void checkCollision()
	{
		EntityList list = checkCollision(new Flags("friendly projectile"),
				Game.getDelta());
		if (!list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				takeDamage(list.get(i), 1);
			}
		}
	}
	
	public ServerEnemy(long id)
	{
		super("enemy", id);
		nextShot = 0;
		radius = 0.05;
		
		hp = 2;
		maxHP = 2;
		
		flags.add(new Flags("enemy targetable"));
		vars.add(new Variable("range", 5.0));
	}
}

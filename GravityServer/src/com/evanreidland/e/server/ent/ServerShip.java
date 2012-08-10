package com.evanreidland.e.server.ent;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.EntityList;
import com.evanreidland.e.ent.SearchData;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.phys.phys;
import com.evanreidland.e.phys.phys.Target;
import com.evanreidland.e.script.Variable;
import com.evanreidland.e.server.GravityServer;
import com.evanreidland.e.shared.Player;

public class ServerShip extends ServerEntity
{
	long nextShot;
	
	public void onThink()
	{
		super.onThink();
		EntityList list = ents.list.getWithFlags(new Flags("enemy targetable"))
				.getWithinBounds(pos, vars.get("range").toDouble(5));
		
		EntityList newList = new EntityList();
		
		// TODO: Make method check aim cone after targeting, finding a new
		// target recursively.
		
		for (int i = 0; i < list.size(); i++)
		{
			Entity ent = list.get(i);
			
			Vector3 ang = Vector3.getAngleDifference(
					angle.plus(0, 0, engine.Pi_2), ent.pos.minus(pos)
							.getAngle().clipAngle());
			boolean works = true;
			for (int j = 0; j < 3; j++)
			{
				double x = Math.abs(ang.at(j));
				if (x > engine.Pi_4 && x < engine.Pi_2 + engine.Pi_4)
				{
					works = false;
					break;
				}
			}
			
			if (works)
			{
				newList.add(ent);
			}
		}
		SearchData data = newList.findNearest(pos);
		if (data.isPositive)
		{
			if (Game.getTime() > nextShot)
			{
				nextShot = Game.getTime() + 50;
				
				double shotSpeed = 128;
				Vector3 launchPos = pos.plus(angle.getForward().multipliedBy(
						0.01));
				Target target = phys.getTarget(launchPos, vel, data.ent.pos,
						data.ent.vel, shotSpeed);
				
				Entity ent = ents.Create("laser");
				ent.vel = vel.plus(target.pos.minus(pos).getNormal()
						.multipliedBy(shotSpeed));
				ent.pos = launchPos;
				ent.flags.add("friendly");
			}
		}
	}
	
	public void checkCollision()
	{
		EntityList list = checkCollision(new Flags("enemy projectile"),
				Game.getDelta());
		if (!list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				takeDamage(list.get(i), 1);
				list.get(i).takeDamage(this, 1);
			}
		}
	}
	
	public void onDie()
	{
		super.onDie();
		Player player = GravityServer.global.getPlayerOnShip(getID());
		if (player != null)
		{
			player.setPlayerShip(0);
			player.permissions.remove(getID());
		}
		GravityServer.global.broadcastMessage(0, "Ship " + getID()
				+ " was destroyed!");
	}
	
	public ServerShip(long id)
	{
		super("ship", id);
		radius = 0.05;
		hp = 4;
		maxHP = 4;
		nextShot = 0;
		vars.add(new Variable("range", 10.0));
	}
	
}

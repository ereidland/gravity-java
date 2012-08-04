package com.evanreidland.e.server.ent;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Game;
import com.evanreidland.e.ent.EntityList;

public class ServerLaser extends ServerEntity
{
	long deathTime;
	
	public void onThink()
	{
		super.onThink();
		if (Game.getTime() >= deathTime)
		{
			Kill();
		}
	}
	
	public void checkCollision()
	{
		EntityList list = checkCollision(new Flags("player targetable"));
		if (!list.isEmpty())
		{
			Kill();
		}
	}
	
	public ServerLaser(long id)
	{
		super("laser", id);
		deathTime = Game.getTime() + 2000;
		radius = 0.05;
		flags.add(new Flags("enemy laser projectile"));
	}
}

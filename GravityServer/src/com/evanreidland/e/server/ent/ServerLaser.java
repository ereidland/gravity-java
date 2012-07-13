package com.evanreidland.e.server.ent;

import com.evanreidland.e.Game;

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
	
	public ServerLaser(long id)
	{
		super("laser", id);
		deathTime = Game.getTime() + 2000;
	}
}

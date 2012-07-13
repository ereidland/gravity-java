package com.evanreidland.e.server;

import com.evanreidland.e.Game;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.server.ent.ServerEnemy;
import com.evanreidland.e.server.ent.ServerLaser;
import com.evanreidland.e.server.ent.ServerShip;
import com.evanreidland.e.shared.config.ServerConfig;

public class GravityServerGame extends Game
{
	public void onUpdate()
	{
		for (int i = 0; i < ents.list.size(); i++)
		{
			Entity ent = ents.list.get(i);
			if (!ent.bSent)
			{
				GravityServer.global.sendEntitySpawn(ent);
			}
		}
		ents.list.onThink();
	}
	
	public void onInit()
	{
		ServerConfig.setupConfigs();
		
		ents.Register("enemy", ServerEnemy.class);
		ents.Register("laser", ServerLaser.class);
		ents.Register("ship", ServerShip.class);
	}
	
	// Unused.
	public void onRender()
	{
	}
	
	public void onRenderHUD()
	{
		
	}
}

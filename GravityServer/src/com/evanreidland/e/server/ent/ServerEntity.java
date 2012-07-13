package com.evanreidland.e.server.ent;

import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.server.GravityServer;

public class ServerEntity extends Entity
{
	public void sendPos()
	{
		GravityServer.global.sendEntityPos(this);
	}
	
	public void onDie()
	{
		GravityServer.global.sendEntityDeath(this);
	}
	
	public ServerEntity(String className, long id)
	{
		super(className, id);
	}
}

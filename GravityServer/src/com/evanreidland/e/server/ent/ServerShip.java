package com.evanreidland.e.server.ent;

public class ServerShip extends ServerEntity
{
	
	public void onThink()
	{
		super.onThink();
	}
	
	public ServerShip(long id)
	{
		super("ship", id);
		radius = 0.01;
	}
	
}

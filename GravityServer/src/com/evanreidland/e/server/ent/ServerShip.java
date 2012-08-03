package com.evanreidland.e.server.ent;

import com.evanreidland.e.Flags;
import com.evanreidland.e.ent.EntityList;
import com.evanreidland.e.server.GravityServer;

public class ServerShip extends ServerEntity
{
	
	public void onThink()
	{
		super.onThink();
	}
	
	public void checkCollision()
	{
		EntityList list = checkCollision(new Flags("enemy projectile"));
		if (!list.isEmpty())
		{
			System.out.println(toString() + " took damage!");
			for (int i = 0; i < list.size(); i++)
			{
				takeDamage(list.get(i), 1);
			}
		}
	}
	
	public void onDie()
	{
		super.onDie();
		GravityServer.global.broadcastMessage(0, "Ship " + getID()
				+ " was destroyed!");
	}
	
	public ServerShip(long id)
	{
		super("ship", id);
		radius = 0.05;
		hp = 4;
		maxHP = 4;
	}
	
}

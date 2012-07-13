package com.evanreidland.e.server.ent;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;

public class ServerShip extends ServerEntity
{
	public Vector3 velThrust, angleThrust;
	
	public void onThink()
	{
		super.onThink();
		vel.add(velThrust.multipliedBy(Game.getDelta()));
		angleVel.add(angleThrust.multipliedBy(Game.getDelta()));
		sendPos();
	}
	
	public ServerShip(long id)
	{
		super("ship", id);
		velThrust = Vector3.Zero();
		angleThrust = Vector3.Zero();
	}
	
}

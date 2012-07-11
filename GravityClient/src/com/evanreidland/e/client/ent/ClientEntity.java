package com.evanreidland.e.client.ent;

import com.evanreidland.e.ent.Entity;

public class ClientEntity extends Entity
{
	public void setupGraphics()
	{
		
	}
	
	public void onSpawn()
	{
		setupGraphics();
	}
	
	public ClientEntity(String className, long id)
	{
		super(className, id);
	}
}

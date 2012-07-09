package com.evanreidland.e.server.ent;

import com.evanreidland.e.ent.Entity;

public class NotReallyAnEnemy extends Entity
{
	public NotReallyAnEnemy()
	{
		super("enemy");
	}
	
	public NotReallyAnEnemy(long id)
	{
		super("enemy", id);
	}
}

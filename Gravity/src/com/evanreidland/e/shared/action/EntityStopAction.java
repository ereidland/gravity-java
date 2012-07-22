package com.evanreidland.e.shared.action;

import com.evanreidland.e.engine;
import com.evanreidland.e.ent.Entity;

public class EntityStopAction extends EntityAction
{
	public boolean onStart()
	{
		engine.Log("Stopped " + ent.toString());
		ent.vel.setAs(0, 0, 0);
		ent.kill("ent_move");
		return true;
	}
	
	public EntityStopAction()
	{
		super("ent_stop", "ent_move");
	}
	
	public EntityStopAction(Entity ent)
	{
		this();
		this.ent = ent;
	}
}

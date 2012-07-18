package com.evanreidland.e.shared.action;

import com.evanreidland.e.engine;

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
		super("ent_stop");
	}
}

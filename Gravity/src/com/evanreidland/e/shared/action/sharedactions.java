package com.evanreidland.e.shared.action;

import com.evanreidland.e.action.Action;
import com.evanreidland.e.action.act;

public class sharedactions
{
	public static final Action ent_move = new EntityMoveAction(),
			ent_stop = new EntityStopAction();
	
	public static void registerAll()
	{
		act.Register(ent_move);
		act.Register(ent_stop);
	}
}

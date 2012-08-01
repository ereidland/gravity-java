package com.evanreidland.e.shared.action;

import com.evanreidland.e.action.Action;
import com.evanreidland.e.action.act;

public class sharedactions
{
	public static final Action ent_move = new EntityMoveAction(),
			ent_stop = new EntityStopAction(),
			ent_bezmove = new EntityBezierMoveAction(),
			ent_manmove = new EntityManualMoveAction();
	
	public static void registerAll()
	{
		act.Register(ent_move);
		act.Register(ent_stop);
		act.Register(ent_bezmove);
		act.Register(ent_manmove);
	}
}

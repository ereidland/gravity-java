package com.evanreidland.e.shared.action;

import com.evanreidland.e.Vector3;

public class EntityMoveAction extends EntityAction
{
	public Vector3 targetPos;
	
	public boolean Update()
	{
		if (super.Update())
			return true;
		
		return true;
	}
	
	public EntityMoveAction()
	{
		super("ent_move");
	}
}

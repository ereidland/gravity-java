package com.evanreidland.e.shared.action;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.net.Bits;

public class EntityMoveAction extends EntityAction
{
	public Vector3 targetPos;
	
	public boolean Update()
	{
		if (super.Update())
			return true;
		
		ent.pos.setAs(targetPos);
		
		return true;
	}
	
	public Bits toBits()
	{
		Bits bits = super.toBits();
		bits.write(targetPos.toBits());
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		super.loadBits(bits);
		targetPos = Vector3.fromBits(bits);
	}
	
	public EntityMoveAction()
	{
		super("ent_move");
	}
	
	public EntityMoveAction(Entity ent, Vector3 targetPos)
	{
		this();
		this.ent = ent;
		this.targetPos = targetPos.cloned();
	}
}

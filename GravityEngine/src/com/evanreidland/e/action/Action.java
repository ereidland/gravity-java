package com.evanreidland.e.action;

import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;

public abstract class Action implements Bitable
{
	private String name, type;
	private Entity actor;
	public boolean isOrdered, isStarted;
	
	public Entity getActor()
	{
		return actor;
	}
	
	public boolean setActor(Entity actor)
	{
		if (this.actor == null)
		{
			this.actor = actor;
			return true;
		}
		return false;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getType()
	{
		return type;
	}
	
	public Bits toBits()
	{
		return new Bits().writeBit(isOrdered);
	}
	
	public void loadBits(Bits bits)
	{
		isOrdered = bits.readBit();
	}
	
	public abstract boolean Update();
	
	public abstract boolean onStart();
	
	public abstract void onEnd(boolean forced);
	
	public boolean validate(PermissionsList perms)
	{
		return true;
	}
	
	// Note: all actions that extend this must have a default constructor for
	// factory usage.
	public Action(String name, String type)
	{
		this.name = name;
		this.type = type;
		actor = null;
		isOrdered = true;
		isStarted = false;
	}
}

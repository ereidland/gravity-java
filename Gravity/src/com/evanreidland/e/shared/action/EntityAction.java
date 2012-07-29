package com.evanreidland.e.shared.action;

import com.evanreidland.e.engine;
import com.evanreidland.e.action.Action;
import com.evanreidland.e.action.PermissionsList;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.net.Bits;

public abstract class EntityAction extends Action
{
	public Entity ent;
	
	public boolean onStart()
	{
		return false;
	}
	
	public void onEnd(boolean bForced)
	{
		
	}
	
	public boolean Update()
	{
		return ent == null;
	}
	
	public Bits toBits()
	{
		Bits bits = new Bits();
		bits.writeBit(ent != null);
		if (ent != null)
		{
			bits.writeLong(ent.getID());
		}
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		if (bits.readBit())
		{
			ent = ents.get(bits.readLong());
		}
		else
		{
			engine.Log("Event passed with null entitiy!");
		}
	}
	
	public boolean validate(PermissionsList perms)
	{
		if (ent != null)
		{
			return perms.isGranted(getName(), ent.getID());
		}
		return false;
	}
	
	public EntityAction(String name, String type)
	{
		super(name, type);
		ent = null;
	}
}

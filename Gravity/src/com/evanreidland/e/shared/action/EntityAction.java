package com.evanreidland.e.shared.action;

import com.evanreidland.e.engine;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.net.Bits;

public class EntityAction extends SharedAction
{
	public Entity ent;
	
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
	
	public EntityAction(String name)
	{
		super(name);
		ent = null;
	}
}

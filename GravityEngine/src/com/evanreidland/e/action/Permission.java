package com.evanreidland.e.action;

import java.util.Vector;

import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;

public class Permission implements Bitable
{
	private String name;
	public boolean state;
	
	private Vector<Long> ids;
	
	public String getName()
	{
		return name;
	}
	
	public boolean addID(long id)
	{
		if (!hasID(id))
		{
			ids.add(id);
			return true;
		}
		return false;
	}
	
	public boolean removeID(long id)
	{
		return ids.remove(id);
	}
	
	public void clearIDs()
	{
		ids.clear();
	}
	
	public boolean hasID(long id)
	{
		return ids.contains(id);
	}
	
	public Bits toBits()
	{
		Bits bits = new Bits();
		
		bits.writeBit(state);
		bits.writeSize(ids.size());
		for (int i = 0; i < ids.size(); i++)
		{
			bits.writeLong(ids.get(i));
		}
		
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		state = bits.readBit();
		int size = (int) bits.readSize();
		for (int i = 0; i < size; i++)
		{
			addID(bits.readLong());
		}
	}
	
	public Permission(String name, boolean state)
	{
		this.name = name;
		this.state = state;
		ids = new Vector<Long>();
	}
}

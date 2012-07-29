package com.evanreidland.e.action;

import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;

public class Permission implements Bitable
{
	private String name;
	public boolean state;
	
	public String getName()
	{
		return name;
	}
	
	public Bits toBits()
	{
		Bits bits = new Bits();
		
		bits.writeBit(state);
		
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		state = bits.readBit();
	}
	
	public Permission(String name, boolean state)
	{
		this.name = name;
		this.state = state;
	}
}

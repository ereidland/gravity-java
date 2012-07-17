package com.evanreidland.e.action;

import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;

public abstract class Action implements Bitable
{
	private String name;
	public boolean isTimed, isOrdered;
	public float remainingTime, maxTime;
	
	public String getName()
	{
		return name;
	}
	
	public Bits toBits()
	{
		Bits bits = new Bits();
		bits.writeBit(isTimed);
		if (isTimed)
		{
			bits.writeFloat(remainingTime);
		}
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		isTimed = bits.readBit();
		if (isTimed)
		{
			remainingTime = bits.readFloat();
		}
	}
	
	public abstract boolean Update();
	
	public abstract boolean onStart();
	
	public abstract void onEnd(boolean forced);
	
	public Action(String name)
	{
		this.name = name;
		isTimed = false;
		remainingTime = 0;
		maxTime = 0;
	}
}

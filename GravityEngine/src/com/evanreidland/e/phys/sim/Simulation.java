package com.evanreidland.e.phys.sim;

import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;

public abstract class Simulation implements Bitable
{
	private double time;
	
	public double getTime()
	{
		return time;
	}
	
	public double setTime(double newTime)
	{
		return time = newTime;
	}
	
	public Bits toBits()
	{
		return new Bits().writeDouble(time);
	}
	
	public void loadBits(Bits bits)
	{
		time = bits.readDouble();
	}
	
	public void prime()
	{
		time = 0;
	}
	
	public boolean increment(double time)
	{
		this.time += time;
		return calc();
	}
	
	public abstract boolean calc();
}

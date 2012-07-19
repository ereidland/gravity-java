package com.evanreidland.e.action;

import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;

public abstract class Action implements Bitable
{
	private String name;
	private Actor actor;
	public boolean isOrdered, isStarted;
	
	public Actor getActor()
	{
		return actor;
	}
	
	public boolean setActor(Actor actor)
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
	
	public Bits toBits()
	{
		return new Bits();
	}
	
	public void loadBits(Bits bits)
	{
	}
	
	public abstract boolean Update();
	
	public abstract boolean onStart();
	
	public abstract void onEnd(boolean forced);
	
	// Note: all actions that extend this must have a default constructor for
	// factory usage.
	public Action(String name)
	{
		this.name = name;
		actor = null;
		isOrdered = true;
		isStarted = false;
	}
}

package com.evanreidland.e.shared.action;

import com.evanreidland.e.action.Action;

public class SharedAction extends Action
{
	public boolean Update()
	{
		return false;
	}
	
	public boolean onStart()
	{
		return false;
	}
	
	public void onEnd(boolean bForced)
	{
		
	}
	
	public SharedAction(String name)
	{
		super(name);
	}
}

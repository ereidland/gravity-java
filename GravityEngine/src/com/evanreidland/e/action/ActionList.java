package com.evanreidland.e.action;

import java.util.Vector;

public class ActionList
{
	private String name;
	public boolean ordered;
	
	public String getName()
	{
		return name;
	}
	
	private Vector<Action> actions;
	
	public boolean isEmpty()
	{
		return actions.isEmpty();
	}
	
	public void Update()
	{
		if (!actions.isEmpty())
		{
			if (ordered)
			{
				Action action = actions.firstElement();
				if (action.Update())
				{
					action.onEnd(false);
					actions.remove(0);
				}
			}
			else
			{
				Vector<Action> toRemove = new Vector<Action>();
				for (int i = 0; i < actions.size(); i++)
				{
					Action action = actions.get(i);
					if (action.Update())
					{
						toRemove.add(action);
					}
				}
				
				for (int i = 0; i < toRemove.size(); i++)
				{
					Action action = toRemove.get(i);
					action.onEnd(false);
					actions.remove(action);
				}
			}
		}
	}
	
	public void add(Action action)
	{
		if (!actions.contains(action))
		{
			if (!action.onStart())
			{
				actions.add(action);
			}
			
		}
	}
	
	public void killAll()
	{
		for (int i = 0; i < actions.size(); i++)
		{
			actions.get(i).onEnd(true);
		}
		
		actions.clear();
	}
	
	public ActionList(String name)
	{
		this.name = name;
		ordered = false;
	}
}

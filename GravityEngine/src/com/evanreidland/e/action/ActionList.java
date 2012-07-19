package com.evanreidland.e.action;

import java.util.Vector;

public class ActionList
{
	private String name;
	public boolean ordered, firstStarted;
	private Actor actor;
	
	public Actor getActor()
	{
		return actor;
	}
	
	public String getName()
	{
		return name;
	}
	
	private Vector<Action> actions;
	
	public boolean isEmpty()
	{
		return actions.isEmpty();
	}
	
	public boolean Update()
	{
		if (!actions.isEmpty())
		{
			if (ordered)
			{
				Action action = actions.firstElement();
				if (!firstStarted)
				{
					if (action.onStart())
					{
						action.onEnd(false);
						actions.remove(0);
						firstStarted = false;
						return Update();
					}
					else
					{
						firstStarted = true;
					}
				}
				else if (action.Update())
				{
					action.onEnd(false);
					actions.remove(0);
					firstStarted = false;
					return Update();
				}
			}
			else
			{
				int i = 0;
				while (i < actions.size())
				{
					if (actions.get(i).Update())
					{
						actions.remove(i);
					}
					else
						i++;
				}
				
			}
			return false;
		}
		
		return true;
	}
	
	public void add(Action action)
	{
		action.setActor(actor);
		if (!actions.contains(action))
		{
			actions.add(action);
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
	
	public ActionList(String name, Actor actor)
	{
		this.name = name;
		this.actor = actor;
		ordered = false;
		
		actions = new Vector<Action>();
		firstStarted = false;
	}
}

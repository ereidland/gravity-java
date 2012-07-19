package com.evanreidland.e.action;

import java.util.Vector;

public class ActionList
{
	private String name;
	public boolean ordered;
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
				if (!action.isStarted)
				{
					boolean rem = action.onStart();
					action.isStarted = true;
					if (rem)
					{
						action.onEnd(false);
						actions.remove(0);
						return Update();
					}
				}
				else if (action.Update())
				{
					action.onEnd(false);
					actions.remove(0);
					return Update();
				}
			}
			else
			{
				int i = 0;
				while (i < actions.size())
				{
					Action action = actions.get(i);
					boolean rem = false;
					if (action.isStarted)
					{
						if (action.Update())
							rem = true;
					}
					else
					{
						
						if (action.onStart())
							rem = true;
						action.isStarted = true;
					}
					
					if (rem)
						actions.remove(i);
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
	
	public void killOthers()
	{
		if (actions.size() > 1)
		{
			for (int i = 0; i < actions.size() - 1; i++)
			{
				actions.get(i).onEnd(true);
			}
		}
		Action action = actions.lastElement();
		actions.clear();
		actions.add(action);
	}
	
	public ActionList(String name, Actor actor)
	{
		this.name = name;
		this.actor = actor;
		ordered = false;
		
		actions = new Vector<Action>();
	}
}

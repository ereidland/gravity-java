package com.evanreidland.e.action;

import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.Flags;
import com.evanreidland.e.script.Stack;

public class Actor
{
	
	private Vector<ActionList> actionTypes;
	private HashMap<String, ActionList> actionTypesMap;
	
	public Flags flags;
	public Stack vars; // Potentially usable with networking.
	
	private long id;
	
	public long getID()
	{
		return id;
	}
	
	public boolean isValid()
	{
		return id != 0;
	}
	
	public void Be(long targetID)
	{
		id = targetID;
	}
	
	public void Be()
	{
		Be(id);
	}
	
	private ActionList getList(String name, boolean create)
	{
		ActionList list = actionTypesMap.get(name);
		if (list == null && create)
		{
			list = new ActionList(name, this);
			actionTypesMap.put(name, list);
			actionTypes.add(list);
		}
		
		return list;
	}
	
	public void add(Action action)
	{
		action.setActor(this);
		getList(action.getName(), true).add(action);
	}
	
	public void kill(String type)
	{
		ActionList list = getList(type, false);
		if (list != null)
		{
			list.killAll();
		}
	}
	
	public void killOthers(String type)
	{
		ActionList list = getList(type, false);
		if (list != null)
		{
			list.killOthers();
		}
	}
	
	public void killActions()
	{
		for (int i = 0; i < actionTypes.size(); i++)
		{
			actionTypes.get(i).killAll();
		}
		
		actionTypes.clear();
		actionTypesMap.clear();
	}
	
	public void Update()
	{
		Vector<ActionList> toRemove = new Vector<ActionList>();
		for (int i = 0; i < actionTypes.size(); i++)
		{
			ActionList list = actionTypes.get(i);
			if (list.Update())
			{
				toRemove.add(list);
			}
		}
		
		for (int i = 0; i < toRemove.size(); i++)
		{
			ActionList list = toRemove.get(i);
			actionTypes.remove(list);
			actionTypesMap.remove(list.getName());
		}
	}
	
	public Actor(long id)
	{
		this(new Flags(), id);
	}
	
	public Actor(Flags flags, long id)
	{
		this.flags = flags;
		this.id = id;
		vars = new Stack();
		actionTypes = new Vector<ActionList>();
		actionTypesMap = new HashMap<String, ActionList>();
	}
}

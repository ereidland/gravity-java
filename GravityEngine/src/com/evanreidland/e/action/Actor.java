package com.evanreidland.e.action;

import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.Flags;

public class Actor
{
	
	private Vector<ActionList> actionTypes;
	private HashMap<String, ActionList> actionTypesMap;
	
	public Flags flags;
	
	private ActionList getList(String name, boolean create)
	{
		ActionList list = actionTypesMap.get(name);
		if (list == null && create)
		{
			list = new ActionList(name);
			actionTypesMap.put(name, list);
			actionTypes.add(list);
		}
		
		return list;
	}
	
	public void add(Action action)
	{
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
	
	public Actor()
	{
		this(new Flags());
	}
	
	public Actor(Flags flags)
	{
		this.flags = flags;
		actionTypes = new Vector<ActionList>();
		actionTypesMap = new HashMap<String, ActionList>();
	}
}

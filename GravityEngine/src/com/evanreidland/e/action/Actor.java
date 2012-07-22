package com.evanreidland.e.action;

import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.Flags;
import com.evanreidland.e.ent.eflags;
import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.script.Stack;

public class Actor implements Bitable
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
	
	private ActionList getList(String type, boolean create)
	{
		ActionList list = actionTypesMap.get(type);
		if (list == null && create)
		{
			list = new ActionList(type, this);
			actionTypesMap.put(type, list);
			actionTypes.add(list);
		}
		
		return list;
	}
	
	public void add(Action action)
	{
		action.setActor(this);
		getList(action.getType(), true).add(action);
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
	
	public Bits toBits()
	{
		Bits bits = new Bits();
		
		bits.write(vars.toBits());
		bits.write(flags.toBits(eflags.table, false));
		
		bits.writeSize(actionTypes.size());
		for (int i = 0; i < actionTypes.size(); i++)
		{
			bits.write(actionTypes.get(i).toBits());
		}
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		vars.loadBits(bits);
		flags.loadBits(bits, eflags.table);
		
		int size = (int) bits.readSize();
		for (int i = 0; i < size; i++)
		{
			new ActionList("", this).loadBits(bits);
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

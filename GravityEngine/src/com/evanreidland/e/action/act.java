package com.evanreidland.e.action;

import java.util.Vector;

public class act
{
	private static Vector<ActionListener> listeners = new Vector<ActionListener>();
	
	public static void addListener(ActionListener listener)
	{
		if (!listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}
	
	public static void removeListener(ActionListener listener)
	{
		listeners.remove(listener);
	}
	
	public static void clearListeners()
	{
		listeners.clear();
	}
	
	public static void start(Actor actor, Action action)
	{
		actor.add(action);
		for (int i = 0; i < listeners.size(); i++)
		{
			listeners.get(i).onStarted(action);
		}
	}
}

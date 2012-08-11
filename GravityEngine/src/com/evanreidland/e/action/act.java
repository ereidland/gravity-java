package com.evanreidland.e.action;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.ent.Entity;

public class act
{
	private static Vector<ActionListener> listeners = new Vector<ActionListener>();
	private static HashMap<String, ActionFactory> actionTypes = new HashMap<String, ActionFactory>();
	
	private static class ActionFactory
	{
		public Constructor<?> cons;
		
		public Action Create()
		{
			if (cons != null)
			{
				try
				{
					Action action = (Action) cons.newInstance();
					return action;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
			return null;
		}
		
		public ActionFactory(Class<? extends Action> actionClass)
		{
			Constructor<?> consList[] = actionClass.getConstructors();
			cons = null;
			for (int i = 0; i < consList.length; i++)
			{
				if (consList[i].getParameterTypes().length == 0)
				{
					cons = consList[i];
					break;
				}
			}
		}
	}
	
	public static boolean Register(String name,
			Class<? extends Action> actionClass)
	{
		ActionFactory factory = new ActionFactory(actionClass);
		if (factory.cons != null)
		{
			actionTypes.put(name, factory);
			return true;
		}
		return false;
	}
	
	public static boolean Register(Action action)
	{
		return Register(action.getName(), action.getClass());
	}
	
	public static Action Create(String name)
	{
		ActionFactory factory = actionTypes.get(name);
		return factory != null ? factory.Create() : null;
	}
	
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
	
	public static void Start(Entity actor, Action action)
	{
		actor.add(action);
		
		for (int i = 0; i < listeners.size(); i++)
		{
			listeners.get(i).onStarted(action);
		}
	}
	
	public static void Start(Entity actor, String actionName)
	{
		Action action = Create(actionName);
		if (action != null)
		{
			act.Start(actor, action);
		}
	}
}

package com.evanreidland.e.event;

import java.util.Vector;

public class EventManager
{
	private Vector<EventCaller> callers;

	private Class<? extends Event> eventClass;

	public Class<? extends Event> getEventClass()
	{
		return eventClass;
	}

	// Returns true if the event was cancelled.
	public boolean Call(Event event)
	{
		for (int i = 0; i < callers.size(); i++)
		{
			EventCaller caller = callers.get(i);
			Object required = caller.getInfo().requiredObject;
			if (required == null || required == event.getObject())
			{
				caller.Call(event);
				if (event.isCanceled())
				{
					return true;
				}
			}
		}
		return false;
	}

	public void addCaller(EventCaller caller)
	{
		callers.add(caller);
	}

	public void removeListener(Object listenerObject)
	{
		for (int i = 0; i < callers.size(); i++)
		{
			EventListenerInfo info = callers.get(i).getInfo();
			if (info.getListener() == listenerObject)
			{
				callers.remove(i);
			}
		}
	}

	public EventManager(Class<? extends Event> eventClass)
	{
		callers = new Vector<EventCaller>();
		this.eventClass = eventClass;
	}
}

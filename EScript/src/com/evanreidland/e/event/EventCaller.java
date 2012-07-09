package com.evanreidland.e.event;

import java.lang.reflect.Method;

public class EventCaller
{
	private Method method;
	private Object object;

	private EventListenerInfo linked;

	public EventListenerInfo getInfo()
	{
		return linked;
	}

	public boolean Call(Event arg)
	{
		try
		{
			method.invoke(object, arg);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public EventCaller(Method method, Object object, EventListenerInfo linked)
	{
		this.method = method;
		this.object = object;
		this.linked = linked;
	}
}

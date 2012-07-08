package com.evanreidland.e;

import com.evanreidland.e.script.Stack;

public class Resource
{
	private ResourceType type;
	protected boolean bValid;
	private long id;

	public Stack info;

	private Object object;

	public Object getObject()
	{
		return object;
	}

	public void setObject(Object newObject, boolean bValid)
	{
		object = newObject;
		this.bValid = bValid;
	}

	public long getID()
	{
		return id;
	}

	public boolean isValid()
	{
		return bValid;
	}

	public ResourceType getType()
	{
		return type;
	}

	/**
	 * Must override.
	 */
	public void reload()
	{
		bValid = false;
	}

	/**
	 * Must override.
	 */
	public void delete()
	{
		bValid = false;
	}

	private Resource()
	{
		this.type = ResourceType.None;
		id = 0;
		bValid = false;
		this.object = new Integer(0);
	}

	public static Resource newInvalid()
	{
		return new Resource();
	}

	public Resource(ResourceType type, Object object, boolean bValid)
	{
		this.type = type;
		id = engine.newID();
		this.bValid = bValid;
		this.object = object;

		info = new Stack();
	}
}

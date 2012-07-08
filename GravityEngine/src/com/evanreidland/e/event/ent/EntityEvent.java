package com.evanreidland.e.event.ent;

import com.evanreidland.e.Flags;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.event.Event;

public class EntityEvent extends Event
{
	private Entity entity;

	public Entity getEntity()
	{
		return entity;
	}

	public String getEntityClass()
	{
		return entity.getClassName();
	}

	public Flags getEntityFlags()
	{
		return entity.flags;
	}

	public EntityEvent(Entity entity)
	{
		super(entity);
		this.entity = entity;
	}
}

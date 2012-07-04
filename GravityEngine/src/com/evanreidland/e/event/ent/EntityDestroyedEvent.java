package com.evanreidland.e.event.ent;

import com.evanreidland.e.ent.Entity;

public class EntityDestroyedEvent extends EntityEvent {
	public EntityDestroyedEvent(Entity ent) {
		super(ent);
	}
}

package com.evanreidland.e.gravity;

import com.evanreidland.e.engine;
import com.evanreidland.e.event.ent.EntitySpawnedEvent;

public class EntityListener {
	public void entitySpawned(EntitySpawnedEvent event) {
		engine.Log("Spawned: " + event.getEntityClass() + " @ " + event.getTime());
	}
}

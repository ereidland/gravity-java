package com.evanreidland.e.ent;

import com.evanreidland.e.Vector3;

public class Projectile extends Entity {
	public Vector3 origin;
	public Entity shooter;
	public float radius, range;
	public Projectile(long id) {
		super("projectile", id);
		shooter = null;
		origin = Vector3.Zero();
		radius = range = 1;
		
		flags.addFlags(eflags.projectile);
	}

}

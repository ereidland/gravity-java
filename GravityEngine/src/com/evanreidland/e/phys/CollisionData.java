package com.evanreidland.e.phys;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;

public class CollisionData
{
	public boolean doesCollide;
	public Entity ent;
	public Vector3 pos, normal;
	
	public CollisionData()
	{
		doesCollide = false;
		ent = null;
		pos = Vector3.Zero();
		normal = Vector3.Zero();
	}
}

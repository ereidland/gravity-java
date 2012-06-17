package com.evanreidland.e.ent;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Vector3;

public class SearchData {
	public Entity ent, other;
	public Flags flags;
	public double length;
	public Vector3 origin;
	public boolean isPositive;
	
	public SearchData() {
		ent = null;
		other = null;
		flags = new Flags();
		length = 0;
		origin = Vector3.Zero();
		isPositive = false;
	}
}

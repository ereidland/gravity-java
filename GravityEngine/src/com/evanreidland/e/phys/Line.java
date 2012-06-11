package com.evanreidland.e.phys;

import com.evanreidland.e.Vector3;


public class Line {
	public Vector3 a, b;
	
	public Vector3 getNormal() {
		return Vector3.fromAngle2d(b.minus(a).getAngle2d() - (float)Math.PI/2); 
	}
	
	public Vector3 nearestPoint(Vector3 origin) {
		Vector3 ap = origin.minus(a);
		Vector3 ab = b.minus(a);
		float abDot = ab.x*ab.x + ab.y*ab.y;
		float apDotab = ap.x*ab.x + ap.y*ab.y;
		float ratio = apDotab / abDot;
		ratio = Math.max(0, ratio);
		ratio = Math.min(ratio, 1);
		return a.plus(ab.multipliedBy(ratio));
	}
	
	public Line() {
		a = Vector3.Zero();
		b = Vector3.Zero();
	}
	
	public Line(Vector3 a, Vector3 b) {
		this.a = a.cloned();
		this.b = b.cloned();
	}
}

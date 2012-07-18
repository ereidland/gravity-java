package com.evanreidland.e.phys;

import com.evanreidland.e.Vector3;

public class Ray
{
	public Vector3 origin, normal;
	
	public Vector3 getPlaneIntersection(Vector3 planeOrigin, Vector3 planeNormal)
	{
		double ndt = planeNormal.dotProduct(normal);
		return ndt != 0 ? origin.plus(normal.multipliedBy(planeNormal
				.dotProduct(planeOrigin.minus(origin)) / ndt)) : origin;
	}
	
	public Vector3 nearestPoint(Vector3 toPoint)
	{
		Vector3 ap = toPoint.minus(origin);
		Vector3 ab = normal;
		double abDot = ab.x * ab.x + ab.y * ab.y;
		double apDotab = ap.x * ab.x + ap.y * ab.y;
		double ratio = apDotab / abDot;
		ratio = Math.max(0, ratio);
		
		return origin.plus(ab.multipliedBy(ratio));
	}
	
	public Ray()
	{
		origin = Vector3.Zero();
		normal = Vector3.Zero();
	}
	
	public Ray(Vector3 origin, Vector3 normal)
	{
		this.origin = origin.cloned();
		this.normal = normal.cloned();
	}
}

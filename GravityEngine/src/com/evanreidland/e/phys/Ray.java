package com.evanreidland.e.phys;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;

public class Ray implements Bitable
{
	public Vector3 origin, normal;
	
	public Vector3 getPlaneIntersection(Vector3 planeOrigin, Vector3 planeNormal)
	{
		double ndt = planeNormal.dot(normal);
		return ndt != 0 ? origin.plus(normal.multipliedBy(planeNormal
				.dot(planeOrigin.minus(origin)) / ndt)) : origin;
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
	
	public Bits toBits()
	{
		return new Bits().write(origin.toBits()).write(normal.toBits());
	}
	
	public void loadBits(Bits bits)
	{
		origin.setAs(Vector3.fromBits(bits));
		normal.setAs(Vector3.fromBits(bits));
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

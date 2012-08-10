package com.evanreidland.e.phys;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;

public class Line implements Bitable
{
	public Vector3 a, b;
	
	public Vector3 getNormal()
	{
		return Vector3.fromAngle2d(b.minus(a).getAngle2d() - (double) Math.PI
				/ 2);
	}
	
	public Vector3 nearestPoint(Vector3 toPoint)
	{
		Vector3 ap = toPoint.minus(a);
		Vector3 ab = b.minus(a);
		double abDot = ab.x * ab.x + ab.y * ab.y;
		double apDotab = ap.x * ab.x + ap.y * ab.y;
		double ratio = apDotab / abDot;
		ratio = Math.max(0, ratio);
		ratio = Math.min(ratio, 1);
		return a.plus(ab.multipliedBy(ratio));
	}
	
	public Vector3 nearestPoint(Line other)
	{
		// a = 1, b = 2, other.a = 3, other.b = 4
		// See: http://paulbourke.net/geometry/lineline3d/
		Vector3 v13 = a.minus(other.a), v43 = other.b.minus(other.a), v21 = b
				.minus(a);
		double d1343 = v13.dot(v43), d4321 = v43.dot(v21), d1321 = v13.dot(v21), d4343 = v43
				.dot(), d2121 = v21.dot();
		double da = (d1343 * d4321) - (d1321 * d4343);
		double db = (d2121 * d4343) - (d4321 * d4321);
		
		if (db != 0)
		{
			double mua = da / db;
			if (mua < 0)
				mua = 0;
			if (mua > 1)
				mua = 1;
			
			return a.plus(v21.multipliedBy(mua));
		}
		
		return null;
	}
	
	public CollisionData testCollision(Line other, double radius)
	{
		CollisionData data = new CollisionData();
		Vector3 pointA = nearestPoint(other), pointB = other.nearestPoint(this);
		
		if (pointA != null && pointB != null)
		{
			double len = pointA.getDistance(pointB);
			data.doesCollide = len <= radius;
		}
		else
		{
			// For the rare case of parallel lines, it's possible to
			// have collision. It's ugly.
			pointA = a;
			pointB = other.a;
			double len = pointA.getDistance(pointB);
			if (!(data.doesCollide = len <= radius))
			{
				pointA = a;
				pointB = other.b;
				len = pointA.getDistance(pointB);
				if (!(data.doesCollide = len <= radius))
				{
					pointA = b;
					pointB = other.a;
					len = pointA.getDistance(pointB);
					if (!(data.doesCollide = len <= radius))
					{
						pointA = b;
						pointB = other.b;
						len = pointA.getDistance(pointB);
						data.doesCollide = len <= radius;
					}
				}
			}
		}
		
		if (data.doesCollide)
		{
			data.pos = pointA;
			data.normal = pointB.minus(pointA).Normalize();
		}
		
		return data;
	}
	
	public CollisionData testCollision(Vector3 point, double radius)
	{
		CollisionData data = new CollisionData();
		Vector3 nearPoint = nearestPoint(point);
		double len = nearPoint.getDistance(point);
		if (data.doesCollide = len <= radius)
		{
			data.pos = nearPoint;
			data.normal = point.minus(nearPoint).Normalize();
		}
		return data;
	}
	
	public Bits toBits()
	{
		return new Bits().write(a.toBits()).write(b.toBits());
	}
	
	public void loadBits(Bits bits)
	{
		a.setAs(Vector3.fromBits(bits));
		b.setAs(Vector3.fromBits(bits));
	}
	
	public Line()
	{
		a = Vector3.Zero();
		b = Vector3.Zero();
	}
	
	public Line(Vector3 a, Vector3 b)
	{
		this.a = a.cloned();
		this.b = b.cloned();
	}
}

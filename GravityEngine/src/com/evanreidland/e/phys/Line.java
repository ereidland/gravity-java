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

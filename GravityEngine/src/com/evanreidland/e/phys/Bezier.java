package com.evanreidland.e.phys;

import java.util.Vector;

import com.evanreidland.e.Vector3;

public class Bezier
{
	public static class Point
	{
		public Vector3 pos, mid;
		
		public Point(Vector3 pos)
		{
			this.pos = pos.cloned();
			mid = Vector3.Zero();
		}
	}
	
	public Vector<Point> points;
	
	public void add(Vector3 p)
	{
		points.add(new Point(p));
	}
	
	public Vector3 get(int index)
	{
		return index >= 0 && index < points.size() ? points.get(index).pos
				: null;
	}
	
	public void clear()
	{
		points.clear();
	}
	
	public int size()
	{
		return points.size();
	}
	
	public Bezier subBezier()
	{
		if (points.size() > 1)
		{
			Bezier sp = new Bezier();
			for (int i = 1; i < points.size(); i++)
			{
				sp.add(points.get(i).mid);
			}
			return sp;
		}
		else
		{
			return null;
		}
	}
	
	public Vector3 calc(double ratio)
	{
		if (points.size() < 2)
		{
			return points.size() > 0 ? points.firstElement().pos.cloned()
					: Vector3.Zero();
		}
		ratio = (ratio < 0) ? 0 : (ratio > 1) ? 1 : ratio;
		if (points.size() == 2)
		{
			Point p = points.get(0);
			Point pp = points.get(1);
			return p.pos.plus(pp.pos.minus(p.pos).multipliedBy(ratio));
		}
		for (int i = 0; i < points.size() - 1; i++)
		{
			Point p = points.get(i);
			Point pp = points.get(i + 1);
			
			pp.mid.setAs(p.pos.plus(pp.pos.minus(p.pos).multipliedBy(ratio)));
		}
		
		Bezier bez = subBezier();
		if (bez != null)
		{
			return bez.calc(ratio);
		}
		else
		{
			return points.lastElement().mid;
		}
	}
	
	public double getRoughLength()
	{
		double len = 0;
		for (int i = 0; i < points.size() - 1; i++)
		{
			len += points.get(i + 1).pos.minus(points.get(i).pos).getLength();
		}
		return len;
	}
	
	public double getLength(int accuracy)
	{
		if (points.size() < 2)
			return 0;
		accuracy = Math.max(2, accuracy);
		
		double len = 0;
		Vector3 lastp = points.get(0).pos;
		for (int i = 1; i <= accuracy; i++)
		{
			double r = (i / (double) accuracy);
			Vector3 newp = calc(r);
			len += lastp.getDistance(newp);
			lastp = newp;
		}
		return len;
	}
	
	public Bezier()
	{
		points = new Vector<Point>();
	}
}

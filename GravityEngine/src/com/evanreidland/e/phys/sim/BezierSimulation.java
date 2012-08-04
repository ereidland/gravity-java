package com.evanreidland.e.phys.sim;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.phys.Bezier;

public class BezierSimulation extends Simulation
{
	public static int precision = 20;
	public Bezier bezier;
	private double ips, len, speed;
	public Entity ent;
	
	public void prime()
	{
		super.prime();
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public void setSpeed(double speed)
	{
		this.speed = speed;
		len = bezier.getLength(precision);
		ips = speed != 0 ? len / speed : 0;
	}
	
	public boolean calc()
	{
		double inc = ips * getTime();
		Vector3 prev = ent.pos.cloned();
		ent.pos.setAs(bezier.calc(inc));
		ent.vel.setAs(ent.pos.minus(prev).divide(Game.getDelta()));
		return inc >= 1 || inc < 0;
	}
	
	public BezierSimulation(Entity ent, Bezier bezier, double speed)
	{
		this.ent = ent;
		this.bezier = bezier;
		this.speed = speed;
		len = bezier.getLength(precision);
		ips = speed != 0 ? len / speed : 0;
		ips = ips != 0 ? 1 / ips : 0;
	}
}

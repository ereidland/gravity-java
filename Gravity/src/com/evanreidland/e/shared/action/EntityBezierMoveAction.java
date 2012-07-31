package com.evanreidland.e.shared.action;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.phys.Bezier;
import com.evanreidland.e.phys.sim.BezierSimulation;

public class EntityBezierMoveAction extends EntityAction
{
	public Vector3 targetPos, targetAngle;
	public double targetSpeed, maxAngleSpeed, maxThrust, maxAngleThrust;
	
	public Bezier bezier;
	public BezierSimulation sim;
	
	public boolean onStart()
	{
		double timeToChange = ent.vel.getDistance(targetPos.minus(ent.pos)
				.getNormal().multipliedBy(targetSpeed))
				/ maxThrust;
		ent.bStatic = true;
		bezier = new Bezier();
		bezier.add(ent.pos);
		bezier.add(ent.pos.plus(ent.vel.multipliedBy(timeToChange)));
		bezier.add(targetPos);
		sim = new BezierSimulation(ent, bezier, targetSpeed);
		return super.onStart();
	}
	
	public Bits toBits()
	{
		Bits bits = super.toBits();
		bits.write(ent.getPosBits());
		bits.write(targetPos.toBits());
		bits.write(targetAngle.toBits());
		
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		super.loadBits(bits);
		ent.loadPosBits(bits);
		targetPos = Vector3.fromBits(bits);
		targetAngle = Vector3.fromBits(bits);
	}
	
	public boolean Update()
	{
		if (super.Update())
			return true;
		
		// Just for now.
		ent.angleVel.setAs(Vector3.Zero());
		Vector3 diff = Vector3.getAngleDifference(ent.angle, targetAngle);
		ent.angle.add(diff.getNormal().multipliedBy(
				Math.min(diff.getLength(), maxAngleThrust * Game.getDelta())));
		
		return sim.increment(Game.getDelta());
	}
	
	public void onEnd(boolean bForced)
	{
		if (ent != null)
			ent.bStatic = false;
	}
	
	public EntityBezierMoveAction()
	{
		super("ent_bezmove", "ent_move");
		ent = null;
		sim = null;
		bezier = null;
		
		targetSpeed = 1;
		maxAngleSpeed = 1;
		maxThrust = 1;
		maxAngleThrust = 1;
	}
	
	public EntityBezierMoveAction(Entity ent, Vector3 targetPos,
			Vector3 targetAngle)
	{
		this();
		this.ent = ent;
		this.targetPos = targetPos.cloned();
		this.targetAngle = targetAngle.cloned();
	}
}

package com.evanreidland.e.shared.action;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.net.Bits;

public class EntityManualMoveAction extends EntityAction
{
	public Vector3 targetAngle;
	public double targetSpeed, targetAngleSpeed;
	
	public boolean onStart()
	{
		getActor().killOthers(getType());
		return false;
	}
	
	public boolean Update()
	{
		if (super.Update())
			return true;
		
		double maxAngleThrust = 1, maxThrust = 1;
		
		// TODO: Entities knowing their thrust capabilities for specific
		// actions.
		Vector3 diff = Vector3.getAngleDifference(ent.angle, targetAngle);
		ent.angle.add(diff.getNormal().multipliedBy(
				Math.min(diff.getLength(), maxAngleThrust * Game.getDelta())));
		
		diff = ent.angle.getForward().multipliedBy(targetSpeed).minus(ent.vel);
		ent.vel.add(diff.getNormal().multipliedBy(
				Math.min(diff.getLength(), maxThrust * Game.getDelta())));
		
		return false;
	}
	
	public Bits toBits()
	{
		Bits bits = super.toBits();
		bits.write(ent.getPosBits());
		bits.write(targetAngle.toBits());
		bits.writeDouble(targetSpeed);
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		super.loadBits(bits);
		ent.loadPosBits(bits);
		targetAngle = Vector3.fromBits(bits);
		targetSpeed = bits.readDouble();
	}
	
	public EntityManualMoveAction()
	{
		super("ent_manmove", "ent_move");
		isOrdered = false;
		
		targetAngle = Vector3.Zero();
		
		targetSpeed = 1;
		targetAngleSpeed = 1;
	}
	
	public EntityManualMoveAction(Entity ent, Vector3 targetAngle,
			double targetSpeed)
	{
		this();
		this.ent = ent;
		this.targetAngle = targetAngle.cloned();
		this.targetSpeed = targetSpeed;
	}
}

package com.evanreidland.e.shared.action;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.net.Bits;

public class EntityMoveAction extends EntityAction
{
	public Vector3 targetPos, targetAngle;
	public double targetSpeed, closingDistance, targetAngleSpeed;
	
	public boolean onStart()
	{
		return false;
	}
	
	public boolean Update()
	{
		if (super.Update() || ent.pos.getDistance(targetPos) < closingDistance)
			return true;
		
		// TODO: Entities knowing their thrust capabilities for specific
		// actions.
		double transThrust = 1, angleThrust = 2;
		
		ent.thrustTowards(targetPos, targetSpeed, transThrust, Game.getDelta());
		ent.thrustTowardsAngle(targetAngle, targetAngleSpeed, angleThrust,
				Game.getDelta());
		
		return false;
	}
	
	public Bits toBits()
	{
		Bits bits = super.toBits();
		bits.write(ent.getPosBits());
		bits.write(targetPos.toBits());
		bits.write(targetAngle.toBits());
		bits.writeDouble(closingDistance);
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		super.loadBits(bits);
		ent.loadPosBits(bits);
		targetPos = Vector3.fromBits(bits);
		targetAngle = Vector3.fromBits(bits);
		closingDistance = bits.readDouble();
	}
	
	public EntityMoveAction()
	{
		super("ent_move", "ent_move");
		isOrdered = true;
		
		targetPos = Vector3.Zero();
		targetAngle = Vector3.Zero();
		
		closingDistance = 0.005;
		targetSpeed = 1;
		targetAngleSpeed = 1;
	}
	
	public EntityMoveAction(Entity ent, Vector3 targetPos, Vector3 targetAngle)
	{
		this();
		this.ent = ent;
		this.targetPos = targetPos.cloned();
		this.targetAngle = targetAngle.cloned();
	}
}

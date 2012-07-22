package com.evanreidland.e.shared.action;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.net.Bits;

public class EntityMoveAction extends EntityAction
{
	public Vector3 targetPos;
	public double targetSpeed, closingDistance, targetAngleSpeed;
	
	public boolean onStart()
	{
		getActor().killOthers(getName());
		return false;
	}
	
	public boolean Update()
	{
		if (super.Update() || ent.pos.getDistance(targetPos) < closingDistance)
			return true;
		
		// TODO: Entities knowing their thrust capabilities for specific
		// actions.
		ent.thrustTowards(targetPos, targetSpeed, 1, Game.getDelta());
		ent.thrustTowardsAngle(targetPos.minus(ent.pos).getAngle(),
				targetAngleSpeed, 1, Game.getDelta());
		
		return false;
	}
	
	public Bits toBits()
	{
		Bits bits = super.toBits();
		bits.write(ent.getPosBits());
		bits.write(targetPos.toBits());
		bits.writeDouble(closingDistance);
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		super.loadBits(bits);
		ent.loadPosBits(bits);
		targetPos = Vector3.fromBits(bits);
		closingDistance = bits.readDouble();
	}
	
	public EntityMoveAction()
	{
		super("ent_move", "ent_move");
		isOrdered = true;
		
		closingDistance = 0.1;
		targetSpeed = 1;
		targetAngleSpeed = 1;
	}
	
	public EntityMoveAction(Entity ent, Vector3 targetPos)
	{
		this();
		this.ent = ent;
		this.targetPos = targetPos.cloned();
		
	}
}

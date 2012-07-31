package com.evanreidland.e.phys;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.net.Bits;

public abstract class MovementSimulation extends Simulation
{
	public Vector3 pos, vel, angle, angleVel;
	public Entity ent;
	
	public Bits toBits()
	{
		Bits bits = super.toBits();
		bits.write(pos.toBits());
		bits.write(vel.toBits());
		bits.write(angle.toBits());
		bits.write(angleVel.toBits());
		bits.writeLong(ent.getID());
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		super.loadBits(bits);
		pos = Vector3.fromBits(bits);
		vel = Vector3.fromBits(bits);
		angle = Vector3.fromBits(bits);
		angleVel = Vector3.fromBits(bits);
		ent = ents.get(bits.readLong());
	}
	
	public void prime()
	{
		super.prime();
		ent.pos.setAs(pos);
		ent.vel.setAs(vel);
	}
	
	public void calc()
	{
		ent.pos.setAs(pos.plus(vel.multipliedBy(getTime())));
		ent.vel.setAs(vel);
		ent.angle.setAs(angle.plus(angleVel.multipliedBy(getTime()))
				.clipAngle());
		ent.angleVel.setAs(angleVel);
	}
	
	public MovementSimulation(Entity ent)
	{
		this.ent = ent;
		pos = ent.pos.cloned();
		vel = ent.vel.cloned();
		angle = ent.angle.cloned();
		angleVel = ent.angleVel.cloned();
	}
}

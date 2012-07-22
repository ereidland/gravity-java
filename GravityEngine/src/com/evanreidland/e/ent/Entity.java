package com.evanreidland.e.ent;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Flags.State;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.action.Actor;
import com.evanreidland.e.event.Event;
import com.evanreidland.e.event.ent.EntitySpawnedEvent;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.net.Bits;

public class Entity extends Actor
{
	public static boolean debug = false;
	public Vector3 pos, vel, angle, angleVel;
	
	public double radius, mass;
	
	public boolean bStatic, bSpawned, bDead, bSent;
	
	private String className;
	
	public boolean matchesFlags(Flags oflags, boolean strict)
	{
		return flags.matchesOther(oflags, strict);
	}
	
	public String getClassName()
	{
		return className;
	}
	
	public String toString()
	{
		return getClassName() + "/" + getID();
	}
	
	public void thrustTowards(Vector3 point, double targetSpeed,
			double maxThrust, double delta)
	{
		Vector3 targetVel = point.minus(pos).Normalize()
				.multipliedBy(targetSpeed);
		Vector3 diff = targetVel.minus(vel);
		vel.add(diff.getNormal().multipliedBy(
				Math.min(diff.getLength(), maxThrust) * delta));
	}
	
	public void thrustTowardsAngle(Vector3 angle, double targetAngleSpeed,
			double maxAngleThrust, double delta)
	{
		Vector3 targetVel = Vector3.getAngleDifference(this.angle, angle)
				.Normalize().multipliedBy(targetAngleSpeed);
		Vector3 diff = targetVel.minus(angleVel);
		angleVel.add(diff.getNormal().multipliedBy(
				Math.min(diff.getLength(), maxAngleThrust) * delta));
	}
	
	public void Kill()
	{
		bDead = true;
	}
	
	public boolean isAlive()
	{
		return !bDead;
	}
	
	public boolean isDead()
	{
		return bDead;
	}
	
	public void Receive(Bits data)
	{
		while (data.getRemainingBytes() > 0)
		{
			byte b = data.readByte();
			EntityMessageCode code = b >= 0
					&& b < EntityMessageCode.values().length ? EntityMessageCode
					.values()[b] : null;
			switch (code)
			{
				case POSITION:
					pos.setAs(data.readDouble(), data.readDouble(),
							data.readDouble());
					break;
				case VELOCITY:
					vel.setAs(data.readDouble(), data.readDouble(),
							data.readDouble());
					break;
				case MASS:
					mass = data.readDouble();
					break;
				case RADIUS:
					radius = data.readDouble();
					break;
				case STATIC:
					bStatic = data.readBit();
					break;
				case CUSTOM:
					onReceive(data);
					break;
				default:
					return;
			}
		}
	}
	
	public void onReceive(Bits data)
	{
		
	}
	
	public void onThink()
	{
		super.Update();
		State f = flags.get("dead");
		if (!bStatic
				&& (f == State.False || f == State.Undef || f == State.Either))
		{
			pos.add(vel.multipliedBy(engine.getDelta()));
			angle.add(angleVel.multipliedBy(engine.getDelta()));
			angle.clipAngle();
		}
	}
	
	public void onDie()
	{
		killActions();
	}
	
	public double getOrbitalVelocity(double distance, double sourceMass)
	{
		if (distance == 0)
			return 0; // Better than returning infinity.
		return (double) Math.sqrt(sourceMass / distance);
	}
	
	public double getOrbitalVelocity(Entity other)
	{
		return getOrbitalVelocity(pos.getDistance(other.pos), other.mass);
	}
	
	public double getGravity(double distance, double sourceMass)
	{
		if (distance == 0)
			return 0;
		return (mass * sourceMass) / (distance * distance);
	}
	
	public double getGravity(Vector3 source, double sourceMass)
	{
		double radius = source.getDistance(pos);
		return (mass * sourceMass) / (radius * radius);
	}
	
	public double getGravity(Entity other)
	{
		return getGravity(other.pos, other.mass);
	}
	
	public void applyGravity(Vector3 source, double sourceMass, double delta)
	{
		if (mass != 0)
		{
			vel.add(source.minus(pos).getNormal()
					.multiply((getGravity(source, sourceMass) * delta) / mass));
		}
	}
	
	public void shiftByDelta(double delta)
	{
		pos.add(vel.multipliedBy(delta));
	}
	
	public void shiftByDeltaMS(long delta)
	{
		shiftByDelta(delta / 1000d);
	}
	
	public void shiftByTimeOffset(long timeReference)
	{
		shiftByDeltaMS(System.currentTimeMillis() - timeReference);
	}
	
	public void applyGravity(Entity other, double delta)
	{
		applyGravity(other.pos, other.mass, delta);
	}
	
	// Possibly going to remove this. If something should be rendered, it should
	// use a SceneObject.
	public void onRender()
	{
		if (debug)
		{
			graphics.unbindTexture();
			graphics.drawLine(pos, pos.plus(vel), 1, 1, 0, 0, 0.5f);
			
			graphics.drawLine(pos, pos.plus(ents.list
					.getGravitationalInfluence(this).multiply(10 / mass)), 1,
					0, 0, 1, 0.5f);
		}
	}
	
	public void onRenderHUD()
	{
		
	}
	
	public void Spawn()
	{
		if (getID() == 0)
		{
			ents.list.add(this);
		}
		bSpawned = true;
		onSpawn();
		Event.Call("onSpawn", new EntitySpawnedEvent(this));
		
	}
	
	public void onSpawn()
	{
		
	}
	
	// Potential break. I'm not sure I like this, but it's useful.
	public void Setup(Object[] args)
	{
		
	}
	
	protected Object getArg(Object[] args, int index, Object def)
	{
		return index >= 0 && index < args.length ? args[index] : def;
	}
	
	public Bits toBits()
	{
		boolean tSpawned = bSpawned;
		bSpawned = false;
		Bits bits = super.toBits();
		bSpawned = tSpawned;
		
		bits.writeBit(bStatic);
		bits.write(pos.toBits());
		bits.write(angle.toBits());
		if (!bStatic)
		{
			bits.write(vel.toBits());
			bits.write(angleVel.toBits());
		}
		bits.writeDouble(mass);
		bits.writeDouble(radius);
		
		return bits;
	}
	
	public Bits getPosBits()
	{
		Bits bits = new Bits();
		bits.write(pos.toBits());
		bits.write(vel.toBits());
		bits.write(angle.toBits());
		bits.write(angleVel.toBits());
		return bits;
	}
	
	public void loadPosBits(Bits bits)
	{
		pos.setAs(Vector3.fromBits(bits));
		vel.setAs(Vector3.fromBits(bits));
		angle.setAs(Vector3.fromBits(bits));
		angleVel.setAs(Vector3.fromBits(bits));
	}
	
	// Note: These should always be read in the order they are sent. If
	// extending a class that has additional data to send, call
	// super.loadBits before the other code. Same goes for toBits.
	
	public void loadBits(Bits bits)
	{
		super.loadBits(bits);
		bStatic = bits.readBit();
		pos.setAs(Vector3.fromBits(bits));
		angle.setAs(Vector3.fromBits(bits));
		if (!bStatic)
		{
			vel.setAs(Vector3.fromBits(bits));
			angleVel.setAs(Vector3.fromBits(bits));
		}
		mass = bits.readDouble();
		radius = bits.readDouble();
	}
	
	public Entity(String className, long id)
	{
		super(id);
		this.className = className;
		
		pos = Vector3.Zero();
		vel = Vector3.Zero();
		angle = Vector3.Zero();
		angleVel = Vector3.Zero();
		
		bStatic = false;
		bDead = false;
		bSpawned = false;
		bSent = false;
		
		flags.addFromObject(this, "bStatic", "static");
		flags.addFromObject(this, "bDead", "dead");
		flags.addFromObject(this, "bSpawned", "spawned");
		flags.addFromObject(this, "bSent", "sent");
	}
	
	public Entity(String className)
	{
		this(className, 0);
	}
}

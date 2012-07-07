package com.evanreidland.e.ent;

import com.evanreidland.e.Flags.State;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.event.Event;
import com.evanreidland.e.event.ent.EntitySpawnedEvent;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.net.Bits;

public class Entity extends EObject
{
	public static boolean debug = false;
	public Vector3 pos, vel, angle, angleVel;

	public double radius, mass;

	public boolean bStatic, bSpawned;

	public String toString()
	{
		return getClassName() + "/" + getID();
	}

	public void Kill()
	{
		flags.setState("dead", State.True);
	}

	public boolean isAlive()
	{
		return flags.getState("dead") == State.False;
	}

	public boolean isDead()
	{
		return flags.getState("dead") == State.True;
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
		State f = flags.getState("dead");
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
		bSpawned = true;
		Event.Call("onSpawn", new EntitySpawnedEvent(this));
		onSpawn();
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

	public Entity(String className)
	{
		super(className);
	}

	public Entity(String className, long id)
	{
		super(className, id);
		pos = Vector3.Zero();
		vel = Vector3.Zero();
		angle = Vector3.Zero();
		angleVel = Vector3.Zero();
		bStatic = false;

		flags.setState("dead", State.False);
	}
}

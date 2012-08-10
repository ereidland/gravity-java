package com.evanreidland.e.ent;

import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.event.Event;
import com.evanreidland.e.event.ent.EntityDestroyedEvent;
import com.evanreidland.e.phys.CollisionData;
import com.evanreidland.e.phys.Line;

public class EntityList
{
	private Vector<Entity> entities;
	private HashMap<Long, Entity> entMap;
	public double ignoreThreshold = 0.001;
	
	private boolean isMaster;
	
	public void simulateGravity(double delta)
	{
		for (int i = 0; i < entities.size(); i++)
		{
			Entity ent = entities.get(i);
			if (!ent.bStatic && Math.abs(ent.mass) > ignoreThreshold)
			{
				for (int j = 0; j < entities.size(); j++)
				{
					if (i == j)
						continue;
					Entity other = entities.get(j);
					ent.applyGravity(other, delta);
				}
			}
		}
	}
	
	public Vector3 getGravitationalInfluence(Entity ent)
	{
		Vector3 v = Vector3.Zero();
		if (ent.mass == 0)
			return v;
		for (int i = 0; i < entities.size(); i++)
		{
			Entity other = entities.get(i);
			if (other.getID() != ent.getID())
				v.add(other.pos.minus(ent.pos).Normalize()
						.multiply(ent.getGravity(other)));
		}
		return v;
	}
	
	public Vector3 getUniversalOrbitalVelocity(Entity ent)
	{
		Vector3 v = Vector3.Zero();
		if (ent.mass == 0)
			return v;
		for (int i = 0; i < entities.size(); i++)
		{
			Entity other = entities.get(i);
			if (other.getID() != ent.getID())
			{
				v.add(other.pos.minus(ent.pos).getAngle().getRight()
						.multiply(ent.getOrbitalVelocity(other)));
			}
		}
		return v;
	}
	
	public Entity add(Entity ent)
	{
		if (!entities.contains(ent))
		{
			entities.add(ent);
			if (ent.getID() == 0)
			{
				ent.Be();
			}
			entMap.put(ent.getID(), ent);
			Event.addPersonalListener(ent);
		}
		return ent;
	}
	
	public Entity remove(Entity ent)
	{
		Event.Call("onDestroy", new EntityDestroyedEvent(ent));
		entities.remove(ent);
		entMap.remove(ent.getID());
		if (isMaster)
			ent.onDie();
		return ent;
	}
	
	public Entity get(int index)
	{
		return index >= 0 && index < entities.size() ? entities.get(index)
				: null;
	}
	
	public Entity getByID(long id)
	{
		return entMap.get(id);
	}
	
	public EntityList getWithFlags(Flags flags, boolean strict)
	{
		EntityList list = new EntityList();
		for (int i = 0; i < entities.size(); i++)
		{
			Entity ent = entities.get(i);
			if (ent.matchesFlags(flags, strict))
				list.add(ent);
		}
		
		return list;
	}
	
	public EntityList getWithFlags(Flags flags)
	{
		return getWithFlags(flags, true);
	}
	
	public EntityList getWithinRadius(Vector3 pos, double radius)
	{
		EntityList list = new EntityList();
		for (int i = 0; i < entities.size(); i++)
		{
			Entity ent = entities.get(i);
			if (ent.pos.getDistance(pos) <= radius)
				list.add(ent);
		}
		return list;
	}
	
	public EntityList getWithinBounds(Vector3 pos, double entRadius,
			long ignoreID)
	{
		EntityList list = new EntityList();
		for (int i = 0; i < entities.size(); i++)
		{
			Entity ent = entities.get(i);
			if (ent.getID() != ignoreID
					&& ent.pos.getDistance(pos) <= entRadius + ent.radius)
				list.add(ent);
		}
		return list;
	}
	
	public EntityList getWithinBounds(Vector3 pos, double entRadius)
	{
		return getWithinBounds(pos, entRadius, 0);
	}
	
	public EntityList getWithinTimeBounds(Vector3 pos, Vector3 vel,
			double entRadius, long ignoreID, double delta)
	{
		EntityList list = new EntityList();
		Line line1 = new Line(pos, pos.minus(vel.multipliedBy(delta)));
		for (int i = 0; i < entities.size(); i++)
		{
			Entity ent = entities.get(i);
			Line line2 = new Line(ent.pos, ent.pos.minus(ent.vel
					.multipliedBy(delta)));
			CollisionData data = line1.testCollision(line2, entRadius
					+ ent.radius);
			
			if (data.doesCollide)
				list.add(ent);
		}
		return list;
	}
	
	public EntityList getWithinTimeBounds(Vector3 pos, Vector3 vel,
			double entRadius, double delta)
	{
		return getWithinTimeBounds(pos, vel, entRadius, 0, delta);
	}
	
	public void removeWithFlags(Flags flags, boolean strict)
	{
		int i = 0;
		
		while (i < entities.size())
		{
			Entity ent = entities.get(i);
			if (ent.matchesFlags(flags, strict))
			{
				if (isMaster)
					ent.onDie();
				remove(ent);
			}
			else
				i++;
		}
	}
	
	public void killAll()
	{
		for (int i = 0; i < entities.size(); i++)
		{
			Entity ent = entities.get(i);
			ent.Kill();
			ent.onDie();
		}
		entities.clear();
	}
	
	public void removeWithFlags(Flags flags)
	{
		removeWithFlags(flags, false);
	}
	
	public void onThink()
	{
		for (int i = 0; i < entities.size(); i++)
		{
			Entity ent = entities.get(i);
			if (!ent.bSpawned)
			{
				ent.Spawn();
				ent.bSpawned = true;
			}
			ent.onThink();
		}
		
		removeWithFlags(eflags.dead);
	}
	
	public void checkCollision()
	{
		for (int i = 0; i < entities.size(); i++)
		{
			Entity ent = entities.get(i);
			if (ent.bSpawned && !ent.isDead())
			{
				ent.checkCollision();
			}
		}
		
		removeWithFlags(eflags.dead);
	}
	
	public void onRender()
	{
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).onRender();
	}
	
	public void onRenderHUD()
	{
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).onRenderHUD();
	}
	
	public int size()
	{
		return entities.size();
	}
	
	public boolean isEmpty()
	{
		return entities.isEmpty();
	}
	
	public SearchData traceToNearest(Vector3 start, Vector3 end, double radius,
			Flags flags)
	{
		SearchData data = new SearchData();
		Line line = new Line(start, end);
		for (int i = 0; i < entities.size(); i++)
		{
			Entity ent = entities.get(i);
			
			if (flags == null || ent.matchesFlags(flags, true))
			{
				Vector3 nearPos = line.nearestPoint(ent.pos);
				if (nearPos.minus(ent.pos).getLength2d() <= ent.radius + radius)
				{
					double len = ent.pos.minus(start).getLength2d()
							- (ent.radius + radius);
					if (!data.isPositive || len < data.length)
					{
						data.length = len;
						data.isPositive = true;
						data.ent = ent;
						data.origin.setAs(start);
					}
				}
			}
		}
		return data;
	}
	
	public SearchData findNearest(Vector3 origin, double radius, Flags flags)
	{
		SearchData data = new SearchData();
		for (int i = 0; i < entities.size(); i++)
		{
			Entity ent = entities.get(i);
			
			if (flags != null && !ent.matchesFlags(flags, true))
				continue;
			
			double len = ent.pos.minus(origin).getLength2d() - ent.radius;
			
			if (len < radius)
			{
				if (!data.isPositive || len < data.length)
				{
					data.length = len;
					data.isPositive = true;
					data.ent = ent;
					data.origin.setAs(origin);
				}
			}
		}
		return data;
	}
	
	public SearchData findNearest(Vector3 origin)
	{
		SearchData data = new SearchData();
		for (int i = 0; i < entities.size(); i++)
		{
			Entity ent = entities.get(i);
			
			double len = ent.pos.minus(origin).getLength2d() - ent.radius;
			
			if (!data.isPositive || len < data.length)
			{
				data.length = len;
				data.isPositive = true;
				data.ent = ent;
				data.origin.setAs(origin);
			}
		}
		return data;
	}
	
	public EntityList()
	{
		entities = new Vector<Entity>();
		entMap = new HashMap<Long, Entity>();
		
		isMaster = false;
	}
	
	public EntityList(boolean isMaster)
	{
		this();
		this.isMaster = isMaster;
	}
}

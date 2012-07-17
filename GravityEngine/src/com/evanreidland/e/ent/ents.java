package com.evanreidland.e.ent;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;

public class ents
{
	public static EntityList list = new EntityList();
	private static long lastID = 1;
	
	public static long getLastID()
	{
		return lastID;
	}
	
	private static class ClassFactory
	{
		Constructor<?> cons;
		
		public Entity Create()
		{
			try
			{
				Entity ent = (Entity) cons.newInstance(++lastID);
				if (ent != null)
				{
					ent.Be();
				}
				if (list != null)
				{
					list.add(ent);
				}
				return ent;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return null;
		}
		
		public Entity CreateForced(long forcedID)
		{
			try
			{
				Entity ent = (Entity) cons.newInstance(forcedID);
				if (ent != null)
				{
					ent.Be(forcedID);
				}
				if (list != null)
				{
					list.add(ent);
				}
				return ent;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return null;
		}
		
		public ClassFactory(Class<? extends Entity> factoryClass)
		{
			Constructor<?> consList[] = factoryClass.getConstructors();
			cons = null;
			for (int i = 0; i < consList.length; i++)
			{
				if (consList[i].getParameterTypes().length == 1
						&& consList[i].getParameterTypes()[0]
								.equals(long.class))
				{
					cons = consList[i];
				}
			}
		}
	}
	
	private static HashMap<String, ClassFactory> factories = new HashMap<String, ClassFactory>();
	
	public static void Register(String className,
			Class<? extends Entity> entClass)
	{
		engine.Log("Regsitered \"" + entClass.getName() + "\" to \""
				+ className + "\".");
		factories.put(className, new ClassFactory(entClass));
	}
	
	public static Entity Create(String className)
	{
		ClassFactory factory = factories.get(className);
		return factory != null ? factory.Create() : null;
	}
	
	public static Entity CreateLocal(String className)
	{
		ClassFactory factory = factories.get(className);
		return factory != null ? factory.CreateForced(-(++lastID)) : null;
	}
	
	// Note: Should only be called client-side after a signal from the server.
	public static Entity Create(Entity ent, long targetID)
	{
		if (list != null)
		{
			ent.Be(targetID);
			list.add(ent);
		}
		return ent;
	}
	
	public static Entity Create(Entity ent)
	{
		return Create(ent, ++lastID);
	}
	
	public static Entity CreateLocal(Entity ent)
	{
		return Create(ent, -(++lastID));
	}
	
	// Note: Should only be called client-side after a signal from the server.
	public static Entity createWithID(String className, long forcedID)
	{
		ClassFactory factory = factories.get(className);
		return factory != null ? factory.CreateForced(forcedID) : null;
	}
	
	public static Entity Create(String className, Object[] args)
	{
		Entity ent = Create(className);
		if (ent != null)
		{
			ent.Setup(args);
		}
		return ent;
	}
	
	public static Entity createWithID(String className, long forcedID,
			Object[] args)
	{
		Entity ent = createWithID(className, forcedID);
		if (ent != null)
		{
			ent.Setup(args);
		}
		return ent;
	}
	
	public static Entity CreateLocal(String className, Object[] args)
	{
		Entity ent = CreateLocal(className);
		if (ent != null)
		{
			ent.Setup(args);
		}
		return ent;
	}
	
	public static Entity get(long id)
	{
		return list.getByID(id);
	}
	
	public static SearchData traceToNearest(Vector3 start, Vector3 end,
			double radius, Flags flags)
	{
		return list != null ? list.traceToNearest(start, end, radius, flags)
				: new SearchData();
	}
	
	public static SearchData findNearest(Vector3 origin, double radius,
			Flags flags)
	{
		return list != null ? list.findNearest(origin, radius, flags)
				: new SearchData();
	}
}

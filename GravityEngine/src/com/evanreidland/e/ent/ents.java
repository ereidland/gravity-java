package com.evanreidland.e.ent;

import java.util.HashMap;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;

public class ents {
	public static EntityList list = new EntityList();
	private static class ClassFactory {
		Class<? extends Entity> factoryClass;
		public Entity Create() {
			try {
				Entity ent = (Entity)factoryClass.getConstructors()[0].newInstance(ID.newID());
				if ( ent != null ) {
					ent.Be();
				}
				if ( list != null ) {
					list.add(ent);
				}
				return ent;
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			
			return null;
		}
		public Entity CreateForced(long forcedID) {
			try {
				Entity ent = (Entity)factoryClass.getConstructors()[0].newInstance(forcedID);
				if ( ent != null ) {
					ent.beForced();
				}
				if ( list != null ) {
					list.add(ent);
				}
				return ent;
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			
			return null;
		}
		public ClassFactory(Class<? extends Entity> factoryClass) {
			this.factoryClass = factoryClass;
		}
	}
	
	private static HashMap<String, ClassFactory> factories = new HashMap<String, ClassFactory>();
	
	public static void Register(String className, Class<? extends Entity> entClass) {
		engine.Log("Regsitered " + entClass.toString() + " to \"" + className + "\".");
		factories.put(className, new ClassFactory(entClass));
	}
	
	public static Entity Create(String className) {
		ClassFactory factory = factories.get(className);
		return factory != null ? factory.Create() : null;
	}
	
	// Note: Should only be called client-side after a signal from the server.
	public static Entity Create(Entity ent, long targetID) {
		if ( list != null ) {
			ent.Be(targetID);
			list.add(ent);
		}
		return ent;
	}
	
	public static Entity Create(Entity ent) {
		return Create(ent, ID.newID());
	}
	
	// Note: Should only be called client-side after a signal from the server.
	public static Entity createWithID(String className, long forcedID) {
		ClassFactory factory = factories.get(className);
		return factory != null ? factory.CreateForced(forcedID) : null;
	}
	
	public static Entity Create(String className, Object[] args) {
		Entity ent = Create(className);
		if ( ent != null ) {
			ent.Setup(args);
		}
		return ent;
	}
	
	public static Entity createWithID(String className, long forcedID, Object[] args) {
		Entity ent = createWithID(className, forcedID);
		if ( ent != null ) {
			ent.Setup(args);
		}
		return ent;
	}
	
	public static Entity get(long id) {
		return list.getByID(id);
	}
	
	
	public static SearchData traceToNearest(Vector3 start, Vector3 end, double radius, Flags flags) {
		return list != null ? list.traceToNearest(start, end, radius, flags) : new SearchData();
	}
	
	public static SearchData findNearest(Vector3 origin, double radius, Flags flags) {
		return list != null ? list.findNearest(origin, radius, flags) : new SearchData();
	}
}

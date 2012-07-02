package com.evanreidland.e.ent;

import java.util.HashMap;

public class ID {
	private static HashMap<Long, EObject> usedIDs = new HashMap<Long, EObject>();
	
	private static long curID = 0;
	
	public EObject getObject(long id) {
		EObject o = usedIDs.get(id);
		return o != null ? o : new EObject("null", 0);
	}
	
	public static long newID() {
		do {
			curID++;
		} while ( usedIDs.containsKey(curID) );
		
		return curID;
	}
	
	protected static void forceID(long forcedID, EObject object) {
		usedIDs.put(forcedID, object);
	}
	
	public static void removeID(long id) {
		usedIDs.remove(id);
	}
	
	public static long registerID(long desired, EObject object) {
		while ( usedIDs.containsKey(desired) ) {
			desired++;
		}
		
		usedIDs.put(desired, object);
		
		return desired;
	}
}

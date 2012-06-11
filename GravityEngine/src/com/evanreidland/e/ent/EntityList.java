package com.evanreidland.e.ent;

import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.phys.Line;

public class EntityList {
	private Vector<Entity> entities;
	private HashMap<Long, Entity> entMap;
	
	public void simulateGravity(float delta) {
		for ( int i = 0; i < entities.size(); i++ ) {
			for ( int j = 0; j < entities.size(); j++ ) {
				if ( i == j ) continue;
				Entity ent = entities.get(i);
				if ( !ent.bStatic ) {
					Entity other = entities.get(j);
					ent.applyGravity(other, delta);
				}
			}
		}
	}
	
	public Entity add(Entity ent) {
		if ( !entities.contains(ent) ) {
			entities.add(ent);
			entMap.put(ent.getID(), ent);
		}
		return ent;
	}
	
	public Entity remove(Entity ent) {
		entities.remove(ent);
		entMap.remove(ent.getID());
		return ent;
	}
	
	public void removeWithFlags(Flags flags, boolean strict) {
		int i = 0;
		while ( i < entities.size() ) {
			if ( entities.get(i).matchesFlags(flags, strict) ) {
				entities.remove(i);
			} else {
				i++;
			}
		}
	}
	public void removeWithFlags(Flags flags) {
		removeWithFlags(flags, false);
	}
	
	public void onThink() {
		for ( int i = 0; i < entities.size(); i++ ) {
			entities.get(i).onThink();
		}
	}
	
	public void onRender() {
		for ( int i = 0; i < entities.size(); i++ ) {
			entities.get(i).onRender();
		}
	}
	
	public void onRenderHUD() {
		for ( int i = 0; i < entities.size(); i++ ) {
			entities.get(i).onRenderHUD();
		}
	}
	
	public int getSize() {
		return entities.size();
	}

	public SearchData traceToNearest(Vector3 start, Vector3 end, float radius, Flags flags) {
		SearchData data = new SearchData();
		Line line = new Line(start, end);
		for ( int i = 0; i < entities.size(); i++ ) {
			Entity ent = entities.get(i);
			
			if ( flags == null || ent.matchesFlags(flags, false) ) {
				Vector3 nearPos = line.nearestPoint(ent.pos);
				if ( nearPos.minus(ent.pos).getLength2d() <= ent.radius + radius ) {
					System.out.println("Possible match:" + ent.getID());
					float len = ent.pos.minus(start).getLength2d() - (ent.radius + radius);
					if ( !data.isPositive || len < data.length ) {
						System.out.println("Match:" + ent.getID());
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
	
	public SearchData findNearest(Vector3 origin, float radius, Flags flags) {
		SearchData data = new SearchData();
		for ( int i = 0; i < entities.size(); i++ ) {
			Entity ent = entities.get(i);
			
			if ( flags != null && !ent.matchesFlags(flags, false) ) {
				continue;
			}
			
			float len = ent.pos.minus(origin).getLength2d() - ent.radius;
			
			if ( len < radius ) {
				if ( !data.isPositive || len < data.length ) {
					data.length = len;
					data.isPositive = true;
					data.ent = ent;
					data.origin.setAs(origin);
				}
			}
		}
		return data;
	}
	
	public EntityList() {
		entities = new Vector<Entity>();
		entMap = new HashMap<Long, Entity>();
	}
}

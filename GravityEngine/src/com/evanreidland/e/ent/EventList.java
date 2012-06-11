package com.evanreidland.e.ent;

import java.util.HashMap;
import java.util.Vector;

public class EventList {
	private HashMap<String, Vector<Event>> events;
	
	public void clearEvent(String name) {
		Vector<Event> list = events.get(name);
		if ( list != null ) {
			list.clear();
		}
	}
	
	public void addEvent(String name, Event event) {
		Vector<Event> list = events.get(name);
		if ( list != null ) {
			list.add(event);
		} else {
			list = new Vector<Event>();
			list.add(event);
			events.put(name, list);
		}
	}
	
	public void callEvent(String name, Object args) {
		Vector<Event> list = events.get(name);
		if ( list != null ) {
			for ( int i = 0; i < list.size(); i++ ) {
				list.get(i).Call(args);
			}
		} 
	}

	public EventList() {
		events = new HashMap<String, Vector<Event>>();
	}
}

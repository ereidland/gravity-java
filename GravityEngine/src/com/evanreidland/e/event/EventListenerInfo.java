package com.evanreidland.e.event;

import java.util.Vector;

public class EventListenerInfo {
	public Vector<String> linkedEvents;
	
	public Object requiredObject;
	private Object listener;
	
	public Object getListener() {
		return listener;
	}
	
	public void add(String linked) {
		linkedEvents.add(linked);
	}
	public void remove() {
		Event.removeListener(listener);
	}
	
	public void unlisten(String event) {
		Event.removeListener(listener, event);
	}
	
	public EventListenerInfo(Object listener, Object requiredObject) {
		this.requiredObject = requiredObject;
		this.listener = listener;
		
		linkedEvents = new Vector<String>();
	}
}

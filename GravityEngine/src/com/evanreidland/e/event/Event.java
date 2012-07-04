package com.evanreidland.e.event;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Event {
	//Normal object.
	private boolean bCanceled;
	private long time;
	
	public long getTime() {
		return time;
	}
	
	public void setCanceled(boolean state) {
		bCanceled = state;
	}
	
	public boolean isCanceled() {
		return bCanceled;
	}
	
	public void cancel() {
		bCanceled = true;
	}
	
	public Event() {
		bCanceled = false;
		time = System.currentTimeMillis();
	}
	
	//Static use.
	
	private static HashMap<String, EventManager> managers = new HashMap<String, EventManager>();
	private static HashMap<String, String> classRegistry = new HashMap<String, String>();
	
	public static void setManager(String eventName, EventManager manager) {
		EventManager oldManager = managers.get(eventName);
		if ( oldManager != null ) {
			classRegistry.remove(oldManager.getClass().toString());
		}
		managers.put(eventName, manager);
		classRegistry.put(manager.getEventClass().toString(), eventName);
	}
	
	public static void setManager(String eventName, Class<? extends Event> eventClass) {
		setManager(eventName, new EventManager(eventClass));
	}
	
	//Returns true if the event was cancelled.
	public static boolean Call(String eventName, Event event) {
		EventManager manager = managers.get(eventName);
		if ( manager != null ) {
			return manager.Call(event);
		}
		return false;
	}
	
	//Note: this must be done after all listener types are added. Also, debug text is going to be removed after testing.
	public static boolean addListener(Object listener) {
		if ( listener.getClass().isPrimitive() ) {
			return false;
		}
		
		Method[] methods = listener.getClass().getMethods();
		
		boolean oneSuccess = false;
		
		for ( int i = 0; i < methods.length; i++ ) {
			Class<?>[] types = methods[i].getParameterTypes();
			if ( types.length > 0 ) {
				String matchingEvent = classRegistry.get(types[0].toString());
				if ( matchingEvent != null && !matchingEvent.isEmpty() ) {
					EventManager manager = managers.get(matchingEvent);
					if ( manager != null ) {
						manager.addCaller(new EventCaller(methods[i], listener));
						System.out.println("Added caller for method " + methods[i].getName());
						oneSuccess = true;
					}
				}
			}
		}
		
		return oneSuccess;
	}
}

package com.evanreidland.e.event;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Event {
	//Normal object.
	private boolean bCanceled;
	private long time;
	private Object object;
	public Object getObject() {
		return object;
	}
	
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
	
	public Event(Object object) {
		bCanceled = false;
		time = System.currentTimeMillis();
		this.object = object;
	}
	
	//Static use.
	
	private static HashMap<String, EventManager> managers = new HashMap<String, EventManager>();
	private static HashMap<Object, EventListenerInfo> listenerMap = new HashMap<Object, EventListenerInfo>();
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
		System.out.println(eventClass.toString());
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
	
	public static EventListenerInfo getListenerInfo(Object listener) {
		return listenerMap.get(listener);
	}
	
	public static EventListenerInfo getListenerInfo(Object listener, boolean make) {
		EventListenerInfo info = listenerMap.get(listener);
		if ( info == null && make ) {
			info = new EventListenerInfo(listener, null);
		}
		return info;
	}
	
	public static void removeListener(Object listener) {
		EventListenerInfo info = getListenerInfo(listener); 
		if ( info != null ) {
			for ( int i = 0; i < info.linkedEvents.size(); i++ ) {
				EventManager manager = managers.get(info.linkedEvents.get(i));
				if ( manager != null ) {
					manager.removeListener(info.getListener());
				}
			}
			info.linkedEvents.clear();
			listenerMap.remove(listener);
		}
	}
	public static void removeListener(Object listener, String eventName) {
		EventListenerInfo info = getListenerInfo(listener);
		if ( info != null ) {
			EventManager manager = managers.get(eventName);
			if ( manager != null ) {
				
				manager.removeListener(listener);
			}
			info.linkedEvents.remove(eventName);
		}
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
			if ( types.length == 1 ) {
				String matchingEvent = classRegistry.get(types[0].toString());
				if ( matchingEvent != null && !matchingEvent.isEmpty() ) {
					EventManager manager = managers.get(matchingEvent);
					if ( manager != null ) {
						EventListenerInfo info = getListenerInfo(listener, true);
						info.add(matchingEvent);
						manager.addCaller(new EventCaller(methods[i], listener, info));
						listenerMap.put(listener, info);
						
						oneSuccess = true;
					}
				}
			}
		}
		
		return oneSuccess;
	}
	
	public static boolean addPersonalListener(Object listener, Object toListenFor) {
		if ( addListener(listener) ) {
			getListenerInfo(listener).requiredObject = toListenFor;
			return true;
		}
		return false;
	}
	
	public static boolean addPersonalListener(Object listener) {
		return addPersonalListener(listener, listener);
	}
}

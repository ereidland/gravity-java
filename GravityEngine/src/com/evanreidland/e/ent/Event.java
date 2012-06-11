package com.evanreidland.e.ent;

import java.lang.reflect.Method;

public class Event {
	private String name;
	private Method method;
	private Object linked;
	public String getName() {
		return name;
	}
	public void Call(Object args) {
		try {
			method.invoke(linked, args);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	public Event(String name, Object linked, Method method) {
		this.name = name;
		this.method = method;
		this.linked = linked;
	}
}

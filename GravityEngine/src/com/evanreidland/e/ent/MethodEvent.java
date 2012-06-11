package com.evanreidland.e.ent;

import java.lang.reflect.Method;

public class MethodEvent extends EventFunction {
	private Method method;
	public Object Execute(Object activeObject, Object args) {
		try {
			return method.invoke(activeObject, args);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}
	
	public MethodEvent(Class<?> eventClass, String methodName) {
		try {
			method = eventClass.getMethod(methodName, eventClass);
		} catch ( Exception e ) {
			e.printStackTrace();
			method = null;
		}
	}
}

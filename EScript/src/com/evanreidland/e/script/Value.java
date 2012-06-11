package com.evanreidland.e.script;

public class Value {
	public static enum Type {
		String,
		Int,
		Float,
		Null // Never supposed to happen.
	}
	
	private Object object;
	private Type type;
	
	public Type getType() {
		return type;
	}
	
	public Object getObject() {
		return object;
	}
	
	public int toInt(int def) {
		try {
			switch(type) {
				case String:
					return Integer.valueOf((String)object).intValue();
				case Int:
					return ((Integer)object).intValue();
				case Float:
					return ((Float)object).intValue();
				case Null:
					return def;
			}
		} catch ( Exception e ) {
			return def;
		}
		return def;
	}
	
	public int toInt() {
		return toInt(0);
	}
	
	public float toFloat(float def) {
		try {
			switch(type) {
				case String:
					return Float.valueOf((String)object).floatValue();
				case Int:
					return ((Integer)object).floatValue();
				case Float:
					return ((Float)object).floatValue();
				case Null:
					return def;
			}
		} catch ( Exception e ) {
			return def;
		}
		return def;
	}
	
	public float toFloat() {
		return toFloat(0);
	}
	
	public String toString() {
		try {
			switch(type) {
				case String:
					return (String)object;
				case Int:
					return ((Integer)object).toString();
				case Float:
					return ((Float)object).toString();
				case Null:
					return "";
			}
		} catch ( Exception e ) {
			e.printStackTrace(); //TODO Remove this line after thorough testing.
			return "";
		}
		return "";
	}
	
	public Value setString(String value) {
		type = Type.String;
		object = value;
		return this;
	}
	
	public Value setInt(int value) {
		type = Type.Int;
		object = (Integer)value;
		return this;
	}
	
	public Value setFloat(float value) {
		type = Type.Float;
		object = (Float)value;
		return this;
	}
	
	public Value set(Value other) {
		type = other.type;
		object = other.object;
		return this;
	}

	public Value(String value) {
		setString(value);
	}
	public Value(int value) {
		setInt(value);
	}
	public Value(float value) {
		setFloat(value);
	}
	
	public Value(Value other) {
		set(other);
	}
	public Value() {
		this("");
	}
}

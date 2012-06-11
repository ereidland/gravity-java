package com.evanreidland.e;

public class Setting {
	public String value;
	private String name;
	public String getName() {
		return name;
	}
	public float asFloat(float def) {
		try {
			return Float.valueOf(value);
		} catch ( Exception e ) {
			return def;
		}
	}
	public int asInt(int def) {
		try {
			return Integer.valueOf(value);
		} catch ( Exception e ) {
			return def;
		}
	}
	
	public boolean asBool(boolean def) {
		try {
			return Boolean.valueOf(value);
		} catch ( Exception e ) {
			return def;
		}
	}
	
	public float asFloat() {
		return asFloat(0);
	}
	
	public int asInt() {
		return asInt(0);
	}
	
	public boolean asBool() {
		return asBool(false);
	}
	
	public void setInt(int num) {
		value = Integer.toString(num);
	}
	
	public void setFloat(float num) {
		value = Float.toString(num);
	}
	
	public void setBool(boolean state) {
		value = Boolean.toString(state);
	}
	
	public Setting(String name, String value) {
		this.name = name;
		this.value = value;
	}
}

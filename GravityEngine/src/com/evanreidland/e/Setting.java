package com.evanreidland.e;

public class Setting {
	public String value;
	private String name;
	public String getName() {
		return name;
	}
	public double asDouble(double def) {
		try {
			return Double.valueOf(value);
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
	
	public double asDouble() {
		return asDouble(0);
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
	
	public void setDouble(double num) {
		value = Double.toString(num);
	}
	
	public void setBool(boolean state) {
		value = Boolean.toString(state);
	}
	
	public Setting(String name, String value) {
		this.name = name;
		this.value = value;
	}
}

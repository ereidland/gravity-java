package com.evanreidland.e;

import java.util.HashMap;

public class Settings {
	private HashMap<String, Setting> settings;
	
	public Setting addSetting(Setting setting) {
		settings.put(setting.getName(), setting);
		return setting;
	}
	public Setting getSetting(String name) {
		Setting s = settings.get(name);
		if ( s == null ) {
			return new Setting("null", "0");
		}
		return s;
	}
	
	public Settings() {
		settings = new HashMap<String, Setting>();
	}
}

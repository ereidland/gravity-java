package com.evanreidland.e;

import java.util.HashMap;

public class Settings
{
	private HashMap<String, Setting> settings;

	public Setting set(Setting setting)
	{
		settings.put(setting.getName(), setting);
		return setting;
	}

	public Setting[] getAll()
	{
		Object[] list = settings.values().toArray();
		Setting[] slist = new Setting[list.length];
		for (int i = 0; i < slist.length; i++)
		{
			slist[i] = (Setting) list[i];
		}

		return slist;
	}

	public Setting get(String name)
	{
		Setting s = settings.get(name);
		if (s == null)
		{
			return new Setting("!null", "0");
		}
		return s;
	}

	public Setting set(String name, String value)
	{
		return set(new Setting(name, value));
	}

	public Settings()
	{
		settings = new HashMap<String, Setting>();
	}
}

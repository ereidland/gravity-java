package com.evanreidland.e.gui;

import java.util.Vector;

public class hud
{
	public static GUI gui = new GUI();

	public static GUIObject addObject(GUIObject object)
	{
		if (gui != null)
		{
			return gui.addObject(object);
		}
		return null;
	}

	public static GUIObject getObject(String name)
	{
		if (gui != null)
		{
			return gui.getObject(name);
		}
		return null;
	}

	public static GUIObject removeObject(GUIObject object)
	{
		if (gui != null)
		{
			return gui.removeObject(object);
		}
		return null;
	}

	public static GUIObject removeObject(String name)
	{
		if (gui != null)
		{
			return gui.removeObject(name);
		}
		return null;
	}

	public static Vector<GUIObject> getObjectChildren(String parentName)
	{
		if (gui != null)
		{
			return gui.getObjectChildren(parentName);
		}
		return new Vector<GUIObject>();
	}

	public static void set(String objectName, String name, String newValue)
	{
		if (gui != null)
		{
			gui.set(objectName, name, newValue);
		}
	}

	public static void setForChildren(String objectName, String name,
			String newValue)
	{
		if (gui != null)
		{
			gui.setForChildren(objectName, name, newValue);
		}
	}

	public static void Layout()
	{
		if (gui != null)
		{
			gui.Layout();
		}
	}
}

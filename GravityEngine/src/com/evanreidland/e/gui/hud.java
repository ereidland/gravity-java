package com.evanreidland.e.gui;

public class hud
{
	public static GUI gui = new GUI("hud");
	
	public static void addObject(GUI object)
	{
		gui.add(object);
	}
	
	public static void removeObject(GUI object)
	{
		gui.remove(object);
	}
}

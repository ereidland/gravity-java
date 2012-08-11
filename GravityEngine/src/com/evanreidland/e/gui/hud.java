package com.evanreidland.e.gui;

public class hud
{
	public static GUI gui = new GUI("hud");
	
	public static void add(GUI object)
	{
		gui.add(object);
	}
	
	public static void remove(GUI object)
	{
		gui.remove(object);
	}
}

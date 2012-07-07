package com.evanreidland.e.client.control;

import java.net.URL;

import com.evanreidland.e.client.EApplet;

public class input
{
	public static EApplet app = null;
	public static URL baseURL = null;
	public static int wheel = 0;

	public static boolean bTyping = false;

	public static final String validChars = "abcdefghijklmnopqrstuvwxyz1234567890-=!@#$%^&*()_+[];',./{}:\"<>?\\| ";

	public static boolean isValidTyped(char c)
	{
		return validChars.indexOf(c) != -1;
	}

	public static int getMouseWheel()
	{
		return wheel;
	}

	public static String getTyped()
	{
		return app.getTyped();
	}

	public static boolean isKeyDown(int key)
	{
		return app.isKeyDown(key);
	}

	public static boolean isKeyUp(int key)
	{
		return app.isKeyUp(key);
	}

	public static boolean getKeyState(int key)
	{
		return app.getKeyState(key);
	}
}

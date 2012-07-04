package com.evanreidland.mecharena.launcher;


import java.net.URL;


public class input {
	public static E2DApplet app = null;
	public static URL baseURL = null;
	public static int wheel = 0;
	
	public static int getMouseWheel() {
		return wheel;
	}
	
	public static String getTyped() { 
		return app.getTyped();
	}
	
	public static boolean isKeyDown(int key) {
		return app.isKeyDown(key);
	}
	public static boolean isKeyUp(int key) {
		return app.isKeyUp(key);
	}
	public static boolean getKeyState(int key) {
		return app.getKeyState(key);
	}
}

package com.evanreidland.e.client.control;

public class EventKey {
	public int state;
	public char key;
	public static final int KEY_NONE = 0;
	public static final int KEY_DOWN = 1;
	public static final int KEY_UP = 2;
	public static final int KEY_PRESSED = 4;
	public EventKey(int state, char key) {
		this.state = state;
		this.key = key;
	}
}

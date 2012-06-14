package com.evanreidland.e;

public abstract class Game {
	public static Vector3 mousePos = Vector3.Zero();
	public static float getDelta() {
		return engine.getDelta();
	}
	public static long getTime() {
		return System.currentTimeMillis();
	}
	public abstract void onUpdate();
	public abstract void onRender();
	public abstract void onRenderHUD();
	public abstract void onInit();
}

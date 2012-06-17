package com.evanreidland.e.graphics;

import com.evanreidland.e.Vector3;

public abstract class SceneObject {
	public Vector3 pos, angle;
	public double roughLength;
	private String className;
	public String getClassName() {
		return className;
	}
	public double roughDistance(Vector3 point) {
		return point.minus(pos).getRoughLength();
	}
	public final double calCamDistance() {
		return (roughLength = roughDistance(graphics.camera.pos));
	}
	public abstract void Render();
	
	public SceneObject(String className) {
		this.className = className;
		pos = Vector3.Zero();
		angle = Vector3.Zero();
		roughLength = 0;
	}
}

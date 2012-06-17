package com.evanreidland.e.graphics;

import com.evanreidland.e.Vector3;

public class Camera {
	public double fov, perspective, width, height, nearDist, farDist, orthoScale;
	public boolean is3D, ortho; // Use Orthographic projection? Default false. 
	public Vector3 pos, angle;
	
	public Vector3 topLeft() {
		return new Vector3(-width*0.5f, -height*0.5f, 0);
	}
	public Vector3 topRight() {
		return new Vector3(width*0.5f, -height*0.5f, 0);
	}
	public Vector3 bottomLeft() {
		return new Vector3(-width*0.5f, -height*0.5f, 0);
	}
	public Vector3 bottomRight() {
		return new Vector3(width*0.5f, height*0.5f, 0);
	}
	
	public Vector3 toScreen(Vector3 v) {
		return v.multipliedBy(getForward());
	}
	
	public Vector3 getForward() {
		return angle.getForward();
	}
	
	public Vector3 getForwardXY() {
		return angle.getForwardXY();
	}
	
	public Vector3 getRight() {
		return angle.getRight();
	}
	
	public Vector3 getUp() {
		return angle.getUp();
	}
	
	public void onRender() {
		
	}
	
	public void applyMouse(double changeX, double changeY, double scalar, boolean restrict) {
		angle.z -= changeX * scalar;
		angle.x += changeY * scalar;
		
		while ( angle.z > Math.PI * 2 ) {
			angle.z -= Math.PI * 2;
		}
		while ( angle.z < 0 ) {
			angle.z += Math.PI * 2;
		}
		
		if ( restrict ) {
			if ( angle.x > 0 ) angle.x = 0;
			if ( angle.x < -Math.PI ) angle.x = -(double)Math.PI;
		} else {
			while ( angle.x > Math.PI * 2 ) {
				angle.x -= Math.PI * 2;
			}
			while ( angle.x < 0 ) {
				angle.x += Math.PI * 2;
			}
		}
	}
	
	public void applyMouse(double changeX, double changeY, double scalar) {
		applyMouse(changeX, changeY, scalar, true);
	}
	
	public Camera() {
		nearDist = 1;
		farDist = 1000;
		fov = 45;
		perspective = 1;
		orthoScale = 1;
		width = 1;
		height = 1;
		is3D = true;
		pos = new Vector3();
		angle = new Vector3();
		ortho = false;
	}
}

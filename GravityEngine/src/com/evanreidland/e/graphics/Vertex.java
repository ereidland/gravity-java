package com.evanreidland.e.graphics;

import com.evanreidland.e.Vector3;

public class Vertex {
	public Vector3 pos, normal;
	public double tx, ty, r, g, b, a;
	
	public Vertex() {
		pos = Vector3.Zero();
		normal = Vector3.Zero();
		tx = ty = 0;
		r = g = b = a = 1;
	}
	
	public Vertex(Vertex other) {
		pos = other.pos.cloned();
		normal = other.normal.cloned();
		tx = other.tx;
		ty = other.ty;
		
		r = other.r;
		g = other.g;
		b = other.b;
		a = other.a;
	}
	
	public Vertex(Vector3 pos, Vector3 normal) {
		this();
		this.pos.setAs(pos);
		this.normal.setAs(normal);
	}
}

package com.evanreidland.e.graphics;

import com.evanreidland.e.Vector3;

public class Quad {
	public Vertex[] vert;
	
	public void pass() {
		graphics.passQuad(vert[0], vert[1], vert[2], vert[3]);
	}
	
	public void setColor(double r, double g, double b, double a) {
		for ( int i = 0; i < vert.length; i++ ) {
			vert[i].r = r;
			vert[i].g = g;
			vert[i].b = b;
			vert[i].a = a;
		}
	}
	
	public void applyToProjection(Vector3 origin, Vector3 right, Vector3 up) {
		for ( int i = 0; i < vert.length; i++ ) {
			vert[i].pos.setAs(origin.plus(right.multipliedBy(vert[i].pos.x).plus(up.multipliedBy(vert[i].pos.y))));
		}
	}
	public void Shift(Vector3 howMuch) {
		for ( int i = 0; i < vert.length; i++ ) {
			vert[i].pos.add(howMuch);
		}
	}
	
	public Quad() {
		vert = new Vertex[4];
		for ( int i = 0; i < vert.length; i++ ) {
			vert[i] = new Vertex();
		}
		
		vert[0].tx = 0; vert[0].ty = 0;
		vert[1].tx = 1; vert[1].ty = 0;
		vert[2].tx = 1; vert[2].ty = 1;
		vert[3].tx = 0; vert[3].ty = 1;
	}
}

package com.evanreidland.e.graphics;

import java.util.Vector;

import com.evanreidland.e.Vector3;

public class TriList extends Vector<Tri> {
	private static final long serialVersionUID = -1002528219987023746L;

	public void addTri(Vertex a, Vertex b, Vertex c) {
		add(new Tri(a, b, c));
	}
	public void addTri(Tri tri) {
		addTri(new Vertex(tri.vert[0]), new Vertex(tri.vert[1]), new Vertex(tri.vert[2]));
	}
	public void addQuad(Vertex a, Vertex b, Vertex c, Vertex d) {
		addTri(a, b, c);
		addTri(c, d, a);
	}
	public void addQuad(Quad quad) {
		addQuad(quad.vert[0], quad.vert[1], quad.vert[2], quad.vert[3]);
	}
	
	public void Rotate(Vector3 origin, Vector3 howMuch) {
		for ( int i = 0; i < size(); i++ ) {
			Tri tri = get(i);
			for ( int j = 0; j < tri.vert.length; j++ ) {
				tri.vert[j].pos.Rotate(howMuch, origin);
			}
		}
	}
	
	
	public void Shift(Vector3 howMuch) {
		for ( int i = 0; i < size(); i++ ) {
			Tri tri = get(i);
			for ( int j = 0; j < tri.vert.length; j++ ) {
				tri.vert[j].pos.add(howMuch);
			}
		}
	}
	
	public TriList() {
		super();
	}
}

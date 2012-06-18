package com.evanreidland.e.graphics;

import java.util.Vector;

import com.evanreidland.e.Vector3;

public class GraphicsData extends TriOperator {
	protected  int elementSize;
	protected int maxVerts;
	public Vector3 pos, size, angle;
	
	private class DataList {
		public int numVerts;
		public double[] data;
		public DataList() {
			data = new double[maxVerts*elementSize];
			numVerts = 0;
		}
	}
	public Vector<DataList> data;
	
	private void oneUp() {
		data.add(new DataList());
	}
	
	public void addQuad(Vertex a, Vertex b, Vertex c, Vertex d) {
		addTri(a, b, c);
		addTri(c, d, a);
	}
	
	public void addTri(Vertex a, Vertex b, Vertex c) {
		DataList d = null;
		if ( data.size() == 0 || data.lastElement().numVerts >= maxVerts - 3) {
			oneUp();
		}
		d = data.lastElement();
		
		graphics.appendList(d.data, graphics.toTriangle(a, b, c), d.numVerts*elementSize);
		d.numVerts += 3;
	}
	
	public void addTriList(TriList tris) {
		for ( int i = 0; i < tris.size(); i++ ) {
			Tri tri = tris.get(i);
			addTri(tri.vert[0], tri.vert[1], tri.vert[2]);
		}
	}
	
	public void passData() {
		graphics.putTranslation(pos, size, angle);
		for ( int i = 0; i < data.size(); i++ ) {
			DataList d = data.get(i);
			graphics.passList(d.data, d.numVerts);
		}
		graphics.endTranslation();
	}
	
	public void passToRenderList(RenderList rlist, boolean translated) {
		if ( translated ) {
			graphics.putTranslation(pos, size, angle);
		}
		for ( int i = 0; i < data.size(); i++ ) {
			DataList d = data.get(i);
			graphics.passListToRenderData(d.data, d.numVerts, rlist);
		}
		if ( translated) { 
			graphics.endTranslation();
		}
	}
	
	public void clear() {
		data.clear();
	}
	
	public GraphicsData() {
		pos = Vector3.Zero();
		angle = Vector3.Zero();
		size = new Vector3(1, 1, 1);
		
		data = new Vector<DataList>();
		
		maxVerts = 2048;
		elementSize = 16;
	}
	
	/**
	 * Does nothing. From TriOperator class.
	 */
	public void Begin() {};
	/**
	 * Does nothing. From TriOperator class.
	 */
	public void End() {};
}

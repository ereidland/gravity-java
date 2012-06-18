package com.evanreidland.e.graphics;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.phys.Rect3;

public class graphics {
	public static Vector3 forward = Vector3.Zero(), up = Vector3.Zero(), right = Vector3.Zero();
	public static Camera camera = new Camera();
	private static GraphicsManager graphicsManager = null;
	private static Class<? extends GraphicsData> graphicsDataClass = null;
	private static Class<? extends Light> lightClass = null;
	private static Class<? extends RenderList> renderListClass = null;	
	public static void setGraphicsManager(GraphicsManager m) {
		graphicsManager = m;
	}
	public static GraphicsManager getGraphicsManager() {
		return graphicsManager;
	}
	
	public static void setDataClass(Class<? extends GraphicsData> dataClass) {
		graphicsDataClass = dataClass;
	}
	
	public static void setLightClass(Class<? extends Light> lightObjectClass) {
		lightClass = lightObjectClass;
	}
	
	public static void setRenderListClass(Class<? extends RenderList> renderClass) {
		renderListClass = renderClass;
	}
	
	public static void drawSkybox(Resource res, double radius) {
		Rect3 r = new Rect3(new Vector3(-radius, -radius, -radius), new Vector3(radius, radius, radius));
		
		r.Shift(camera.pos);
		
		Quad q = new Quad();
		graphics.setTexture(res);
		
		//Top
		q.vert[0].pos = r.topRight(); q.vert[1].pos = r.topLeft();
		q.vert[3].pos = r.topBRight(); q.vert[2].pos = r.topBLeft();
		q.pass();
		
		//Bottom
		q.vert[0].pos = r.bottomULeft(); q.vert[1].pos = r.bottomURight();
		q.vert[3].pos = r.bottomLeft(); q.vert[2].pos = r.bottomRight();
		q.pass();
		
		//Left
		q.vert[0].pos = r.topBLeft(); q.vert[1].pos = r.topLeft();
		q.vert[3].pos = r.bottomLeft(); q.vert[2].pos = r.bottomULeft();
		q.pass();
		
		//Right
		q.vert[0].pos = r.topRight(); q.vert[1].pos = r.topBRight();
		q.vert[3].pos = r.bottomURight(); q.vert[2].pos = r.bottomRight();
		q.pass();
		
		//Front
		q.vert[0].pos = r.topLeft(); q.vert[1].pos = r.topRight();
		q.vert[3].pos = r.bottomULeft(); q.vert[2].pos = r.bottomURight();
		q.pass();
		
		//Back
		q.vert[0].pos = r.topBRight(); q.vert[1].pos = r.topBLeft();
		q.vert[3].pos = r.bottomRight(); q.vert[2].pos = r.bottomLeft();
		q.pass();
	}
	
	public static GraphicsData newData() {
		try {
			return (GraphicsData)graphicsDataClass.newInstance();
		} catch ( Exception e ) {
			return null;
		}
	}
	
	public static Light newLight(int id) {
		try {
			Light l = (Light)lightClass.newInstance();
			l.id = id;
			return l;
		} catch ( Exception e ) {
			return null;
		}
	}
	
	public static RenderList newRenderList() {
		try {
			return (RenderList)renderListClass.newInstance();
		} catch ( Exception e ) {
			return null;
		}
	}
	
	public static void beginFrame() {
		if ( graphicsManager != null ) {
			graphicsManager.beginFrame();
		}
	}
	
	public static void endFrame() {
		if ( graphicsManager != null ) {
			graphicsManager.endFrame();
		}
	}
	
	public static Resource loadTexture(String path) {
		return engine.loadTexture(path);
	}
	
	public static void unbindTexture() {
		setTexture(null);
	}
	
	public static void setTexture(Resource tex) {
		if ( graphicsManager != null ) {
			graphicsManager.setTexture(tex);
		}
	}
	
	public static void drawFont(Vector3 pos, String txt, double size) {
		if ( graphicsManager != null ) {
			graphicsManager.drawFont(pos, txt, size);
		}
	}
	public static void passTriangle(Vertex a, Vertex b, Vertex c) {
		if ( graphicsManager != null ) {
			graphicsManager.passTriangle(a, b, c);
		}
	}
	public static void passTriangleList(TriList list ) {
		for ( int i = 0; i < list.size(); i++) {
			Tri t = list.get(i);
			passTriangle(t.vert[0], t.vert[1], t.vert[2]);
		}
	}
	public static void passTriangle(double[] data) {
		if ( graphicsManager != null ) {
			graphicsManager.passTriangle(data);
		}
	}
	public static void passQuad(Vertex a, Vertex b, Vertex c, Vertex d) {
		if ( graphicsManager != null ) {
			graphicsManager.passQuad(a, b, c, d);
		}
	}
	public static void passList(double[] list, int numVerts) {
		if ( graphicsManager != null ) {
			graphicsManager.passList(list, numVerts);
		}
	}
	
	public static void passListToRenderData(double[] list, int numVerts, RenderList rlist) {
		if ( graphicsManager != null ) {
			graphicsManager.passListToRenderData(list, numVerts, rlist);
		}
	}
	
	public static void appendList(double[] data, double[] toAppend, int insertIndex) {
		for ( int i = 0; i < toAppend.length; i++) {
			data[insertIndex + i] = toAppend[i];
		}
	}
	
	public static double[] toTriangle(Vertex a, Vertex b, Vertex c) {
		if ( graphicsManager != null ) {
			return graphicsManager.toTriangle(a, b, c);
		}
		
		return null;
	}
	
	public static void setCamera2D(Camera cam) {
		if ( graphicsManager != null ) {
			graphicsManager.setCamera2D(cam);
		}
	}
	
	public static void setCamera(Camera cam) {
		if ( graphicsManager != null ) {
			graphicsManager.setCamera(cam);
		}
	}
	
	public static void putTranslation(Vector3 offset, Vector3 scale, Vector3 angle) {
		if ( graphicsManager != null ) {
			graphicsManager.putTranslation(offset, scale, angle);
		}
	}
	
	public static void endTranslation() {
		if ( graphicsManager != null ) {
			graphicsManager.endTranslation();
	}
	}
	
	public static void drawLine(Vector3 pos1, Vector3 pos2, double width, double r, double g, double b, double a) {
		if ( graphicsManager != null ) {
			graphicsManager.drawLine(pos1, pos2, width, r, g, b, a);
		}
	}
}

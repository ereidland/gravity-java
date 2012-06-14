package com.evanreidland.e.graphics;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;

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
	
	public static void drawFont(Vector3 pos, String txt, float size) {
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
	public static void passTriangle(float[] data) {
		if ( graphicsManager != null ) {
			graphicsManager.passTriangle(data);
		}
	}
	public static void passQuad(Vertex a, Vertex b, Vertex c, Vertex d) {
		if ( graphicsManager != null ) {
			graphicsManager.passQuad(a, b, c, d);
		}
	}
	public static void passList(float[] list, int numVerts) {
		if ( graphicsManager != null ) {
			graphicsManager.passList(list, numVerts);
		}
	}
	
	public static void passListToRenderData(float[] list, int numVerts, RenderList rlist) {
		if ( graphicsManager != null ) {
			graphicsManager.passListToRenderData(list, numVerts, rlist);
		}
	}
	
	public static void appendList(float[] data, float[] toAppend, int insertIndex) {
		for ( int i = 0; i < toAppend.length; i++) {
			data[insertIndex + i] = toAppend[i];
		}
	}
	
	public static float[] toTriangle(Vertex a, Vertex b, Vertex c) {
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
	
	public static void drawLine(Vector3 pos1, Vector3 pos2, float width, float r, float g, float b, float a) {
		if ( graphicsManager != null ) {
			graphicsManager.drawLine(pos1, pos2, width, r, g, b, a);
		}
	}
}

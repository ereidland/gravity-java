package com.evanreidland.e.graphics;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;

public abstract class GraphicsManager {
	public abstract void beginFrame();
	
	public abstract void drawFont(Vector3 pos, String txt, float size);
	
	public abstract void passTriangle(Vertex a, Vertex b, Vertex c);
	
	public abstract void passQuad(Vertex a, Vertex b, Vertex c, Vertex d);
	
	public abstract void setCamera2D(Camera cam);
	
	public abstract void setCamera(Camera cam);
	
	public abstract void setTexture(Resource tex);
	
	public abstract void passTriangle(float[] data);
	
	public abstract void passList(float[] list, int numVerts);
	public abstract void passListToRenderData(float[] list, int numVerts, RenderList rlist);
	
	public abstract float[] toTriangle(Vertex a, Vertex b, Vertex c);
	
	public abstract void drawLine(Vector3 pos1, Vector3 pos2, float width, float r, float g, float b, float a);
	
	public abstract void putTranslation(Vector3 offset, Vector3 scale, Vector3 angle);
	public abstract void endTranslation();
	
	public abstract void endFrame();
}

package com.evanreidland.e.graphics;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;

public abstract class GraphicsManager {
	public abstract void beginFrame();
	
	public abstract void drawFont(Vector3 pos, String txt, double size);
	
	public abstract void passTriangle(Vertex a, Vertex b, Vertex c);
	
	public abstract void passQuad(Vertex a, Vertex b, Vertex c, Vertex d);
	
	public abstract void setCamera2D(Camera cam);
	
	public abstract void setCamera(Camera cam);
	
	public abstract void setTexture(Resource tex);
	
	public abstract void passTriangle(double[] data);
	
	public abstract void passList(double[] list, int numVerts);
	public abstract void passListToRenderData(double[] list, int numVerts, RenderList rlist);
	
	public abstract double[] toTriangle(Vertex a, Vertex b, Vertex c);
	
	public abstract void drawLine(Vector3 pos1, Vector3 pos2, double width, double r, double g, double b, double a);
	
	public abstract void putTranslation(Vector3 offset, Vector3 scale, Vector3 angle);
	
	public abstract void endTranslation();
	
	public abstract void setClipping(double x, double y, double width, double height);
	public abstract void endClipping();
	
	public abstract void endFrame();
}

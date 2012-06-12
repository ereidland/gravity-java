package com.evanreidland.e.client.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.graphics.Camera;
import com.evanreidland.e.graphics.GraphicsManager;
import com.evanreidland.e.graphics.RenderList;
import com.evanreidland.e.graphics.Vertex;
import com.evanreidland.e.graphics.graphics;

public class GLGraphicsManager extends GraphicsManager {
	
	public void beginFrame() {
		
	}
	
	public void drawFont(Vector3 pos, String txt, float size) {
		//TODO code.
	}
	
	public void passTriangle(Vertex a, Vertex b, Vertex c) {
		vbo.passTriangle(vbo.toTriangle(a, b, c));
	}
	
	public void passQuad(Vertex a, Vertex b, Vertex c, Vertex d) {
		vbo.passQuad(vbo.toQuad(a, b, c, d));
	}
	
	public void setCamera(Camera cam) {
		setView(cam, false);
	}
	
	public void setCamera2D(Camera cam) {
		Vector3 viewPos = cam.pos.cloned();
		cam.pos.setAs(0, 0, 0);
		setView(cam, true);
		cam.pos.setAs(viewPos);
	}
	
	public static void setView(Camera view, boolean ortho) {
		graphics.camera = view;
		if ( view.is3D) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			if ( ortho ) {
				float r = view.width/view.height;
				GL11.glOrtho(-0.5f*view.orthoScale*r, 0.5f*view.orthoScale*r, -0.5f*view.orthoScale, 0.5f*view.orthoScale, view.nearDist, view.farDist);
			} else {
				GLU.gluPerspective(view.fov, view.width/view.height, view.nearDist, view.farDist);
				//GLU.gluOrtho2D(-(view.width/2), (view.width/2), (view.height/2), -(view.height/2));
			}
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			
			//GL11.glScalef(1, 1, -1);
			
			GL11.glRotatef((float)Math.toDegrees(-view.angle.x), 1, 0, 0);
			GL11.glRotatef((float)Math.toDegrees(-view.angle.y), 0, 1, 0);
			GL11.glRotatef((float)Math.toDegrees(-view.angle.z), 0, 0, 1);
			
			//System.out.println("(" + Math.toDegrees(view.angle.x) + ", " + Math.toDegrees(view.angle.y) + ", " + Math.toDegrees(view.angle.z) + ")" );
			
			GL11.glTranslatef(-view.pos.x, -view.pos.y, -view.pos.z);
		} else {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluOrtho2D(-(view.width/2), (view.width/2), -(view.height/2), (view.height/2));
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			
			//GL11.glRotatef((float)Math.toDegrees(-view.angle.x), 1, 0, 0);
			//GL11.glRotatef((float)Math.toDegrees(-view.angle.y), 0, 1, 0);
			//GL11.glRotatef((float)Math.toDegrees(-view.angle.z), 0, 0, 1);
			//GL11.glTranslatef(-view.pos.x, -view.pos.y, -view.pos.z);
			GL11.glLoadIdentity();

			GL11.glDisable(GL11.GL_CULL_FACE);
		}
		//GL11.glBlendFunc(GL11.GL_BLEND, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void setLWJGLPath() {
		String dataPath = System.getenv("appdata") + "/.spellcraft/bin";
		System.setProperty("java.library.path", dataPath);
		System.setProperty("org.lwjgl.librarypath", dataPath);
		System.setProperty("net.java.games.input.librarypath", dataPath);
	}
	
	public void setTexture(Resource tex) {
		Texture t = tex != null ? (tex.isValid() ? (Texture)tex.getObject() : null) : null;
		vbo.setTexture(t);
		if ( t != null ) t.bind();
	}
	
	public void passTriangle(float[] data) {
		vbo.passTriangle(data);
	}
	
	public void passList(float[] list, int numVerts) {
		vbo.setBuffer(list, numVerts);
	}
	public void passListToRenderData(float[] list, int numVerts, RenderList rlist) {
		vbo.passToRenderList(list, numVerts, rlist);
	}
	
	public float[] toTriangle(Vertex a, Vertex b, Vertex c) {
		return vbo.toTriangle(a, b, c);
	}
	
	public void drawLine(Vector3 pos1, Vector3 pos2, float width, float r, float g, float b, float a) {
		GL11.glLineWidth(width);
		
		GL11.glBegin(GL11.GL_LINES);
			GL11.glColor4f(r, g, b, a);
			
			GL11.glVertex2f(pos1.x, pos1.y);
			
			GL11.glVertex2f(pos2.x, pos2.y);
		GL11.glEnd();
	}
	
	public void putTranslation(Vector3 offset, Vector3 scale, Vector3 angle) {
		GL11.glPushMatrix();
		GL11.glScalef(scale.x, scale.y, scale.z);
		
		GL11.glTranslatef(offset.x, offset.y, offset.z);
		
		GL11.glRotatef((float)Math.toDegrees(angle.z), 0, 0, 1);
		GL11.glRotatef((float)Math.toDegrees(angle.y), 0, 1, 0);
		GL11.glRotatef((float)Math.toDegrees(angle.x), 1, 0, 0);
		
	}

	public void endTranslation() {
		GL11.glPopMatrix();
	}

	public void endFrame() {
		vbo.drawBuffer();
	}
}

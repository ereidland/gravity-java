package com.evanreidland.e.client.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.graphics.Camera;
import com.evanreidland.e.graphics.GraphicsManager;
import com.evanreidland.e.graphics.RenderList;
import com.evanreidland.e.graphics.Vertex;
import com.evanreidland.e.graphics.graphics;

public class GLGraphicsManager extends GraphicsManager
{
	
	public void beginFrame()
	{
		
	}
	
	private static FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16),
			modelMatrix = BufferUtils.createFloatBuffer(16);
	private static IntBuffer viewPort = BufferUtils.createIntBuffer(16);
	
	public void drawFont(Vector3 pos, String txt, double size)
	{
		// TODO remove.
	}
	
	public void passTriangle(Vertex a, Vertex b, Vertex c)
	{
		vbo.passTriangle(vbo.toTriangle(a, b, c));
	}
	
	public void passQuad(Vertex a, Vertex b, Vertex c, Vertex d)
	{
		vbo.passQuad(vbo.toQuad(a, b, c, d));
	}
	
	public void setCamera(Camera cam)
	{
		setView(cam, false);
	}
	
	public void setCamera2D(Camera cam)
	{
		Vector3 viewPos = cam.pos.cloned();
		cam.pos.setAs(0, 0, 0);
		setView(cam, true);
		cam.pos.setAs(viewPos);
	}
	
	public static void setView(Camera view, boolean ortho)
	{
		graphics.camera = view;
		if (view.is3D)
		{
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			if (ortho)
			{
				double r = view.width / view.height;
				GL11.glOrtho(-0.5f * view.orthoScale * r, 0.5f
						* view.orthoScale * r, -0.5f * view.orthoScale,
						0.5f * view.orthoScale, view.nearDist, view.farDist);
			}
			else
			{
				GLU.gluPerspective((float) view.fov,
						(float) (view.width / view.height),
						(float) view.nearDist, (float) view.farDist);
				// GLU.gluOrtho2D(-(view.width/2), (view.width/2),
				// (view.height/2), -(view.height/2));
			}
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			
			// GL11.glScalef(1, 1, -1);
			
			GL11.glRotatef((float) Math.toDegrees(-view.angle.x), 1, 0, 0);
			GL11.glRotatef((float) Math.toDegrees(-view.angle.y), 0, 1, 0);
			GL11.glRotatef((float) Math.toDegrees(-view.angle.z), 0, 0, 1);
			
			// System.out.println("(" + Math.toDegrees(view.angle.x) + ", " +
			// Math.toDegrees(view.angle.y) + ", " +
			// Math.toDegrees(view.angle.z) + ")" );
			
			GL11.glTranslatef(-(float) view.pos.x, -(float) view.pos.y,
					-(float) view.pos.z);
			
			GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
			GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projMatrix);
			viewPort.put(0, (int) (-view.width * 0.5));
			viewPort.put(1, (int) (-view.height * 0.5));
			viewPort.put(2, (int) (view.width));
			viewPort.put(3, (int) (view.height));
		}
		else
		{
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluOrtho2D(-(float) (view.width / 2), (float) (view.width / 2),
					-(float) (view.height / 2), (float) (view.height / 2));
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			
			// GL11.glRotatef((double)Math.toDegrees(-view.angle.x), 1, 0, 0);
			// GL11.glRotatef((double)Math.toDegrees(-view.angle.y), 0, 1, 0);
			// GL11.glRotatef((double)Math.toDegrees(-view.angle.z), 0, 0, 1);
			// GL11.glTranslatef(-view.pos.x, -view.pos.y, -view.pos.z);
			GL11.glLoadIdentity();
			
			GL11.glDisable(GL11.GL_CULL_FACE);
		}
		// GL11.glBlendFunc(GL11.GL_BLEND, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void setLWJGLPath()
	{
		String dataPath = engine.getPath() + "bin";
		System.setProperty("java.library.path", dataPath);
		System.setProperty("org.lwjgl.librarypath", dataPath);
		System.setProperty("net.java.games.input.librarypath", dataPath);
	}
	
	public void setTexture(Resource tex)
	{
		Texture t = tex != null ? (tex.isValid() ? (Texture) tex.getObject()
				: null) : null;
		vbo.setTexture(t);
		if (t != null)
			t.bind();
		else
			Texture.unbind();
	}
	
	public Vector3 toScreen(Vector3 point)
	{
		FloatBuffer pos = BufferUtils.createFloatBuffer(3);
		
		GLU.gluProject((float) point.x, (float) point.y, (float) point.z,
				modelMatrix, projMatrix, viewPort, pos);
		return new Vector3(pos.get(0), pos.get(1), pos.get(2));
	}
	
	public Vector3 unProject(Vector3 point)
	{
		FloatBuffer pos = BufferUtils.createFloatBuffer(3);
		
		GLU.gluUnProject((float) point.x, (float) point.y, (float) point.z,
				modelMatrix, projMatrix, viewPort, pos);
		return new Vector3(pos.get(0), pos.get(1), pos.get(2));
	}
	
	public Vector3 toWorld(Vector3 point)
	{
		return unProject(
				new Vector3(point.x, point.y, graphics.camera.nearDist))
				.minus(unProject(new Vector3(point.x, point.y,
						graphics.camera.farDist))).Normalize();
	}
	
	public void passTriangle(double[] data)
	{
		vbo.passTriangle(data);
	}
	
	public void passList(double[] list, int numVerts)
	{
		vbo.setBuffer(list, numVerts);
	}
	
	public void passListToRenderData(double[] list, int numVerts,
			RenderList rlist)
	{
		vbo.passToRenderList(list, numVerts, rlist);
	}
	
	public double[] toTriangle(Vertex a, Vertex b, Vertex c)
	{
		return vbo.toTriangle(a, b, c);
	}
	
	public void drawLine(Vector3 pos1, Vector3 pos2, double width, double r,
			double g, double b, double a)
	{
		GL11.glLineWidth((float) width);
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor4f((float) r, (float) g, (float) b, (float) a);
		
		GL11.glTexCoord2f(0, 0.5f);
		GL11.glVertex3d((float) pos1.x, (float) pos1.y, (float) pos1.z);
		
		GL11.glTexCoord2f(1, 0.5f);
		GL11.glVertex3d((float) pos2.x, (float) pos2.y, (float) pos2.z);
		GL11.glEnd();
	}
	
	public void putTranslation(Vector3 offset, Vector3 scale, Vector3 angle)
	{
		GL11.glPushMatrix();
		GL11.glScalef((float) scale.x, (float) scale.y, (float) scale.z);
		
		GL11.glTranslatef((float) offset.x, (float) offset.y, (float) offset.z);
		
		GL11.glRotatef((float) Math.toDegrees(angle.z), 0, 0, 1);
		GL11.glRotatef((float) Math.toDegrees(angle.y), 0, 1, 0);
		GL11.glRotatef((float) Math.toDegrees(angle.x), 1, 0, 0);
		
	}
	
	public void endTranslation()
	{
		GL11.glPopMatrix();
	}
	
	public void setClipping(double x, double y, double width, double height)
	{
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		int ix = graphics.toPixelX(x), iy = graphics.toPixelY(y), iwidth = graphics
				.toPixelX(width), iheight = graphics.toPixelY(height);
		GL11.glScissor(ix, iy, iwidth, iheight);
	}
	
	public void endClipping()
	{
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	public void endFrame()
	{
		vbo.drawBuffer();
	}
}

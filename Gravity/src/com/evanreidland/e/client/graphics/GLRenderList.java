package com.evanreidland.e.client.graphics;

import org.lwjgl.opengl.GL11;

import com.evanreidland.e.graphics.RenderList;
import com.evanreidland.e.graphics.Vertex;

public class GLRenderList extends RenderList {
	private int id = 0;
	public void Begin() {
		GL11.glNewList(id, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_TRIANGLES);
	}
	public void addTri(Vertex a, Vertex b, Vertex c) {
		GL11.glColor3f(a.r, a.g, a.b);
		GL11.glNormal3f(a.normal.x, a.normal.y, a.normal.z);
		GL11.glTexCoord2f(a.tx, a.ty);
		GL11.glVertex3f(a.pos.x, a.pos.y, a.pos.z);
		
		GL11.glColor3f(b.r, b.g, b.b);
		GL11.glNormal3f(b.normal.x, b.normal.y, b.normal.z);
		GL11.glTexCoord2f(b.tx, b.ty);
		GL11.glVertex3f(b.pos.x, b.pos.y, b.pos.z);
		
		GL11.glColor3f(c.r, c.g, c.b);
		GL11.glNormal3f(c.normal.x, c.normal.y, c.normal.z);
		GL11.glTexCoord2f(c.tx, c.ty);
		GL11.glVertex3f(c.pos.x, c.pos.y, c.pos.z);
	}
	public void End() {
		GL11.glEnd();
		GL11.glEndList();
	}
	public void Render() {
		GL11.glCallList(id);
	}
	public GLRenderList() {
		id = GL11.glGenLists(1);
	}
}

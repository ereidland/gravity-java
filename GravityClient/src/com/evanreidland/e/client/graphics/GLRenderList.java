package com.evanreidland.e.client.graphics;

import org.lwjgl.opengl.GL11;

import com.evanreidland.e.graphics.RenderList;
import com.evanreidland.e.graphics.Vertex;

public class GLRenderList extends RenderList
{
	private int id = 0;

	public void Begin()
	{
		GL11.glNewList(id, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_TRIANGLES);
	}

	public void addTri(Vertex a, Vertex b, Vertex c)
	{
		GL11.glColor3f((float) a.r, (float) a.g, (float) a.b);
		GL11.glNormal3f((float) a.normal.x, (float) a.normal.y,
				(float) a.normal.z);
		GL11.glTexCoord2f((float) a.tx, (float) a.ty);
		GL11.glVertex3f((float) a.pos.x, (float) a.pos.y, (float) a.pos.z);

		GL11.glColor3f((float) b.r, (float) b.g, (float) b.b);
		GL11.glNormal3f((float) b.normal.x, (float) b.normal.y,
				(float) b.normal.z);
		GL11.glTexCoord2f((float) b.tx, (float) b.ty);
		GL11.glVertex3f((float) b.pos.x, (float) b.pos.y, (float) b.pos.z);

		GL11.glColor3f((float) c.r, (float) c.g, (float) c.b);
		GL11.glNormal3f((float) c.normal.x, (float) c.normal.y,
				(float) c.normal.z);
		GL11.glTexCoord2f((float) c.tx, (float) c.ty);
		GL11.glVertex3f((float) c.pos.x, (float) c.pos.y, (float) c.pos.z);
	}

	public void End()
	{
		GL11.glEnd();
		GL11.glEndList();
	}

	public void Render()
	{
		GL11.glCallList(id);
	}

	public GLRenderList()
	{
		id = GL11.glGenLists(1);
	}
}

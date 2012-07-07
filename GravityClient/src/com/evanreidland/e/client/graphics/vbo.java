package com.evanreidland.e.client.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;

import com.evanreidland.e.graphics.RenderList;
import com.evanreidland.e.graphics.Vertex;

public class vbo
{
	public static final int STRIDE = 128;// (3 + 3 + 4 + 2) * 8; // 3 for
											// vertex, 3 for normal, 4 for color
											// and 2 for texture coordinates and
											// * 8 for bytes.
	public static final int MAX_VERTS = 2048;
	private static int current = 0;

	private static Texture tex = null;

	public static void Begin(int id)
	{
		current = id;
	}

	public static void setTexture(Texture newTex)
	{
		if (newTex == null || (tex == null && newTex != null)
				|| newTex.getID() != tex.getID())
		{
			drawBuffer();
		}
		tex = newTex;
	}

	private static DoubleBuffer curBuff = ByteBuffer
			.allocateDirect(MAX_VERTS * STRIDE).order(ByteOrder.nativeOrder())
			.asDoubleBuffer();
	private static IntBuffer curElemBuff = ByteBuffer
			.allocateDirect(MAX_VERTS * 4).order(ByteOrder.nativeOrder())
			.asIntBuffer();
	private static int vp = 0;

	public static int getVP()
	{
		return vp;
	}

	public static void setBuffer(int id)
	{
		current = id;
	}

	public static void passToRenderList(double[] data, int count,
			RenderList rlist)
	{
		for (int i = 0; i < count; i++)
		{
			Vertex[] tverts = new Vertex[3];

			int p = i * STRIDE;

			for (int j = 0; j < 3; j++)
			{
				tverts[j] = new Vertex();
				tverts[j].pos.x = data[++p];
				tverts[j].pos.y = data[++p];
				tverts[j].pos.z = data[++p];

				tverts[j].normal.x = data[++p];
				tverts[j].normal.y = data[++p];
				tverts[j].normal.z = data[++p];

				tverts[j].r = data[++p];
				tverts[j].g = data[++p];
				tverts[j].b = data[++p];
				tverts[j].a = data[++p];

				tverts[j].tx = data[++p];
				tverts[j].ty = data[++p];
			}

			rlist.addTri(tverts[0], tverts[1], tverts[2]);
		}
	}

	public static void setBuffer(double[] data, int count)
	{
		drawBuffer();
		curBuff.put(data);
		vp = count;
		drawBuffer();
	}

	public static void clearBuffer()
	{
		curBuff.clear();
		curElemBuff.clear();
		vp = 0;
	}

	public static void passTriangle(double[] tri)
	{
		if (vp >= MAX_VERTS - 3
				|| curElemBuff.position() >= curElemBuff.capacity() - 3)
		{
			drawBuffer();
		}

		curBuff.put(tri);
		for (int i = 0; i < 3; i++)
		{
			curElemBuff.put(vp + i);
		}
		vp += 3;
	}

	public static void passQuad(double[] quad)
	{
		if (vp >= MAX_VERTS - 4
				|| curElemBuff.position() >= curElemBuff.capacity() - 6)
		{
			drawBuffer();
		}

		curBuff.put(quad);
		curElemBuff.put(vp);
		curElemBuff.put(vp + 1);
		curElemBuff.put(vp + 2);
		curElemBuff.put(vp + 2);
		curElemBuff.put(vp + 3);
		curElemBuff.put(vp);
		vp += 4;
	}

	public static double[] toTriangle(Vertex a, Vertex b, Vertex c)
	{
		return new double[]
		{
				a.pos.x, a.pos.y, a.pos.z, a.normal.x, a.normal.y, a.normal.z,
				a.r, a.g, a.b, a.a, a.tx, a.ty, 0, 0, 0, 0,

				b.pos.x, b.pos.y, b.pos.z, b.normal.x, b.normal.y, b.normal.z,
				b.r, b.g, b.b, b.a, b.tx, b.ty, 0, 0, 0, 0,

				c.pos.x, c.pos.y, c.pos.z, c.normal.x, c.normal.y, c.normal.z,
				c.r, c.g, c.b, c.a, c.tx, c.ty, 0, 0, 0, 0,
		};
	}

	public static double[] toQuad(Vertex a, Vertex b, Vertex c, Vertex d)
	{
		return new double[]
		{
				a.pos.x, a.pos.y, a.pos.z, a.normal.x, a.normal.y, a.normal.z,
				a.r, a.g, a.b, a.a, a.tx, a.ty, 0, 0, 0, 0,

				b.pos.x, b.pos.y, b.pos.z, b.normal.x, b.normal.y, b.normal.z,
				b.r, b.g, b.b, b.a, b.tx, b.ty, 0, 0, 0, 0,

				c.pos.x, c.pos.y, c.pos.z, c.normal.x, c.normal.y, c.normal.z,
				c.r, c.g, c.b, c.a, c.tx, c.ty, 0, 0, 0, 0,

				d.pos.x, d.pos.y, d.pos.z, d.normal.x, d.normal.y, d.normal.z,
				d.r, d.g, d.b, d.a, d.tx, d.ty, 0, 0, 0, 0,
		};
	}

	public static int newID()
	{
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object)
		{
			IntBuffer buffer = BufferUtils.createIntBuffer(1);
			ARBVertexBufferObject.glGenBuffersARB(buffer);
			return buffer.get(0);
		}
		return 0;
	}

	public static void setup()
	{
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_INDEX_ARRAY);

		ARBVertexBufferObject.glBindBufferARB(
				ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, current);
		// vertices
		int offset = 0; // 0 as its the first in the chunk, i.e. no offset. * 8
						// to convert to bytes.
		GL11.glVertexPointer(3, GL11.GL_DOUBLE, STRIDE, offset);

		// normals
		offset = 3 * 8; // 3 components is the initial offset from 0, then
						// convert to bytes
		GL11.glNormalPointer(GL11.GL_DOUBLE, STRIDE, offset);

		// colors
		offset = (3 + 3) * 8; // (6*8) is the number of byte to skip to get to
								// the color chunk
		GL11.glColorPointer(4, GL11.GL_DOUBLE, STRIDE, offset);

		// texture coordinates
		offset = (3 + 3 + 4) * 8;
		GL11.glTexCoordPointer(2, GL11.GL_DOUBLE, STRIDE, offset);
	}

	public static void init()
	{

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		current = newID();

		setup();
	}

	private static void bufferData(int id)
	{
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object)
		{
			if (curBuff.position() != 0)
			{
				curBuff.flip();
				curElemBuff.flip();

				ARBVertexBufferObject.glBindBufferARB(
						ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);

				ARBVertexBufferObject.glBufferDataARB(
						ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, curBuff,
						ARBVertexBufferObject.GL_STREAM_DRAW_ARB);
			}
		}
	}

	/*
	 * private static void bufferElementData(int id, IntBuffer buffer) { if
	 * (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
	 * ARBVertexBufferObject
	 * .glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
	 * ARBVertexBufferObject
	 * .glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB,
	 * buffer, ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB); } }
	 */

	public static void drawBuffer()
	{
		if (current != 0 && vp >= 3)
		{
			bufferData(current);
			if (tex == null)
			{
				Texture.unbind();
			}
			else
			{
				tex.bind();
			}
			GL12.glDrawRangeElements(GL11.GL_TRIANGLES, 0, vp - 1, curElemBuff);
			clearBuffer();
			GL11.glFlush();
		}
	}
}

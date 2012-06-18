package com.evanreidland.e.client.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.graphics.RenderList;
import com.evanreidland.e.graphics.Vertex;

public class vbo {
	public static final int STRIDE = 64;//(3 + 3 + 4 + 2) * 4; // 3 for vertex, 3 for normal, 4 for color and 2 for texture coordinates and * 4 for bytes.
	public static final int MAX_VERTS = 2048;//GL12.GL_MAX_ELEMENTS_INDICES;
	private static int current = 0;//, currentElements = 0;
	
	private static Texture tex = null;
	public static void Begin(int id) {
		current = id;
	}
	
	public static void setTexture(Texture newTex ) {
		if ( newTex == null  || (tex == null && newTex != null ) || newTex.getID() != tex.getID() ) {
			drawBuffer();
		}
		tex = newTex;
	}
	
	private static FloatBuffer curBuff = ByteBuffer.allocateDirect(MAX_VERTS*STRIDE*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
	private static IntBuffer curElemBuff = ByteBuffer.allocateDirect(MAX_VERTS*4).order(ByteOrder.nativeOrder()).asIntBuffer();
	private static int vp = 0;
	
	public static int getVP() {
		return vp;
	}
	
	public static void setBuffer(int id) {
		current = id;
	}
	
	public static void passToRenderList(float[] data, int count, RenderList rlist) {
		for ( int i = 0; i < count; i++ ) {
			Vertex[] tverts = new Vertex[3];
			
			int p = i*STRIDE;
			
			for ( int j = 0; j < 3; j++ ) {
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
	
	public static void setBuffer(float[] data, int count) {
		drawBuffer();
		curBuff.put(data);
		vp = count;
		drawBuffer();
	}
	
	public static void clearBuffer() {
		curBuff.clear();
		curElemBuff.clear();
		vp = 0;
	}
	
	public static void passTriangle(float[] tri) {
		if ( vp >= MAX_VERTS - 3 ) {
			drawBuffer();
		}
		
		curBuff.put(tri);
		for ( int i = 0; i < 3; i++ ) {
			curElemBuff.put(vp + i);
		}
		vp += 3;
	}
	
	
	public static void passQuad(float[] quad) {
		if ( vp >= MAX_VERTS - 4 ) {
			drawBuffer();
			clearBuffer();
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
	
	public static float[] toTriangle(Vertex a, Vertex b, Vertex c) {
		return new float[] {
				(float)a.pos.x, (float)a.pos.y, (float)a.pos.z,
				(float)a.normal.x, (float)a.normal.y, (float)a.normal.z,
				(float)a.r, (float)a.g, (float)a.b, (float)a.a,
				(float)a.tx, (float)a.ty,
				0, 0, 0, 0,
				
				(float)b.pos.x, (float)b.pos.y, (float)b.pos.z,
				(float)b.normal.x, (float)b.normal.y, (float)b.normal.z,
				(float)b.r, (float)b.g, (float)b.b, (float)b.a,
				(float)b.tx, (float)b.ty,
				0, 0, 0, 0,
				
				(float)c.pos.x, (float)c.pos.y, (float)c.pos.z,
				(float)c.normal.x, (float)c.normal.y, (float)c.normal.z,
				(float)c.r, (float)c.g, (float)c.b, (float)c.a,
				(float)c.tx, (float)c.ty,
				0, 0, 0, 0,
		};
	}
	
	public static float[] toQuad(Vertex a, Vertex b, Vertex c, Vertex d) {
		return new float[] {
				(float)a.pos.x, (float)a.pos.y, (float)a.pos.z,
				(float)a.normal.x, (float)a.normal.y, (float)a.normal.z,
				(float)a.r, (float)a.g, (float)a.b, (float)a.a,
				(float)a.tx, (float)a.ty,
				0, 0, 0, 0,
				
				(float)b.pos.x, (float)b.pos.y, (float)b.pos.z,
				(float)b.normal.x, (float)b.normal.y, (float)b.normal.z,
				(float)b.r, (float)b.g, (float)b.b, (float)b.a,
				(float)b.tx, (float)b.ty,
				0, 0, 0, 0,
				
				(float)c.pos.x, (float)c.pos.y, (float)c.pos.z,
				(float)c.normal.x, (float)c.normal.y, (float)c.normal.z,
				(float)c.r, (float)c.g, (float)c.b, (float)c.a,
				(float)c.tx, (float)c.ty,
				0, 0, 0, 0,
				
				(float)d.pos.x, (float)d.pos.y, (float)d.pos.z,
				(float)d.normal.x, (float)d.normal.y, (float)d.normal.z,
				(float)d.r, (float)d.g, (float)d.b, (float)d.a,
				(float)d.tx, (float)d.ty,
				0, 0, 0, 0,
		};
	}
	
	public static float[] toTriangle(Vector3 p1, Vector3 p2, Vector3 p3, Vector3 normal, float[][] texCoords, float r, float g, float b, float a) {
		return new float[] {
				(float)p1.x, (float)p1.y, (float)p1.z,
				(float)normal.x, (float)normal.y, (float)normal.z,
				  r, g, b, a,
				  texCoords[0][0], texCoords[0][1],
				  0, 0, 0, 0,
				  
				  (float)p2.x, (float)p2.y, (float)p2.z,
				  (float)normal.x, (float)normal.y, (float)normal.z,
				  r, g, b, a,
				  texCoords[1][0], texCoords[1][1],
				  0, 0, 0, 0,
				  
				  (float)p3.x, (float)p3.y, (float)p3.z,
				  (float)normal.x, (float)normal.y, (float)normal.z,
				  r, g, b, a,
				  texCoords[2][0], texCoords[2][1],
				  0, 0, 0, 0
		};
	}
	
	
	public static float[] toQuad(Vector3 p1, Vector3 p2, Vector3 p3, Vector3 p4, Vector3 normal, float[][] texCoords, float r, float g, float b, float a) {
		return new float[] {
				(float)p1.x, (float)p1.y, (float)p1.z,
				(float)normal.x, (float)normal.y, (float)normal.z,
				  r, g, b, a,
				  texCoords[0][0], texCoords[0][1],
				  0, 0, 0, 0,
				  
				  (float)p2.x, (float)p2.y, (float)p2.z,
				  (float)normal.x, (float)normal.y, (float)normal.z,
				  r, g, b, a,
				  texCoords[1][0], texCoords[1][1],
				  0, 0, 0, 0,
				  
				  (float)p3.x, (float)p3.y, (float)p3.z,
				  (float)normal.x, (float)normal.y, (float)normal.z,
				  r, g, b, a,
				  texCoords[2][0], texCoords[2][1],
				  0, 0, 0, 0,
				  
				  (float)p3.x, (float)p3.y, (float)p3.z,
				  (float)normal.x, (float)normal.y, (float)normal.z,
				  r, g, b, a,
				  texCoords[2][0], texCoords[2][1],
				  0, 0, 0, 0,
				  
				  (float)p4.x, (float)p4.y, (float)p4.z,
				  (float)normal.x, (float)normal.y, (float)normal.z,
				  r, g, b, a,
				  texCoords[3][0], texCoords[3][1],
				  0, 0, 0, 0,
				  
				  (float)p1.x, (float)p1.y, (float)p1.z,
				  (float)normal.x, (float)normal.y, (float)normal.z,
				  r, g, b, a,
				  texCoords[0][0], texCoords[0][1],
				  0, 0, 0, 0
		};
	}	

	public static int newID() {
		  if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
		    IntBuffer buffer = BufferUtils.createIntBuffer(1);
		    ARBVertexBufferObject.glGenBuffersARB(buffer);
		    return buffer.get(0);
		  }
		  return 0;
	}
	
	public static void setup() {
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_INDEX_ARRAY);
		
		ARBVertexBufferObject.glBindBufferARB( ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, current );
		// vertices
		int offset = 0 * 4; // 0 as its the first in the chunk, i.e. no offset. * 4 to convert to bytes.
		GL11.glVertexPointer(3, GL11.GL_FLOAT, STRIDE, offset);
		 
		// normals
		offset = 3 * 4; // 3 components is the initial offset from 0, then convert to bytes
		GL11.glNormalPointer(GL11.GL_FLOAT, STRIDE, offset);
		 
		// colors
		offset = (3 + 3) * 4; // (6*4) is the number of byte to skip to get to the color chunk
		GL11.glColorPointer(4, GL11.GL_FLOAT, STRIDE, offset);
		 
		// texture coordinates
		offset = (3 + 3 + 4) * 4;
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, STRIDE, offset);
	}
	
	public static void init() {
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY); 
		
		current = newID();
		

//		for (int i = 0; i < MAX_VERTS; i++ ) {
//			curElemBuff.put(i);
//		}
//		
//		curElemBuff.flip();
		 
		setup();
	}
	
	private static void bufferData(int id) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			if ( curBuff.position() != 0 ) {
				curBuff.flip();
				curElemBuff.flip();
				
				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
				
				ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, curBuff, ARBVertexBufferObject.GL_STREAM_DRAW_ARB);
			}
		}
	}
	
	/*private static void bufferElementData(int id, IntBuffer buffer) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
			ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, buffer, ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB);
		}
	}*/
	
	
	public static void drawBuffer() {
		if ( current != 0 && vp >= 3) {
			setup();
			bufferData(current);
			//gl.LineWidth(4);
			if ( tex == null ) {
				Texture.unbind();
			} else {
				tex.bind();
			}
			GL12.glDrawRangeElements(GL11.GL_TRIANGLES, 0, vp, curElemBuff);
			clearBuffer();
			GL11.glFlush();
		}
	}
}

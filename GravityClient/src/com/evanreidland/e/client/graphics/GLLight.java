package com.evanreidland.e.client.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import com.evanreidland.e.graphics.Light;

public class GLLight extends Light
{
	public void Apply()
	{
		GL11.glEnable(GL11.GL_LIGHTING);
		try
		{
			int lightID = GL11.GL_LIGHT0;

			switch (id)
			{
			case 1:
				lightID = GL11.GL_LIGHT1;
				break;
			case 2:
				lightID = GL11.GL_LIGHT2;
				break;
			case 3:
				lightID = GL11.GL_LIGHT3;
				break;
			case 4:
				lightID = GL11.GL_LIGHT4;
				break;
			case 5:
				lightID = GL11.GL_LIGHT5;
				break;
			case 6:
				lightID = GL11.GL_LIGHT6;
				break;
			case 7:
				lightID = GL11.GL_LIGHT7;
				break;
			default:
				break;
			}

			if (bActive)
			{
				GL11.glEnable(lightID);

				ByteBuffer a = ByteBuffer.allocateDirect(16);
				a.order(ByteOrder.nativeOrder());

				ByteBuffer b = ByteBuffer.allocateDirect(16);
				b.order(ByteOrder.nativeOrder());

				ByteBuffer c = ByteBuffer.allocateDirect(16);
				c.order(ByteOrder.nativeOrder());

				ByteBuffer v = ByteBuffer.allocateDirect(16);
				v.order(ByteOrder.nativeOrder());

				FloatBuffer af = a.asFloatBuffer();
				FloatBuffer bf = b.asFloatBuffer();
				FloatBuffer cf = c.asFloatBuffer();
				FloatBuffer vf = v.asFloatBuffer();

				af.put(new float[]
				{
						(float) ambR, (float) ambG, (float) ambB, (float) ambA
				});
				bf.put(new float[]
				{
						(float) diffR, (float) diffG, (float) diffB,
						(float) diffA
				});
				cf.put(new float[]
				{
						(float) specR, (float) specG, (float) specB,
						(float) specA
				});

				vf.put(new float[]
				{
						(float) pos.x, (float) pos.y, (float) pos.z, 1.0f
				});

				af.flip();
				bf.flip();
				cf.flip();
				vf.flip();

				GL11.glLight(lightID, GL11.GL_AMBIENT, af);
				GL11.glLight(lightID, GL11.GL_DIFFUSE, bf);
				GL11.glLight(lightID, GL11.GL_SPECULAR, cf);
				GL11.glLight(lightID, GL11.GL_POSITION, vf);
			}
			else
			{
				GL11.glDisable(lightID);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public GLLight()
	{
		super();
	}
}

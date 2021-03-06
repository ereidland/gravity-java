package com.evanreidland.e.graphics;

public class Tri
{
	public Vertex[] vert;
	
	public Tri pass()
	{
		graphics.passTriangle(vert[0], vert[1], vert[2]);
		return this;
	}
	
	public Tri setColor(double r, double g, double b, double a)
	{
		for (int i = 0; i < vert.length; i++)
		{
			vert[i].r = r;
			vert[i].g = g;
			vert[i].b = b;
			vert[i].a = a;
		}
		return this;
	}
	
	public Tri(Vertex a, Vertex b, Vertex c)
	{
		vert = new Vertex[3];
		vert[0] = new Vertex(a);
		vert[1] = new Vertex(b);
		vert[2] = new Vertex(c);
	}
	
	public Tri()
	{
		vert = new Vertex[3];
		for (int i = 0; i < vert.length; i++)
		{
			vert[i] = new Vertex();
		}
		
		vert[0].tx = 1;
		vert[0].ty = 0;
		vert[1].tx = 0;
		vert[1].ty = 0;
		vert[2].tx = 0;
		vert[2].ty = 1;
	}
}

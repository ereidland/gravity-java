package com.evanreidland.e.graphics;

public abstract class RenderList extends TriOperator
{
	public abstract void Render();

	public abstract void Begin();

	public void addQuad(Vertex a, Vertex b, Vertex c, Vertex d)
	{
		addTri(a, b, c);
		addTri(c, d, a);
	}

	public abstract void addTri(Vertex a, Vertex b, Vertex c);

	public abstract void End();

	public void addTriList(TriList tris)
	{
		for (int i = 0; i < tris.size(); i++)
		{
			Tri tri = tris.get(i);
			addTri(tri.vert[0], tri.vert[1], tri.vert[2]);
		}
	}
}

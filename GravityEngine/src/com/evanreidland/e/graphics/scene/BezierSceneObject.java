package com.evanreidland.e.graphics.scene;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.graphics.SceneObject;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.phys.Bezier;

public class BezierSceneObject extends SceneObject
{
	public Bezier bezier;
	public double increment;
	public double r, g, b, a, width;
	
	public void Render()
	{
		if (increment > 0 && bezier.size() >= 2)
		{
			Vector3 lastp = bezier.get(0);
			for (int i = 1; i < bezier.size(); i++)
			{
				Vector3 newp = bezier.get(i);
				graphics.drawLine(lastp, newp, width, r, g, b, a * 0.5);
				lastp = newp;
			}
			lastp = bezier.get(0);
			for (double i = increment; i < 1 + increment; i += increment)
			{
				Vector3 newp = bezier.calc(i);
				graphics.drawLine(lastp, newp, width * 0.5, r, g, b, a);
				lastp = newp;
			}
		}
	}
	
	public BezierSceneObject(Bezier bezier, double increment)
	{
		this.bezier = bezier;
		this.increment = increment;
		r = g = b = a = 1;
		width = 2;
	}
}

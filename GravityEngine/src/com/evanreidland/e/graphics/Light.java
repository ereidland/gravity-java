package com.evanreidland.e.graphics;

import com.evanreidland.e.Vector3;

public abstract class Light
{
	public Vector3 pos, face;

	public double diffR, diffG, diffB, diffA, ambR, ambG, ambB, ambA, specR,
			specG, specB, specA;
	public int id;

	public void setAmbient(double r, double g, double b, double a)
	{
		ambR = r;
		ambG = g;
		ambB = b;
		ambA = a;
	}

	public void setDiffuse(double r, double g, double b, double a)
	{
		diffR = r;
		diffG = g;
		diffB = b;
		diffA = a;
	}

	public void setSpecular(double r, double g, double b, double a)
	{
		specR = r;
		specG = g;
		specB = b;
		specA = a;
	}

	public boolean bActive;

	public abstract void Apply();

	public Light()
	{
		pos = Vector3.Zero();
		face = Vector3.Zero();
		diffR = diffG = diffB = diffA = ambR = ambG = ambB = ambA = specR = specG = specB = specA = 1;
		id = 0;

		bActive = true;
	}
}

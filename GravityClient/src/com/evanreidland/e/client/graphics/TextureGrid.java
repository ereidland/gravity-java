package com.evanreidland.e.client.graphics;

public class TextureGrid
{
	private double width, height;

	public double x(double u)
	{
		return u / width;
	}

	public double y(double v)
	{
		return v / height;
	}

	public double getWidth()
	{
		return width;
	}

	public double getHeight()
	{
		return height;
	}

	public TextureGrid(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
}

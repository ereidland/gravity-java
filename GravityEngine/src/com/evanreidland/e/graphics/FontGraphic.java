package com.evanreidland.e.graphics;

import com.evanreidland.e.Resource;
import com.evanreidland.e.ResourceType;

//TODO finish.
public class FontGraphic extends Resource
{
	private Resource fontTexture;

	public Resource getTexture()
	{
		return fontTexture;
	}

	private double[] widths;
	private double texSize, padding;

	public double getPadding()
	{
		return padding;
	}

	public double getTextureRatio()
	{
		return texSize;
	}

	public double tx(char ch)
	{
		return ((ch % 16) / 16f) * texSize;
	}

	public double ty(char ch)
	{
		return ((ch / 16) / 16f) * texSize;
	}

	public double interval()
	{
		return texSize * (0.0625f);
	}

	public double interval(char ch)
	{
		return interval() * getCharWidth(ch);
	}

	public double getCharWidth(char ch)
	{
		return (ch >= 0 && ch < 256 ? widths[ch] : 0);
	}

	public double[] getWidths()
	{
		return widths;
	}

	public double getStringWidth(String str, double size)
	{
		double len = 0;
		for (int i = 0; i < str.length(); i++)
		{
			len += getCharWidth(str.charAt(i)) * size
					* (i == 0 || i == str.length() - 1 ? 0.5f : 1);
		}
		return len;
	}

	public Quad buildQuad(char ch)
	{
		Quad q = new Quad();
		double xsize = getCharWidth(ch);
		q.vert[0].pos.setAs(-xsize * 0.5, 0.5, 0);
		q.vert[1].pos.setAs(xsize * 0.5, 0.5, 0);
		q.vert[2].pos.setAs(xsize * 0.5, -0.5, 0);
		q.vert[3].pos.setAs(-xsize * 0.5, -0.5, 0);

		double tx = tx(ch), ty = ty(ch);

		double xinterval = interval(ch);
		double yinterval = interval();

		q.vert[0].tx = tx;
		q.vert[0].ty = ty;
		q.vert[1].tx = tx + xinterval;
		q.vert[1].ty = ty;
		q.vert[2].tx = tx + xinterval;
		q.vert[2].ty = ty + yinterval;
		q.vert[3].tx = tx;
		q.vert[3].ty = ty + yinterval;

		return q;
	}

	public FontGraphic()
	{
		super(ResourceType.Font, null, false);
		fontTexture = null;
		widths = new double[256];
		texSize = padding = 0;
	}

	public FontGraphic(Resource fontTexture, double[] widths, double texSize,
			double padding)
	{
		super(ResourceType.Font, fontTexture, true);
		this.fontTexture = fontTexture;
		this.widths = widths;
		this.texSize = texSize;
		this.padding = padding;

		double imageSize = fontTexture.info.get("width").toDouble();
		if (imageSize != 0)
		{
			this.texSize /= imageSize;
			this.padding /= imageSize;
		}
	}
}

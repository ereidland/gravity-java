package com.evanreidland.e.client.gui;

import com.evanreidland.e.Resource;
import com.evanreidland.e.engine;
import com.evanreidland.e.graphics.Quad;
import com.evanreidland.e.graphics.font;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.gui.GUI;

public class GravityGUIObject extends GUI
{
	public static Resource defaultFont = engine.loadFont("Courier Newx32");
	public Resource tex, fnt;
	public String text;
	public double fontSize;
	
	public void onUpdate()
	{
	}
	
	public void onRender()
	{
		graphics.setTexture(tex);
		renderQuadOnRect(new Quad());
		font.r = font.g = 1;
		font.b = 0;
		font.a = 1;
		font.Render2d(fnt, text, rect.getCenter(),
				(rect.getWidth() * 0.75 / font.getWidth(fnt, text, fontSize))
						* fontSize, true);;
	}
	
	public boolean onClick(double x, double y, boolean down)
	{
		return true;
	}
	
	public GravityGUIObject(double width, double height, String name)
	{
		super(name);
		rect.b.x = width;
		rect.b.y = height;
		
		fontSize = 32;
		text = "Stuff";
		tex = null;
		fnt = defaultFont;
	}
}

package com.evanreidland.e.client.gui;

import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
import com.evanreidland.e.graphics.Quad;
import com.evanreidland.e.graphics.font;
import com.evanreidland.e.graphics.graphics;

public class TextField extends GravityGUIObject
{
	public int maxLength;
	
	private long lastBackspace, lastFlash;
	private boolean bFlash;
	public boolean bFocused;
	
	public void onRender()
	{
		graphics.setTexture(null);
		renderQuadOnRect(new Quad().setColor(0.5, 0.5, 0.5, 0.25));
		font.r = font.g = font.b = font.a = 1;
		font.Render2d(fnt, text, rect.topLeft().plus(0, fontSize * 0.5, 0),
				fontSize, false);
		if (bFlash && bFocused)
			font.Render2d(
					fnt,
					"_",
					rect.topLeft().plus(
							font.getWidth(fnt, text + "_", fontSize),
							fontSize * 0.5, 0), fontSize, true);
	}
	
	public void onEnter(String text)
	{
		
	}
	
	public void onUpdate()
	{
		if (bFocused)
		{
			if (System.currentTimeMillis() > lastFlash + 250)
			{
				lastFlash = System.currentTimeMillis();
				bFlash = !bFlash;
			}
			if (input.getTyped().length() > 0)
			{
				lastFlash = System.currentTimeMillis();
				bFlash = true;
				text += input.getTyped();
			}
			
			if (input.isKeyDown(key.KEY_BACKSPACE))
			{
				lastBackspace = 0;
			}
			if (input.getKeyState(key.KEY_BACKSPACE)
					&& System.currentTimeMillis() > lastBackspace + 100)
			{
				lastBackspace = (lastBackspace == 0 ? 100 : 0)
						+ System.currentTimeMillis();
				if (text.length() > 1)
					text = text.substring(0, text.length() - 1);
				else
					text = "";
			}
			if (input.isKeyDown(key.KEY_ENTER))
			{
				if (text.length() > 0)
					onEnter(text);
				text = "";
				bFocused = false;
			}
		}
	}
	
	public TextField(String name, double width, double height)
	{
		super(name, width, height);
		maxLength = 64;
		fontSize = 16;
		bFocused = false;
		flags.addFromObject(this, "bFocused", "focused");
		text = "";
	}
	
}

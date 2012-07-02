package com.evanreidland.e.gravity.gui;

import com.evanreidland.e.Resource;
import com.evanreidland.e.engine;
import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
import com.evanreidland.e.graphics.Quad;
import com.evanreidland.e.graphics.font;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.gui.GUIObject;

public class Button extends GUIObject {
	public static Resource defaultFont = engine.loadFont("Courier Newx32");
	public Resource tex, fnt;
	public String text;
	public double fontSize;
	
	public void onUpdate() {
		if ( input.isKeyUp(key.MOUSE_LBUTTON ) ) {
			bClicked = false;
		}
		text = bClicked ? "CLICKED!" : "Button :(";
	}
	
	public void onRender() {
		graphics.setTexture(tex);
		renderQuadOnRect(new Quad());
		font.r = font.g = 1;
		font.b = 0;
		font.a = 1;
		font.Render2d(fnt, text, rect.getCenter(), (rect.getWidth()*0.75/font.getWidth(fnt, text, fontSize))*fontSize, true);
	}
	public boolean onClick(double x, double y) {
		bClicked = true;
		return true;
	}
	public Button(double width, double height, String name) {
		super(name);
		rect.b.x = width;
		rect.b.y = height;
		
		fontSize = 32;
		text = "Button";
		tex = null;
		fnt = defaultFont;
	}
}

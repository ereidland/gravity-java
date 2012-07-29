package com.evanreidland.e.client.gui;

import java.util.Vector;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
import com.evanreidland.e.graphics.Quad;
import com.evanreidland.e.graphics.font;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.gui.GUI;

public class MessageArea extends GravityGUIObject
{
	public class MessageItem
	{
		public float r, g, b, a;
		public String text;
		public long timestamp;
		
		public void Update()
		{
			if (a <= 0)
				return;
			if (System.currentTimeMillis() > timestamp + defaultTime)
			{
				a -= fadeSpeed * Game.getDelta();
			}
		}
		
		public MessageItem(String text)
		{
			this.text = text;
			r = g = b = a = 1;
			timestamp = System.currentTimeMillis();
		}
		
		public void Render(Vector3 pos)
		{
			if (a <= 0)
				return;
			font.r = r;
			font.g = g;
			font.b = b;
			font.a = a;
			font.Render2d(fnt, text, pos, fontSize, false);
		}
	}
	
	private Vector<MessageItem> items;
	
	public long defaultTime;
	public float fadeSpeed;
	public int maxItems;
	
	public void addItem(String line)
	{
		if (maxItems > 0)
		{
			items.add(new MessageItem(line));
			while (items.size() > maxItems)
				items.remove(0);
		}
		else
			items.clear();
	}
	
	public void onUpdate()
	{
		if (input.isKeyDown(key.KEY_ENTER))
		{
			for (int i = 0; i < items.size(); i++)
			{
				items.get(i).a = 1;
			}
		}
		GUI object = gui.getObject("chatfield");
		if (object != null && !object.flags.getBoolState("focused"))
		{
			for (int i = 0; i < items.size(); i++)
			{
				items.get(i).Update();
			}
		}
	}
	
	public void onRender()
	{
		GUI object = gui.getObject("chatfield");
		if (object != null && object.flags.getBoolState("focused"))
		{
			graphics.setTexture(null);
			renderQuadOnRect(new Quad().setColor(0.5, 0.5, 0.5, 0.25));
		}
		
		for (int i = 0; i < items.size(); i++)
		{
			items.get(i).Render(
					rect.topLeft().plus(
							0,
							((items.size() - 1) - i) * (fontSize + 4)
									+ fontSize * 0.5, 0));
		}
	}
	
	public MessageArea(String name, double width, double height,
			double textHeight)
	{
		super(name, width, height);
		fontSize = textHeight;
		defaultTime = 10000;
		fadeSpeed = 0.5f;
		maxItems = (int) (height / textHeight);
		
		items = new Vector<MessageItem>();
	}
}

package com.evanreidland.e.client.gui;

import java.util.Vector;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.graphics.Quad;
import com.evanreidland.e.graphics.font;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.phys.Rect3;
import com.evanreidland.e.script.Value;

public class EngineeringPanel extends GravityGUIObject
{
	public static class PanelItem
	{
		public Value value;
		public String name;
		public boolean isToggle;
		public double min, max;
		
		public PanelItem setRange(double min, double max)
		{
			this.min = min;
			this.max = max;
			return this;
		}
		
		public PanelItem(String name, Value value)
		{
			this.name = name;
			this.value = value;
			isToggle = false;
			min = 0;
			max = 1;
		}
	}
	
	double itemWidth, itemHeight, itemSpacing;
	
	private Vector<PanelItem> items;
	
	public void onRender()
	{
		graphics.setTexture(null);
		Quad q = new Quad();
		q.setColor(0.3, 0.3, 0.3, 0.5);
		renderQuadOnRect(q, rect);
		for (int i = 0; i < items.size(); i++)
		{
			graphics.setTexture(null);
			PanelItem item = items.get(i);
			Vector3 pos = rect.topLeft().plus(
					i * itemSpacing + itemSpacing * 0.5, itemSpacing * 0.5, 0);
			Rect3 rect = new Rect3(pos.minus(itemWidth * 0.5, 0, 0), pos.plus(
					itemWidth * 0.5, itemHeight, 0));
			if (item.isToggle)
			{
				if (item.value.toBool())
					q.setColor(1, 1, 0, 1);
				else
					q.setColor(0.5, 0.5, 0.5, 1);
				rect.b.y -= itemHeight * 0.5;
			}
			else
			{
				q.setColor(0.5, 0.5, 0.5, 1);
				renderQuadOnRect(q, rect);
				rect.b.y = rect.a.y
						+ ((item.value.toDouble() - item.min) / (item.max - item.min))
						* itemHeight;
				q.setColor(0, 1, 0, 1);
			}
			renderQuadOnRect(q, rect);
			
			String text = item.name;
			font.Render2d(fnt, text, pos.plus(0, -fontSize * 0.5, 0), fontSize,
					true);
		}
	}
	
	public PanelItem getItem(String name)
	{
		for (int i = 0; i < items.size(); i++)
		{
			PanelItem item = items.get(i);
			if (item.name.equals(name))
				return item;
		}
		return null;
	}
	
	public boolean remove(String name)
	{
		PanelItem item = getItem(name);
		return item != null ? items.remove(item) : false;
	}
	
	public PanelItem add(String name, Value value, boolean isToggle)
	{
		PanelItem item = getItem(name);
		if (item != null)
			items.remove(item);
		item = new PanelItem(name, value);
		item.isToggle = isToggle;
		items.add(item);
		
		rect.b.x = rect.a.x + (items.size() + 0.5) * itemSpacing;
		
		return item;
	}
	
	public EngineeringPanel(String name, double width, double height)
	{
		super(name, width, height);
		
		items = new Vector<PanelItem>();
		
		itemWidth = 16;
		itemHeight = 128;
		itemSpacing = 64;
		fontSize = 16;
	}
}

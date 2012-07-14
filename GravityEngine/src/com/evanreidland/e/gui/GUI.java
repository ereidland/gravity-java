package com.evanreidland.e.gui;

import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.graphics.Quad;
import com.evanreidland.e.phys.Rect3;

public class GUI
{
	public Rect3 rect;
	public boolean bVisible, bActive;
	public Flags flags;
	
	private String name;
	
	public String getName()
	{
		return name;
	}
	
	private Vector<GUI> objects;
	private HashMap<String, GUI> objectMap;
	
	public void add(GUI object)
	{
		if (!objects.contains(object))
		{
			objects.add(object);
			objectMap.put(object.getName(), object);
		}
	}
	
	public void remove(GUI object)
	{
		if (objects.remove(object))
		{
			objectMap.remove(object.getName());
		}
	}
	
	public GUI getObject(String name)
	{
		return objectMap.get(name);
	}
	
	public void onUpdate()
	{
		for (int i = 0; i < objects.size(); i++)
		{
			GUI object = objects.get(i);
			if (object.bActive)
			{
				object.onUpdate();
			}
		}
	}
	
	public void onRender()
	{
		for (int i = 0; i < objects.size(); i++)
		{
			GUI object = objects.get(i);
			if (object.bVisible)
			{
				object.onRender();
			}
		}
	}
	
	public void onType(char key)
	{
		for (int i = 0; i < objects.size(); i++)
		{
			GUI object = objects.get(i);
			if (object.bActive)
			{
				object.onType(key);
			}
		}
	}
	
	public boolean checkClick(double x, double y, boolean down)
	{
		if (rect.containsPoint(new Vector3(x, y, 0)))
		{
			boolean grabs = onClick(x, y, down);
			if (grabs)
			{
				return true;
			}
			for (int i = 0; i < objects.size(); i++)
			{
				grabs = objects.get(i).checkClick(x, y, down);
				if (grabs)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean onClick(double x, double y, boolean down)
	{
		return false;
	}
	
	public void Layout()
	{
		for (int i = 0; i < objects.size(); i++)
		{
			GUI object = objects.get(i);
			if (object.bActive)
			{
				object.onUpdate();
			}
		}
	}
	
	public void renderQuadOnRect(Quad quad, Rect3 rect)
	{
		quad.vert[0].pos = new Vector3(rect.a.x, rect.a.y, 0);
		quad.vert[1].pos = new Vector3(rect.b.x, rect.a.y, 0);
		quad.vert[2].pos = new Vector3(rect.b.x, rect.b.y, 0);
		quad.vert[3].pos = new Vector3(rect.a.x, rect.b.y, 0);
		quad.pass();
	}
	
	public void renderQuadOnRect(Quad quad)
	{
		renderQuadOnRect(quad, rect);
	}
	
	public GUI(String name)
	{
		this.name = name;
		objects = new Vector<GUI>();
		objectMap = new HashMap<String, GUI>();
		rect = new Rect3(Vector3.Zero(), Vector3.Zero());
		flags = new Flags();
		bVisible = false;
		bActive = true;
		
		flags.addFromObject(this, "bVisible", "visible");
		flags.addFromObject(this, "bActive", "active");
	}
}

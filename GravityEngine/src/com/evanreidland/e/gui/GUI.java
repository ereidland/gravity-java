package com.evanreidland.e.gui;

import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.Vector3;

//Note: Although GUIObjects can have child objects, all objects are added to the GUI the object is a part of.
public class GUI
{
	public static enum Layout
	{
		DEFAULT, RIGHT_DOWN, RIGHT, DOWN,
	}

	private Vector<GUIObject> objects;
	private HashMap<String, GUIObject> objectsMap;

	public Layout layout;
	public Vector3 layoutOrigin;

	public GUIObject addObject(GUIObject object)
	{
		if (!objects.contains(object))
		{
			objects.add(object);
			objectsMap.put(object.getName(), object);
		}
		return object;
	}

	public void Render()
	{
		for (int i = 0; i < objects.size(); i++)
		{
			GUIObject object = objects.get(i);
			if (object.parent == null
					&& !object.settings.get("hidden").asBool())
			{
				object.Render();
			}
		}
	}

	public void Update()
	{
		for (int i = 0; i < objects.size(); i++)
		{
			GUIObject object = objects.get(i);
			if (object.parent == null)
			{
				object.Update();
			}
		}
	}

	public GUIObject getObject(String name)
	{
		return objectsMap.get(name);
	}

	public GUIObject removeObject(GUIObject object)
	{
		if (object != null)
		{
			if (object.getGUI() != null)
			{
				object.remove();
			}
			else
			{
				objects.remove(object);
				objectsMap.remove(object.getName());
			}
		}
		return object;
	}

	public GUIObject removeObject(String name)
	{
		return removeObject(objectsMap.get(name));
	}

	public Vector<GUIObject> getObjectChildren(String parentName)
	{
		Vector<GUIObject> children = new Vector<GUIObject>();
		for (int i = 0; i < objects.size(); i++)
		{
			GUIObject object = objects.get(i);
			if (object.parent != null
					&& object.parent.getName().equals(parentName))
			{
				children.add(object);
			}
		}
		return children;
	}

	public void set(String objectName, String name, String newValue)
	{
		GUIObject object = getObject(objectName);
		if (object != null)
		{
			object.settings.set(name, newValue);
		}
	}

	public void setForChildren(String objectName, String name, String newValue)
	{
		Vector<GUIObject> children = getObjectChildren(objectName);
		for (int i = 0; i < children.size(); i++)
		{
			GUIObject object = children.get(i);
			object.settings.set(name, newValue);
			setForChildren(object.getName(), name, newValue);
		}
	}

	public void layoutObject(GUIObject object)
	{
		// TODO code.
	}

	public void Layout()
	{
		for (int i = 0; i < objects.size(); i++)
		{
			GUIObject object = objects.get(i);
			if (object.parent != null)
			{
				layoutObject(object);
			}
		}
	}

	public boolean onClick(double x, double y)
	{
		for (int i = 0; i < objects.size(); i++)
		{
			GUIObject object = objects.get(i);
			if (object.parent == null
					&& object.rect.containsPoint(new Vector3(x, y, 0)))
			{
				if (object.onClick(x, y))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void onType(char key)
	{
		for (int i = 0; i < objects.size(); i++)
		{
			objects.get(i).onType(key);
		}
	}

	public GUI()
	{
		objects = new Vector<GUIObject>();
		objectsMap = new HashMap<String, GUIObject>();

		layout = Layout.RIGHT_DOWN;
		layoutOrigin = Vector3.Zero();
	}
}

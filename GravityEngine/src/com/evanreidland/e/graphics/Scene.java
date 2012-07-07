package com.evanreidland.e.graphics;

import java.util.Vector;

import com.evanreidland.e.Flags.State;
import com.evanreidland.e.ent.Entity;

// Potential break. Maybe SceneObjects should use an ID system so that they can't add recursively?
public class Scene extends SceneObject
{
	private Vector<SceneObject> objects, orderedObjects;

	public boolean autoRemove, autoPosition, autoOrder;

	public int getObjectCount()
	{
		return objects.size();
	}

	public SceneObject addObject(SceneObject object, boolean order)
	{
		if (order)
		{
			double dist = object.calcRoughDistance();
			for (int i = 0; i < orderedObjects.size(); i++)
			{
				double otherDist = orderedObjects.get(i).getRoughDistance();
				if (dist > otherDist)
				{
					orderedObjects.insertElementAt(object, i);
					return object;
				}
			}
			orderedObjects.add(object);
		}
		else
		{
			objects.add(object);
		}
		return object;
	}

	public SceneObject addObject(SceneObject object)
	{
		return addObject(object, true);
	}

	public SceneObject addObject(SceneObject object, Entity parent,
			AnchorType anchorType)
	{
		addObject(object);
		object.setParentEntity(parent);
		object.anchorType = anchorType;
		return object;
	}

	public SceneObject addObject(SceneObject object, Entity parent)
	{
		return addObject(object, parent, AnchorType.POS_ANGLE);
	}

	public SceneObject addObject(SceneObject object, SceneObject parent,
			AnchorType anchorType)
	{
		parent.child.addObject(object);
		object.setParentObject(parent);
		object.anchorType = anchorType;
		return object;
	}

	public SceneObject addObject(SceneObject object, SceneObject parent)
	{
		return addObject(object, parent, AnchorType.POS_ANGLE);
	}

	public void Position()
	{
		for (int i = 0; i < objects.size(); i++)
		{
			objects.get(i).Position();
		}

		for (int i = 0; i < orderedObjects.size(); i++)
		{
			orderedObjects.get(i).Position();
		}
	}

	public void Render()
	{
		if (objects.size() == 0 && orderedObjects.size() == 0)
			return;
		if (autoRemove)
		{
			bringOutTheDead();
		}
		if (autoPosition)
		{
			Position();
		}
		if (autoOrder)
		{
			Order();
		}
		for (int i = 0; i < objects.size(); i++)
		{
			objects.get(i).Render();
		}
		for (int i = 0; i < orderedObjects.size(); i++)
		{
			orderedObjects.get(i).Render();
		}
	}

	public void Order()
	{

		int i = 0;
		while (i < objects.size())
		{
			SceneObject object = objects.get(i);
			if (object.zOrder)
			{
				objects.remove(i);
				orderedObjects.add(object);
			}
			else
			{
				i++;
			}
		}

		Vector<SceneObject> currentObjects = new Vector<SceneObject>(
				orderedObjects);
		Vector<SceneObject> newOrderedObjects = new Vector<SceneObject>();

		for (i = 0; i < currentObjects.size(); i++)
		{
			SceneObject object = currentObjects.get(i);
			object.Order();
			if (object.zOrder)
			{
				boolean added = false;
				double dist = object.calcRoughDistance();
				for (int j = 0; j < newOrderedObjects.size(); j++)
				{
					double otherDist = newOrderedObjects.get(j)
							.getRoughDistance();
					if (dist > otherDist)
					{
						newOrderedObjects.insertElementAt(object, j);
						added = true;
						break;
					}
				}
				if (!added)
				{
					newOrderedObjects.add(object);
				}
			}
			else
			{
				objects.add(object);
			}
		}

		orderedObjects = new Vector<SceneObject>();
		for (i = 0; i < newOrderedObjects.size(); i++)
		{
			orderedObjects.add(newOrderedObjects.get(i));
		}
	}

	public void bringOutTheDead()
	{
		Vector<SceneObject> dead = new Vector<SceneObject>();
		for (int i = 0; i < objects.size(); i++)
		{
			SceneObject object = objects.get(i);
			if (object.getParentEntity() != null)
			{
				if (object.getParentEntity().flags.getState("dead") == State.True)
				{
					dead.add(object);
				}
			}
		}
		for (int i = 0; i < dead.size(); i++)
		{
			objects.remove(dead.get(i));
		}

		dead.clear();
		for (int i = 0; i < orderedObjects.size(); i++)
		{
			SceneObject object = orderedObjects.get(i);
			if (object.getParentEntity() != null)
			{
				if (object.getParentEntity().flags.getState("dead") == State.True)
				{
					dead.add(object);
				}
			}
		}
		for (int i = 0; i < dead.size(); i++)
		{
			orderedObjects.remove(dead.get(i));
		}
	}

	public Scene()
	{
		super(false);
		objects = new Vector<SceneObject>();
		orderedObjects = new Vector<SceneObject>();
		autoRemove = true;
		autoPosition = true;
		autoOrder = true;
		child = this;
	}
}

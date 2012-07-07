package com.evanreidland.e.graphics;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;

public abstract class SceneObject
{
	public static enum AnchorType
	{
		NONE, POS, POS_ANGLE,
	}

	public boolean zOrder = false;
	public Vector3 pos, angle, offset, angleOffset;
	private double roughDistance;

	private Entity parentEntity;
	private SceneObject parentObject;

	public AnchorType anchorType;

	public Scene child;

	public void setParentEntity(Entity ent)
	{
		parentEntity = ent;
		parentObject = null;
	}

	public void setParentObject(SceneObject object)
	{
		parentObject = object;
		parentEntity = null;
	}

	public SceneObject getParentObject()
	{
		return parentObject;
	}

	public Entity getParentEntity()
	{
		return parentEntity;
	}

	public Vector3 getParentPos()
	{
		return parentEntity != null ? parentEntity.pos
				: parentObject != null ? parentObject.pos : Vector3.Zero();
	}

	public Vector3 getParentAngle()
	{
		return parentEntity != null ? parentEntity.angle
				: parentObject != null ? parentObject.angle : Vector3.Zero();
	}

	public void Position()
	{
		switch (anchorType)
		{
		case NONE:
			break;
		case POS_ANGLE:
			angle.setAs(getParentAngle());
		case POS:
			pos.setAs(getParentPos());
			break;
		}

		if (child != null)
			child.Position();
	}

	public final double getRoughDistance()
	{
		return roughDistance;
	}

	public final double getRoughDistance(Vector3 point)
	{
		return point.minus(pos).getRoughLength();
	}

	public final double calcRoughDistance()
	{
		return (roughDistance = getRoughDistance(graphics.camera.pos));
	}

	public abstract void Render();

	public void Order()
	{
		if (child != null)
		{
			child.Order();
		}
	}

	public SceneObject()
	{
		pos = Vector3.Zero();
		offset = Vector3.Zero();
		angle = Vector3.Zero();
		angleOffset = Vector3.Zero();
		roughDistance = -1;
		anchorType = AnchorType.NONE;
		child = new Scene();
		child.setParentObject(this);
		zOrder = true;
	}

	public SceneObject(boolean initChild)
	{
		pos = Vector3.Zero();
		angle = Vector3.Zero();
		roughDistance = -1;
		anchorType = AnchorType.NONE;
		zOrder = false;
		if (initChild)
		{
			child = new Scene();
			child.setParentObject(this);
		}
		else
		{
			child = null;
		}
	}
}

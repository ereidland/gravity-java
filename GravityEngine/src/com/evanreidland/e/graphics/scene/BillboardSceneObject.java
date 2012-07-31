package com.evanreidland.e.graphics.scene;

import com.evanreidland.e.graphics.SceneObject;
import com.evanreidland.e.graphics.Sprite;

public class BillboardSceneObject extends SceneObject
{
	public Sprite sprite;

	// Note: Type true means face the camera, type false means rotate with the
	// camera angle.
	public boolean type;

	public void Render()
	{
		sprite.pos.setAs(pos.plus(offset));
		sprite.angle.setAs(angle.plus(offset));
		sprite.renderBillboard(type);
	}

	public BillboardSceneObject(Sprite sprite, boolean type)
	{
		this.sprite = sprite;
		this.type = type;
		this.zOrder = true;
	}

}

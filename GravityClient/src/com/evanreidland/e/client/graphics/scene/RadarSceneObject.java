package com.evanreidland.e.client.graphics.scene;

import com.evanreidland.e.Resource;
import com.evanreidland.e.graphics.SceneObject;
import com.evanreidland.e.graphics.Sprite;

public class RadarSceneObject extends SceneObject
{
	public static Resource zarrows = null;
	public static double texperlen = 0.25;
	
	public void Render()
	{
		Sprite billboard = new Sprite(1, 1, zarrows);
		billboard.cr = billboard.cg = 0;
		billboard.renderLongBillboard(pos.multipliedBy(1, 1, 0), pos, 1, 0,
				pos.z, true);
	}
	
	public RadarSceneObject()
	{
	}
}

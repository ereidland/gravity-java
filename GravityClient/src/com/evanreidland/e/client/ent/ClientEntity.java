package com.evanreidland.e.client.ent;

import com.evanreidland.e.client.graphics.scene.RadarSceneObject;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.graphics.SceneObject.AnchorType;
import com.evanreidland.e.graphics.graphics;

public class ClientEntity extends Entity
{
	public void setupGraphics()
	{
		
	}
	
	public void onSpawn()
	{
		setupGraphics();
	}
	
	public void addRadarObject()
	{
		graphics.scene.addObject(new RadarSceneObject(), this, AnchorType.POS);
	}
	
	public ClientEntity(String className, long id)
	{
		super(className, id);
	}
}

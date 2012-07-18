package com.evanreidland.e.client.ent;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.graphics.ModelSceneObject;
import com.evanreidland.e.graphics.generate;
import com.evanreidland.e.graphics.graphics;

public class ClientShip extends ClientEntity
{
	public Vector3 velThrust, angleThrust;
	
	public void setupGraphics()
	{
		Model model = generate.Sphere(Vector3.Zero(), new Vector3(0.05, 0.05,
				0.05), Vector3.Zero(), 8, 8);
		
		model.tex = engine.loadTexture("sun1.png");
		graphics.scene.addObject(new ModelSceneObject(model), this);
	}
	
	public void onSpawn()
	{
		setupGraphics();
	}
	
	public void onThink()
	{
		super.onThink();
	}
	
	public ClientShip(long id)
	{
		super("ship", id);
		velThrust = Vector3.Zero();
		angleThrust = Vector3.Zero();
	}
}

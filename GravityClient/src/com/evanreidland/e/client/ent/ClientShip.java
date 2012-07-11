package com.evanreidland.e.client.ent;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.client.GravityClient;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.graphics.ModelSceneObject;
import com.evanreidland.e.graphics.generate;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.shared.ent.Ship;

public class ClientShip extends Ship
{
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
		GravityClient.global.sendThrust(velThrust, angleThrust);
	}
	
	public ClientShip(long id)
	{
		super(id);
	}
}

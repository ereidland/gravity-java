package com.evanreidland.e.client.ent;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.graphics.SceneObject.AnchorType;
import com.evanreidland.e.graphics.generate;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.graphics.scene.ModelSceneObject;

public class ClientEnemy extends ClientEntity
{
	public void setupGraphics()
	{
		Model model = generate.Sphere(Vector3.Zero(),
				new Vector3(0.1, 0.1, 0.1), Vector3.Zero(), 8, 8);
		
		model.tex = engine.loadTexture("shiptest1.png");
		graphics.scene.addObject(new ModelSceneObject(model), this,
				AnchorType.POS);
		addRadarObject();
	}
	
	public ClientEnemy(long id)
	{
		super("enemy", id);
	}
}

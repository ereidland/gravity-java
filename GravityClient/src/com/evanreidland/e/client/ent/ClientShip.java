package com.evanreidland.e.client.ent;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.graphics.ModelSceneObject;
import com.evanreidland.e.graphics.SceneObject;
import com.evanreidland.e.graphics.SceneObject.AnchorType;
import com.evanreidland.e.graphics.generate;
import com.evanreidland.e.graphics.graphics;

public class ClientShip extends ClientEntity
{
	private class ClientShipSceneObject extends SceneObject
	{
		public void Render()
		{
			Vector3 spos = ClientShip.this.pos, sangle = ClientShip.this.angle;
			graphics.setTexture(null);
			graphics.drawLine(spos,
					spos.plus(sangle.getForward().multipliedBy(0.5)), 2, 1, 1,
					1, 1);
			
			graphics.drawLine(spos.plus(sangle.getRight().multipliedBy(-0.25)),
					spos.plus(sangle.getRight().multipliedBy(0.25)), 2, 1, 1,
					0, 1);
		}
	}
	
	public void setupGraphics()
	{
		Model model = generate.Sphere(Vector3.Zero(),
				new Vector3(0.1, 0.1, 0.1), Vector3.Zero(), 8, 8);
		
		model.tex = engine.loadTexture("sun1.png");
		graphics.scene.addObject(new ModelSceneObject(model), this);
		
		model.tex = engine.loadTexture("planet1.png");
		graphics.scene.addObject(new ClientShipSceneObject(), this,
				AnchorType.NONE);
		
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
	}
}

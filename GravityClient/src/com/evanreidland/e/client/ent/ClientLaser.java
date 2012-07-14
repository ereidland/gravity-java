package com.evanreidland.e.client.ent;

import com.evanreidland.e.engine;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.graphics.BillboardSceneObject;
import com.evanreidland.e.graphics.LongBillboardSceneObject;
import com.evanreidland.e.graphics.SceneObject.AnchorType;
import com.evanreidland.e.graphics.Sprite;
import com.evanreidland.e.graphics.graphics;

public class ClientLaser extends Entity
{
	public LongBillboardSceneObject billboard;
	
	public void onThink()
	{
		super.onThink();
		angle = vel.getAngle();
		billboard.v1.setAs(pos.plus(vel.getNormal().multipliedBy(-0.1)));
		billboard.v2.setAs(pos);
	}
	
	public void onDie()
	{
		Entity ent = ents.Create("explosion", new Double[]
		{
				0d, 0.5d, 0.5d, 1d, 0d
		});
		if (ent != null)
		{
			ent.pos.setAs(pos);
			ent.vel.setAs(vel);
			ent.mass = mass;
			ent.flags.set("enemy", flags.get("enemy"));
			ent.Spawn();
		}
	}
	
	public void onSpawn()
	{
		Sprite sprite = new Sprite(0.05, 0.05, engine.loadTexture("laser1.png"));
		sprite.cr = 0.5;
		sprite.cg = 1;
		sprite.cb = 0.5;
		sprite.ca = 1.0;
		billboard = new LongBillboardSceneObject(sprite, pos.cloned(),
				pos.plus(vel), 0.1, 0.4, 0.5, true);
		graphics.scene.addObject(billboard, this, AnchorType.POS);
		graphics.scene.addObject(new BillboardSceneObject(sprite, true), this,
				AnchorType.POS);
	}
	
	public ClientLaser(long id)
	{
		super("laser", id);
		mass = 0.000001;
		radius = 0.1;
	}
}
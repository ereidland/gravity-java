package com.evanreidland.e.client.ent;

import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.graphics.Sprite;

public class Planet extends Entity {
	public Sprite sprite;
	public void onRender() {
		sprite.width = radius*0.5f;
		sprite.height = radius*0.5f;
		sprite.pos.setAs(pos);
		sprite.renderBillboard();
		
		super.onRender();
	}
	
	public Planet(long id) {
		super("planet", id);
		bStatic = true;
		
		flags.setState("planet", true);
	}
}

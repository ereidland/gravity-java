package com.evanreidland.e.client.ent;

import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.graphics.Sprite;

public class Planet extends Entity {
	public Sprite sprite;
	public Model model;
	public void onRender() {
		if ( model != null ) {
			model.pos.setAs(pos);
			model.angle.setAs(angle);
			model.Render();
		}
		/*sprite.width = radius*0.5f;
		sprite.height = radius*0.5f;
		sprite.pos.setAs(pos);
		sprite.renderBillboard();
		*/
		super.onRender();
	}
	
	public Planet(long id) {
		super("planet", id);
		sprite = null;
		model = null;
		bStatic = true;
		flags.setState("planet", true);
	}
}

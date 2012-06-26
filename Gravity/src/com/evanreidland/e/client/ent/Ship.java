package com.evanreidland.e.client.ent;

import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.graphics.Model;

public class Ship extends Entity {
	public Model model;
	public void onThink() {
		super.onThink();
	}
	public void onRender() {
//		if ( model != null ) {
//			model.pos.setAs(pos);
//			model.angle.setAs(angle);
//			model.Render();
//		} else {
//		}
		super.onRender();
	}
	public Ship(long id) {
		super("ship", id);
		
		model = null;
		
		flags.setState("ship", true);
	}

}

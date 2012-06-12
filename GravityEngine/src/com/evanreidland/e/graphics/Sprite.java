package com.evanreidland.e.graphics;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;

public class Sprite {
	public Vector3 pos, angle;
	public float width, height;
	public Resource tex;
	
	public float cr, cg, cb, ca;
	
	public void renderBillboard() {
		float w2 = width*0.5f;
		float h2 = height*0.5f;
		
		Vertex a = new Vertex(), b = new Vertex(), c = new Vertex(), d = new Vertex();
		
		a.tx = 0; a.ty = 0;
		b.tx = 1; b.ty = 0;
		c.tx = 1; c.ty = 1;
		d.tx = 0; d.ty = 1;
		
		Vector3 newAngle = graphics.forward.getAngle().plus(angle);
		
		Vector3 up = newAngle.getUp(), right = newAngle.getRight();
		
		a.pos.setAs(pos.plus(up.multipliedBy(-h2).plus(right.multipliedBy(-w2))));
		b.pos.setAs(pos.plus(up.multipliedBy(-h2).plus(right.multipliedBy(w2))));
		c.pos.setAs(pos.plus(up.multipliedBy(h2).plus(right.multipliedBy(w2))));
		d.pos.setAs(pos.plus(up.multipliedBy(h2).plus(right.multipliedBy(-w2))));
		
		a.r = cr; a.g = cg; a.b = cb; a.a = ca;
		b.r = cr; b.g = cg; b.b = cb; b.a = ca;
		c.r = cr; c.g = cg; c.b = cb; c.a = ca;
		d.r = cr; d.g = cg; d.b = cb; d.a = ca;
		
		graphics.setTexture(tex);
		graphics.passQuad(a, b, c, d);
	}
	
	public void render2D() {
		float w2 = width*0.5f;
		float h2 = height*0.5f;
		
		Vertex a = new Vertex(), b = new Vertex(), c = new Vertex(), d = new Vertex();
		
		a.tx = 1; a.ty = 0;
		b.tx = 0; b.ty = 0;
		c.tx = 0; c.ty = 1;
		d.tx = 1; d.ty = 1;
		
		Vector3 right = new Vector3((float)Math.cos(angle.z), (float)Math.sin(angle.z), 0),
				up = new Vector3((float)Math.cos(angle.z + engine.Pi_2), (float)Math.sin(angle.z + engine.Pi_2), 0);
		
		a.pos.setAs(pos.plus(up.multipliedBy(h2).plus(right.multipliedBy(w2))));
		b.pos.setAs(pos.plus(up.multipliedBy(h2).plus(right.multipliedBy(-w2))));
		c.pos.setAs(pos.plus(up.multipliedBy(-h2).plus(right.multipliedBy(-w2))));
		d.pos.setAs(pos.plus(up.multipliedBy(-h2).plus(right.multipliedBy(w2))));
		
		a.r = cr; a.g = cg; a.b = cb; a.a = ca;
		b.r = cr; b.g = cg; b.b = cb; b.a = ca;
		c.r = cr; c.g = cg; c.b = cb; c.a = ca;
		d.r = cr; d.g = cg; d.b = cb; d.a = ca;
		
		graphics.setTexture(tex);
		graphics.passQuad(a, b, c, d);
	}
	
	public Sprite(float width, float height, Resource tex) {
		this.width = width;
		this.height = height;
		this.tex = tex;
		
		pos = Vector3.Zero();
		angle = Vector3.Zero();
		
		cr = cg = cb = ca = 1;
	}
}

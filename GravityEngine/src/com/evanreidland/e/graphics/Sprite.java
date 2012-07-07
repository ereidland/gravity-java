package com.evanreidland.e.graphics;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;

public class Sprite {
	public Vector3 pos, angle;
	public double width, height;
	public Resource tex;
	
	public double cr, cg, cb, ca;
	
	public void renderBillboard(boolean type) {
		Vertex a = new Vertex(), b = new Vertex(), c = new Vertex(), d = new Vertex();
		
		a.tx = 0; a.ty = 0;
		b.tx = 1; b.ty = 0;
		c.tx = 1; c.ty = 1;
		d.tx = 0; d.ty = 1;
		
		Vector3 newAngle = type ? graphics.camera.pos.minus(pos).getAngle() : graphics.forward.getAngle();
		
		newAngle.add(angle.multipliedBy(1, 0, 1));
		
		Vector3 up = newAngle.getUp().multipliedBy(width*0.5), right = newAngle.getRight().multipliedBy(width*0.5);
		
		
		a.pos.setAs(pos.plus(Vector3.Projected(right, up, angle.y + engine.Pi_2 + engine.Pi_4)));
		b.pos.setAs(pos.plus(Vector3.Projected(right, up, angle.y + engine.Pi_4)));
		c.pos.setAs(pos.plus(Vector3.Projected(right, up, angle.y - engine.Pi_4)));
		d.pos.setAs(pos.plus(Vector3.Projected(right, up, angle.y - engine.Pi_2 - engine.Pi_4)));
		
		a.r = cr; a.g = cg; a.b = cb; a.a = ca;
		b.r = cr; b.g = cg; b.b = cb; b.a = ca;
		c.r = cr; c.g = cg; c.b = cb; c.a = ca;
		d.r = cr; d.g = cg; d.b = cb; d.a = ca;
		
		graphics.setTexture(tex);
		graphics.passQuad(a, b, c, d);
	}
	
	public void renderLongBillboard(Vector3 v1, Vector3 v2, double height, double tx1, double tx2, boolean type) {
		Vector3 angle = v2.minus(v1).getAngle();
		Vector3 viewAngle = type ? graphics.camera.pos.minus(pos).getAngle() : graphics.forward.getAngle();
		
		Vector3 up = viewAngle.getUp().combined(angle.getUp()).multipliedBy(height*0.5);//.combined(angle.getRight()).multipliedBy(width*0.5);
		
		Vertex a = new Vertex(), b = new Vertex(), c = new Vertex(), d = new Vertex();
		
		a.tx = tx1; a.ty = 0;
		b.tx = tx2; b.ty = 0;
		c.tx = tx2; c.ty = 1;
		d.tx = tx1; d.ty = 1;
		
		
		a.pos.setAs(v1.plus(up.multipliedBy(0.5)));
		b.pos.setAs(v2.plus(up.multipliedBy(0.5)));
		c.pos.setAs(v2.plus(up.multipliedBy(-0.5)));
		d.pos.setAs(v1.plus(up.multipliedBy(-0.5)));
		
		a.r = cr; a.g = cg; a.b = cb; a.a = ca;
		b.r = cr; b.g = cg; b.b = cb; b.a = ca;
		c.r = cr; c.g = cg; c.b = cb; c.a = ca;
		d.r = cr; d.g = cg; d.b = cb; d.a = ca;
		
		graphics.setTexture(tex);
		graphics.passQuad(a, b, c, d);
	}
	
	public void render2D() {
		double w2 = width*0.5f;
		double h2 = height*0.5f;
		
		Vertex a = new Vertex(), b = new Vertex(), c = new Vertex(), d = new Vertex();
		
		a.tx = 1; a.ty = 0;
		b.tx = 0; b.ty = 0;
		c.tx = 0; c.ty = 1;
		d.tx = 1; d.ty = 1;
		
		Vector3 right = new Vector3((double)Math.cos(angle.y), (double)Math.sin(angle.y), 0),
				up = new Vector3((double)Math.cos(angle.y + engine.Pi_2), (double)Math.sin(angle.y + engine.Pi_2), 0);
		
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
	
	public Sprite(double width, double height, Resource tex) {
		this.width = width;
		this.height = height;
		this.tex = tex;
		
		pos = Vector3.Zero();
		angle = Vector3.Zero();
		
		cr = cg = cb = ca = 1;
	}
}

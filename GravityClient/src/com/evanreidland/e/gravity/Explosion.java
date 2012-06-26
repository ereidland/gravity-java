package com.evanreidland.e.gravity;

import com.evanreidland.e.Game;
import com.evanreidland.e.engine;
import com.evanreidland.e.roll;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.graphics.BillboardSceneObject;
import com.evanreidland.e.graphics.SceneObject;
import com.evanreidland.e.graphics.Sprite;
import com.evanreidland.e.graphics.graphics;

public class Explosion extends Entity {
	public double startSize, endSize;
	public double startAlpha, endAlpha;
	public double lifeTime, maxTime;
	public SceneObject object;
	public Sprite sprite;
	public void onThink() {
		super.onThink();
		lifeTime += Game.getDelta();
		if ( lifeTime >= maxTime ) {
			Kill();
		} else {
			radius = startSize + (endSize - startSize)*(lifeTime/maxTime);
			sprite.ca = startAlpha + (endAlpha - startAlpha)*(lifeTime/maxTime);
			sprite.width = radius*0.5;
			sprite.height = radius*0.5;
		}
	}
	
	public void Setup(Object[] args) {
		startSize = (Double)getArg(args, 0, 0d);
		endSize = (Double)getArg(args, 1, 1d);
		maxTime = (Double)getArg(args, 2, 1d);
		startAlpha = (Double)getArg(args, 3, 1d);
		endAlpha = (Double)getArg(args, 4, 0d);
	}
	
	public void onSpawn() {
		sprite = new Sprite(0, 0, engine.loadTexture("explosion1.png"));
		object = graphics.scene.addObject(
				new BillboardSceneObject(
						sprite,
						true),
				this,
				SceneObject.AnchorType.POS_ANGLE);
		
		sprite.cr = 1;
		sprite.cg = 0.8;
		sprite.cb = 0;
		
		angle.y = roll.randomDouble(0, engine.Pi2);
	}
	
	public Explosion(long id) {
		super("explosion", id);
		
		startSize = 0;
		endSize = 1;
		startAlpha = 1;
		endAlpha = 0;
		lifeTime = 0;
		maxTime = 1;
		
		object = null;
	}
}

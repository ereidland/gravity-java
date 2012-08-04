package com.evanreidland.e.client.graphics.scene;

import com.evanreidland.e.Game;
import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.graphics.SceneObject;
import com.evanreidland.e.graphics.Sprite;

public class PingSceneObject extends SceneObject
{
	public static Resource res = null;
	public double timeLeft, maxTime, maxScale;
	
	public void Render()
	{
		timeLeft -= Game.getDelta();
		if (timeLeft > 0)
		{
			double scale = (timeLeft / maxTime) * maxScale;
			Sprite sprite = new Sprite(scale, scale, res);
			sprite.pos.setAs(pos);
			sprite.angle.y = timeLeft;
			sprite.renderBillboard(true);
		}
		else
		{
			isDead = true;
		}
	}
	
	public PingSceneObject(Vector3 pos, double time, double scale)
	{
		zOrder = true;
		timeLeft = maxTime = time;
		maxScale = scale;
		this.pos.setAs(pos);
	}
}

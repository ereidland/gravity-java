package com.evanreidland.e.client.action;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.graphics.scene.BezierSceneObject;
import com.evanreidland.e.shared.action.EntityBezierMoveAction;

public class ClientBezierMoveAction extends EntityBezierMoveAction
{
	BezierSceneObject sceneObject;
	
	public boolean onStart()
	{
		boolean fail = super.onStart();
		if (!fail)
		{
			sceneObject = new BezierSceneObject(bezier, 0.05);
			graphics.scene.addObject(sceneObject);
		}
		return fail;
	}
	
	public void onEnd(boolean forced)
	{
		super.onEnd(forced);
		sceneObject.isDead = true;
	}
	
	public ClientBezierMoveAction()
	{
		super();
		sceneObject = null;
	}
	
	public ClientBezierMoveAction(Entity ent, Vector3 targetPos,
			Vector3 targetAngle)
	{
		super(ent, targetPos, targetAngle);
		sceneObject = null;
	}
}

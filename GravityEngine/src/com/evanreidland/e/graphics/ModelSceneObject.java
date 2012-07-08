package com.evanreidland.e.graphics;

public class ModelSceneObject extends SceneObject
{
	public Model model;

	public void Render()
	{
		model.pos.setAs(pos.plus(offset));
		model.angle.setAs(angle.plus(offset));
		model.Render();
	}

	public ModelSceneObject(Model model)
	{
		this.model = model;
	}

}

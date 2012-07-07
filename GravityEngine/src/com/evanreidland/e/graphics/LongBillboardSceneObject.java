package com.evanreidland.e.graphics;

import com.evanreidland.e.Vector3;

public class LongBillboardSceneObject extends SceneObject {
	public Sprite sprite;
	public Vector3 v1, v2;
	public double height, tx1, tx2;
	public boolean type;
	public void Render() {
		sprite.renderLongBillboard(v1, v2, height, tx1, tx2, type);
	}

	public LongBillboardSceneObject(Sprite sprite, Vector3 v1, Vector3 v2, double height, double tx1, double tx2, boolean type) {
		this.sprite = sprite;
		this.v1 = v1;
		this.v2 = v2;
		this.height = height;
		this.tx1 = tx1;
		this.tx2 = tx2;
		this.type = type;
	}
}

package com.evanreidland.e.graphics;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.graphics.Model.ModelType;
import com.evanreidland.e.phys.Rect3;

public class generate {
	private static ModelType type = ModelType.RenderList;
	public static ModelType getModelType() {
		return type;
	}
	public static void setModelType(ModelType type) {
		generate.type = type;
	}
	
	public static TriList CubeList(Vector3 offset, Vector3 size, Vector3 angle) {
		TriList triList = new TriList();
		
		Rect3 r = Rect3.fromPoints(size.multipliedBy(-0.5f), size.multipliedBy(0.5f));
		
		Quad q = new Quad();
		//Top
		q.vert[0].pos.setAs(r.topRight());
		q.vert[1].pos.setAs(r.topLeft());
		q.vert[2].pos.setAs(r.topBLeft());
		q.vert[3].pos.setAs(r.topBRight());
		triList.addQuad(q);
		
		//Bottom
		q.vert[0].pos.setAs(r.bottomURight());
		q.vert[1].pos.setAs(r.bottomULeft());
		q.vert[2].pos.setAs(r.bottomLeft());
		q.vert[3].pos.setAs(r.bottomRight());
		triList.addQuad(q);
		
		//Back
		q.vert[0].pos.setAs(r.topLeft());
		q.vert[1].pos.setAs(r.topRight());
		q.vert[2].pos.setAs(r.bottomURight());
		q.vert[3].pos.setAs(r.bottomULeft());
		triList.addQuad(q);
		
		//Front
		q.vert[0].pos.setAs(r.topBRight());
		q.vert[1].pos.setAs(r.topBLeft());
		q.vert[2].pos.setAs(r.bottomLeft());
		q.vert[3].pos.setAs(r.bottomRight());
		triList.addQuad(q);
		
		//Left
		q.vert[0].pos.setAs(r.topBLeft());
		q.vert[1].pos.setAs(r.topLeft());
		q.vert[2].pos.setAs(r.bottomULeft());
		q.vert[3].pos.setAs(r.bottomLeft());
		triList.addQuad(q);
		
		//Right
		q.vert[0].pos.setAs(r.topRight());
		q.vert[1].pos.setAs(r.topBRight());
		q.vert[2].pos.setAs(r.bottomRight());
		q.vert[3].pos.setAs(r.bottomURight());
		triList.addQuad(q);
		
		triList.Shift(offset);
		
		if ( angle.x != 0 || angle.y != 0 || angle.z != 0 ) {
			triList.Rotate(offset, angle);
		}
		
		return triList;
	}
	
	public static Model Cube(Vector3 offset, Vector3 size, Vector3 angle) {
		
		Model m = new Model();
		m.setData(CubeList(offset, size, angle), type);
		return m;
	}
	
	public static Model fromList(TriList list) {
		Model m = new Model();
		m.setData(list, type);
		return m;
	}
}

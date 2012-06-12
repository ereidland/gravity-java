package com.evanreidland.e.graphics;

public class unit {
	public static float getRatio(float curUnit, float maxUnit) {
		return maxUnit != 0 ? (curUnit/maxUnit) : 0;
	}
	
	public static float roundedTo100(float f) {
		return ((int)(f*100))/100f;
	}
	
	public static Quad generateQuad(float pointA, float pointB, float maxUnit, float numPerUnit) {
		Quad q = new Quad();
		q.vert[0].pos.setAs(-1, -1, 0);
		q.vert[1].pos.setAs( 1, -1, 0);
		q.vert[2].pos.setAs( 1,  1, 0);
		q.vert[3].pos.setAs(-1,  1, 0);
		
		q.vert[0].tx = q.vert[3].tx = getRatio(pointA, numPerUnit);
		q.vert[1].tx = q.vert[2].tx = getRatio(pointB, numPerUnit);
		
		q.vert[0].pos.x = q.vert[3].pos.x = pointA/maxUnit - 0.5f;
		q.vert[1].pos.x = q.vert[2].pos.x = pointB/maxUnit - 0.5f;
		
		q.vert[0].pos.y = q.vert[1].pos.y = 0.5f;
		q.vert[3].pos.y = q.vert[2].pos.y = -0.5f;
		
		return q;
	}
}

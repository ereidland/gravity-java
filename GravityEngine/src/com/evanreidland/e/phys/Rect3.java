package com.evanreidland.e.phys;

import com.evanreidland.e.Vector3;


public class Rect3 {
	public Vector3 a, b;
	public static Rect3 fromPoints(Vector3 ta, Vector3 tb) {
		Vector3 a = Vector3.Zero(), b = Vector3.Zero();
		a.x = Math.min(ta.x, tb.x);
		a.y = Math.min(ta.y, tb.y);
		a.z = Math.min(ta.z, tb.z);
		
		b.x = Math.max(ta.x, tb.x);
		b.y = Math.max(ta.y, tb.y);
		b.z = Math.max(ta.z, tb.z);
		return new Rect3(a, b);
	}
	public boolean Intersects(Rect3 other) {
		if ( Math.abs(a.x + b.x - other.a.x - other.b.x) < (b.x - a.x + other.b.x - other.a.x) )
			if ( Math.abs(a.y + b.y - other.a.y - other.b.y) < (b.y - a.y + other.b.y - other.a.y) )
				if ( Math.abs(a.z + b.z - other.a.z - other.b.z) < (b.z - a.z + other.b.z - other.a.z) )
					return true;
		return false;
	}
	public boolean containsPoint(Vector3 point) {
		return point.x >= a.x && point.y >= a.y && point.z >= a.z && point.x <= b.x && point.y <= b.y && point.z <= b.z;
	}
	
	
	public Vector3 topLeft() {
		return new Vector3(a.x, a.y, b.z);
	}
	
	public Vector3 topRight() {
		return new Vector3(b.x, a.y, b.z);
	}
	
	public Vector3 topBRight() {
		return new Vector3(b.x, b.y, b.z);
	}
	public Vector3 topBLeft() {
		return new Vector3(a.x, b.y, b.z);
	}
	
	public Vector3 bottomLeft() {
		return new Vector3(a.x, b.y, a.z);
	}
	
	public Vector3 bottomULeft() {
		return new Vector3(a.x, a.y, a.z);
	}
	
	public Vector3 bottomURight() {
		return new Vector3(b.x, a.y, a.z);
	}
	
	public Vector3 bottomRight() {
		return new Vector3(b.x, b.y, a.z);
	}
	
	public Vector3 getCenter() {
		return a.plus(b).multipliedBy(0.5f);
	}
	
	public Rect3 Shift(Vector3 howMuch) {
		a.add(howMuch);
		b.add(howMuch);
		return this;
	}
	
	public Rect3 Shifted(Vector3 howMuch) {
		return new Rect3(a, b).Shift(howMuch);
	}
	
	public void moveTo(Vector3 newPos) {
		Shift(newPos.minus(getCenter()));
	}
	
	
	
	public double getWidth() {
		return Math.abs(b.x - a.x);
	}
	
	public double getHeight() {
		return Math.abs(b.y - a.y);
	}
	
	public double getDepth() {
		return Math.abs(b.z - a.z);
	}
	
	public Rect3(Vector3 a, Vector3 b) {
		this.a = a.cloned();
		this.b = b.cloned();
	}
}

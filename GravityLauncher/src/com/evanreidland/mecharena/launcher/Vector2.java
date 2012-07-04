package com.evanreidland.mecharena.launcher;

public class Vector2 {
	public float x, y;
	
	public static Vector2 Zero() {
		return new Vector2();
	}
	
	public static Vector2 RandomNormal() {
		return new Vector2((float)(Math.random() - 0.5f)*2, (float)(Math.random() - 0.5f)*2).Normalize();
	}
	public static Vector2 Random() {
		return new Vector2((float)Math.random(), (float)Math.random());
	}
	
	public static Vector2 fromAngle(float angle) {
		return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
	}
	
	public void setAngle(float newAngle) {
		setAs(Vector2.fromAngle(newAngle).multipliedBy(getLength()));
	}
	
	public Vector2 Rotate(Vector2 origin, float howmuch) {
		return setAs(origin.plus(origin.minus(this).Rotate(howmuch)));
	}
	
	public Vector2 Rotate(float howmuch) {
		setAngle(getAngle() + howmuch);
		return this;
	}
	
	public Vector2 setAs(Vector2 other) {
		x = other.x;
		y = other.y;
		return this;
	}
	
	public void setAs(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2 add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	public Vector2 plus(float x, float y) {
		return cloned().add(x, y);
	}
	public Vector2 minus(float x, float y) {
		return cloned().subtract(x, y);
	}
	
	public Vector2 subtract(float x, float y) {
		return add(-x, -y);
	}
	
	public Vector2 multiply(float x, float y) {
		this.x *= x;
		this.y *= y;
		return this;
	}
	
	public Vector2 divide(float x, float y) {
		if ( x != 0 ) {
			this.x /= x;
		} else this.x = 0;
		if ( y != 0 ) {
			this.y /= y;
		} else this.y = 0;
		return this;
	}
	public Vector2 multiply(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}
	public Vector2 divide(float scalar) {
		if ( scalar != 0 ) {
			x /= scalar;
			y /= scalar;
			x = y = 0;
		}
		return this;
	}
	public Vector2 multipliedBy(float scalar) {
		return cloned().multiply(scalar);
	}
	public Vector2 dividedBy(float scalar) {
		return cloned().divide(scalar);
	}
	
	public Vector2 multipliedBy(float x, float y) {
		return cloned().multiply(x, y);
	}
	public Vector2 dividedBy(float x, float y) {
		return cloned().divide(x, y);
	}
	
	public Vector2 add(Vector2 other) {
		return add(other.x, other.y);
	}
	public Vector2 subtract(Vector2 other) {
		return subtract(other.x, other.y);
	}
	public Vector2 multiply(Vector2 other) {
		return multiply(other.x, other.y);
	}
	public Vector2 divide(Vector2 other) {
		return divide(other.x, other.y);
	}
	public Vector2 plus(Vector2 other) {
		return plus(other.x, other.y);
	}
	public Vector2 minus(Vector2 other) {
		return minus(other.x, other.y);
	}
	public Vector2 multipliedBy(Vector2 other) {
		return multipliedBy(other.x, other.y);
	}
	public Vector2 dividedBy(Vector2 other) {
		return dividedBy(other.x, other.y);
	}
	
	public float getDistance(Vector2 other) {
		return getDistance(other.x, other.y);
	}
	
	public Vector2 rounded2() {
		return new Vector2(((int)x/2)*2, ((int)y/2)*2); 
	}
	
	public float getLength() {
		return (float) Math.sqrt(x*x + y*y);
	}
	public float getDistance(float x, float y) {
		return new Vector2(x - this.x, y - this.y).getLength();
	}
	public Vector2 getNormal() {
		float len = getLength();
		return len != 0 ? multipliedBy(1/len) : Vector2.Zero();
	}
	
	public Vector2 Normalize() {
		float len = getLength();
		return len != 0 ? multiply(1/len) : Vector2.Zero();
	}
	
	public float getAngle() {
		return (float)Math.atan2(y, x);
	}
	
	public float dotProduct() {
		return x*x + y*y;
	}
	
	public Vector2 Abs() {
		x = Math.abs(x);
		y = Math.abs(y);
		return this;
	}
	
	public Vector2 getAbs() {
		return cloned().Abs();
	}
	
	public float[] toArray() {
		return new float[] { x, y };
	}
	
	public boolean gr(Vector2 other) {
		return x > other.x && y > other.y;
	}
	
	public boolean lt(Vector2 other) {
		return x < other.x && y < other.y;
	}
	
	public boolean gre(Vector2 other) {
		return x >= other.x && y >= other.y;
	}
	
	public boolean lte(Vector2 other) {
		return x <= other.x && y <= other.y;
	}
	
	public Vector2() {
		x = y = 0;
	}
	public Vector2(Vector2 other) {
		this(other.x, other.y);
	}
	public Vector2 cloned() {
		return new Vector2(x, y);
	}
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
}

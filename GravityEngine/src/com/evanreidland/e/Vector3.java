package com.evanreidland.e;

public class Vector3 {
	public float x, y, z;
	
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
	public String toRoundedString() {
		return String.format("(%3.02f, %3.02f, %3.02f)", x, y, z);
	}
	
	public static Vector3 Zero() {
		return new Vector3();
	}
	
	public static Vector3 New(float x, float y, float z ) {
		return new Vector3(x, y, z);
	}
	
	public static Vector3 fromAngle2d(float f) {
		return new Vector3((float)Math.cos(f), (float)Math.sin(f), 0);
	}
	
	public static Vector3 fromAngle(Vector3 angle) {
		return angle.getForward();
	}
	
	public static Vector3 Projected(Vector3 right, Vector3 up, float x, float y) {
		return right.multipliedBy(x).plus(up.multipliedBy(y));
	}
	
	public static Vector3 Projected(Vector3 right, Vector3 up, float angle) {
		return Projected(right, up, (float)Math.cos(angle), (float)Math.sin(angle));
	}
	
	public static Vector3 normalFromPoints(Vector3 a, Vector3 b, Vector3 c) {
		Vector3 u = b.minus(a);
		Vector3 v = c.minus(a);
		
		return new Vector3(
				u.y*v.z - u.z*v.y,
				u.z*v.x - u.x*v.z,
				u.x*v.y - u.y*v.x);
	}
	
	public static Vector3 reflectNormal(Vector3 in, Vector3 surface) {
		return in.minus(surface.multipliedBy(in.dotProduct(surface)).multipliedBy(2)).Normalize();
	}
	
	public static Vector3 RandomNormal() {
		return new Vector3((float)(Math.random() - 0.5f)*2, (float)(Math.random() - 0.5f)*2, (float)(Math.random() - 0.5f)*2).Normalize();
	}
	public static Vector3 Random() {
		return new Vector3((float)Math.random(), (float)Math.random(), (float)Math.random());
	}
	
	public Vector3 setAs(Vector3 other) {
		x = other.x;
		y = other.y;
		z = other.z;
		return this;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setZ(float z) {
		this.z = z;
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	
	public Vector3 setAs(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vector3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	public Vector3 plus(float x, float y, float z) {
		return cloned().add(x, y, z);
	}
	public Vector3 minus(float x, float y, float z) {
		return cloned().subtract(x, y, z);
	}
	
	public Vector3 subtract(float x, float y, float z) {
		return add(-x, -y, -z);
	}
	
	public Vector3 multiply(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}
	
	public Vector3 divide(float x, float y, float z) {
		if ( x != 0 ) {
			this.x /= x;
		} else this.x = 0;
		if ( y != 0 ) {
			this.y /= y;
		} else this.y = 0;
		if ( z != 0 ) {
			this.z /= z;
		} else this.z = 0;
		return this;
	}
	public Vector3 multiply(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		return this;
	}
	public Vector3 divide(float scalar) {
		if ( scalar != 0 ) {
			x /= scalar;
			y /= scalar;
			z /= scalar;
		} else {
			x = y = z = 0;
		}
		return this;
	}
	public Vector3 multipliedBy(float scalar) {
		return cloned().multiply(scalar);
	}
	public Vector3 dividedBy(float scalar) {
		return cloned().divide(scalar);
	}
	
	public Vector3 multipliedBy(float x, float y, float z) {
		return cloned().multiply(x, y, z);
	}
	public Vector3 dividedBy(float x, float y, float z) {
		return cloned().divide(x, y, z);
	}
	
	public Vector3 add(Vector3 other) {
		return add(other.x, other.y, other.z);
	}
	public Vector3 subtract(Vector3 other) {
		return subtract(other.x, other.y, other.z);
	}
	public Vector3 multiply(Vector3 other) {
		return multiply(other.x, other.y, other.z);
	}
	public Vector3 divide(Vector3 other) {
		return divide(other.x, other.y, other.z);
	}
	public Vector3 plus(Vector3 other) {
		return plus(other.x, other.y, other.z);
	}
	public Vector3 minus(Vector3 other) {
		return minus(other.x, other.y, other.z);
	}
	public Vector3 multipliedBy(Vector3 other) {
		return multipliedBy(other.x, other.y, other.z);
	}
	public Vector3 dividedBy(Vector3 other) {
		return dividedBy(other.x, other.y, other.z);
	}
	
	public float getDistance2D(Vector3 other) {
		return getDistance2D(other.x, other.y);
	}
	public float getDistance(Vector3 other) {
		return getDistance(other.x, other.y, other.z);
	}
	
	public Vector3 Reduce(float howMuch) {
		Vector3 sub = multipliedBy(-howMuch);
		float len = getLength();
		if ( sub.getLength() > len ) {
			return setAs(0, 0, 0);
			//sub.Normalize().multiply(len);
		}
		return add(sub);
	}
	
	public Vector3 reducedBy(float howMuch) {
		return cloned().Reduce(howMuch);
	}
	
	public Vector3 clipAngle() {
		double pi2 = Math.PI * 2;
		if ( x > pi2) { 
			x -= pi2;
		} else if ( x < 0 ) {
			x += pi2;
		}
		
		if ( y > pi2) { 
			y -= pi2;
		} else if ( y < 0 ) {
			y += pi2;
		}
		
		if ( z > pi2) { 
			z -= pi2;
		} else if ( z < 0 ) {
			z += pi2;
		}
		return this;
	}
	
	public Vector3 clipAngle(boolean recursive) {
		if ( recursive ) {
			double pi2 = Math.PI * 2;
			while ( x > pi2) { 
				x -= pi2;
			}
			while ( x < 0 ) {
				x += pi2;
			}
			
			while ( y > pi2) { 
				y -= pi2;
			}
			while ( y < 0 ) {
				y += pi2;
			}
			
			while ( z > pi2) { 
				z -= pi2;
			}
			while ( z < 0 ) {
				z += pi2;
			}
		} else {
			clipAngle();
		}
		
		return this;
	}
	
	public Vector3 clipMin(float minX, float minY, float minZ) {
		if ( x < minX ) x = minX;
		if ( y < minY ) y = minY;
		if ( z < minZ ) z = minZ;
		return this;
	}
	
	public Vector3 clipMin(Vector3 min) {
		return clipMin(min.x, min.y, min.z);
	}
	
	
	
	public Vector3 clipMax(float maxX, float maxY, float maxZ) {
		if ( x > maxX ) x = maxX;
		if ( y > maxY ) y = maxY;
		if ( z > maxZ ) z = maxZ;
		return this;
	}
	
	public Vector3 clipMax(Vector3 max) {
		return clipMin(max.x, max.y, max.z);
	}
	
	public boolean matchesSign(Vector3 other) {
		return Math.signum(x) == Math.signum(other.x)
			&& Math.signum(y) == Math.signum(other.y)
			&& Math.signum(z) == Math.signum(other.z); 
	}
	
	public float getLength2d() {
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public float getLength() {
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	public float getRoughLength() {
		return Math.abs(x) + Math.abs(y) + Math.abs(z);
	}
	
	public float getDistance2D(float x, float y) {
		return new Vector3(x - this.x, y - this.y, 0).getLength2d();
	}
	public float getDistance(float x, float y, float z) {
		return new Vector3(x - this.x, y - this.y, z - this.z).getLength();
	}
	public Vector3 getNormal() {
		float len = getLength();
		return len != 0 ? multipliedBy(1/len) : Vector3.Zero();
	}
	
	public Vector3 Normalize() {
		float len = getLength();
		return len != 0 ? multiply(1/len) : Vector3.Zero();
	}
	
	public float dotProduct() {
		return x*x + y*y + z*z;
	}
	
	public float dotProduct(Vector3 other) {
		return x*other.x + y*other.y + z*other.z;
	}
	
	public float dotProduct2D() {
		return x*x + y*y;
	}
	
	public Vector3 Round(int decimals) {
		float place = (int)Math.pow(10, decimals);
		x = Math.round(x*place)/place;
		y = Math.round(y*place)/place;
		z = Math.round(z*place)/place;
		return this;
	}
	
	public Vector3 Rounded(int decimals) {
		return cloned().Round(decimals);
	}
	
	public Vector3 Abs() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		return this;
	}
	
	public Vector3 getAbs() {
		return cloned().Abs();
	}
	
	public float[] toArray() {
		return new float[] { x, y, z};
	}
	
	
	//Begin angle functions
	public float getAngle2d() {
		return (float)Math.atan2(y, x);
	}
	
	/*public Vector3 getAngle() {	
		return new Vector3(
				-(float)Math.atan2(Math.sqrt(x*x + y*y), z) + (float)Math.PI,
				0,
				(float)Math.atan2(y, x) - (float)Math.PI/2f);
	}*/
	
	/*public Vector3 getAngle() {	
		float len = getLength();
		return new Vector3(
				(float)Math.atan2(z, len),
				0,
				(float)Math.atan2(x, y));
	}*/
	
	public Vector3 getAngle() {
		float len = getLength2d();
		return new Vector3(
				len != 0 ? (float)(Math.atan(z/len) + engine.Pi_2) : 0,
				0,
				(float)Math.atan2(y, x) - engine.Pi_2);
	}
	
	public Vector3 Rotate2d(float howMuch) {
		return setAs(Vector3.fromAngle2d(getAngle2d()).multiply(getLength()));
	}
	
	public Vector3 Rotate(Vector3 howMuch) {
		
		Vector3 cur = cloned();
		
		if ( howMuch.z != 0 ) {
			cur.setAs(cur.getAngle().plus(new Vector3(0, 0, howMuch.z)).getForward());
		}
		
		cur.setAs(cur.toFormat(VectorFormat.YZX));
		if ( howMuch.x != 0 ) {
			cur.setAs(cur.getAngle().plus(new Vector3(0, 0, howMuch.x)).getForward());
		}
		
		cur.setAs(cur.toXYZ(VectorFormat.YZX).toFormat(VectorFormat.XZY));
		if ( howMuch.y != 0 ) {
			cur.setAs(cur.getAngle().plus(new Vector3(0, 0, howMuch.y)).getForward());
		}
		cur.setAs(cur.toXYZ(VectorFormat.XZY));
		cur.multiply(getLength());
		
		return setAs(cur);
	}
	
	public static enum VectorFormat {
		YZX,
		XZY,
	}
	public Vector3 toXYZ(VectorFormat from) {
		Vector3 v = cloned();
		switch(from) {
			case YZX:
				v.x = z;
				v.y = x;
				v.z = y;
				break;
			case XZY:
				v.x = x;
				v.y = z;
				v.z = y;
				break;
		}
		
		return v;
	}
	
	public Vector3 toFormat(VectorFormat newForm) {
		Vector3 v = cloned();
		switch(newForm) {
			case YZX:
				v.x = y;
				v.y = z;
				v.z = x;
				break;
			case XZY:
				v.x = x;
				v.y = z;
				v.z = y;
				break;
		}
		return v;
	}
	
	public Vector3 Rotate(Vector3 howMuch, Vector3 origin) {
		return setAs(origin.plus(this.minus(origin).Rotate(howMuch)));
	}
	
	public Vector3 getRotated2d(float howMuch) {
		return cloned().Rotate2d(howMuch);
	}
	public Vector3 getRotated(Vector3 howMuch) {
		return cloned().Rotate(howMuch);
	}
	public Vector3 getRotated(Vector3 howMuch, Vector3 origin) {
		return cloned().Rotate(howMuch, origin);
	}
	/*public Vector3 getForward() {
		return new Vector3(
		-(float)(Math.sin(-z) * Math.sin(-x)),
		-(float)(Math.cos(-z) * Math.sin(-x)),
		-(float)(Math.cos(-x)));
	}
	*/
	public Vector3 getForward() {
		return new Vector3(
		(float)(Math.cos(z + engine.Pi_2) * Math.sin(x)),
		(float)(Math.sin(z + engine.Pi_2) * Math.sin(x)),
		(float)(-Math.cos(x)));
	}
	
	public Vector3 getForwardXY() {
		return new Vector3(
				(float)(Math.cos(z + engine.Pi_2)),
				(float)(Math.sin(z + engine.Pi_2)), 0);
	}
	
	public Vector3 getRight() {
		return new Vector3(
				(float)(Math.cos(z)),
				(float)(Math.sin(z)), 0);
	}
	
	public Vector3 getUp() {
		float anglex = x + engine.Pi_2;
		return new Vector3(
				(float)(Math.cos(z + engine.Pi_2) * Math.sin(anglex)),
				(float)(Math.sin(z + engine.Pi_2) * Math.sin(anglex)),
				(float)(-Math.cos(anglex)));
	}

	
	//End angle functions.
	
	//Begin comparison functions.
	
	public boolean lt(Vector3 other) {
		return x < other.x && y < other.y && z < other.z;
	}
	public boolean gr(Vector3 other) {
		return x > other.x && y > other.y && z > other.z;
	}
	public boolean lte(Vector3 other) {
		return x <= other.x && y <= other.y && z <= other.z;
	}
	public boolean gre(Vector3 other) {
		return x >= other.x && y >= other.y && z >= other.z;
	}
	
	//End comparison functions.
	
	public Vector3() {
		x = y = z = 0;
	}
	public Vector3(Vector3 other) {
		this(other.x, other.y, other.z);
	}
	public Vector3 cloned() {
		return new Vector3(this);
	}
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}

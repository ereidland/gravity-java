package com.evanreidland.e.client.graphics;

public class BlockType {
	private int id;
	private int tx, ty, fx, fy, bkx, bky, lx, ly, rx, ry, bx, by; 
	public int getID() {
		return id;
	}
	
	public int getTX() {
		return tx;
	}
	public int getTY() {
		return ty;
	}
	
	public int getFX() {
		return fx;
	}
	public int getFY() {
		return fy;
	}
	
	public int getBkX() {
		return bkx;
	}
	public int getBkY() {
		return bky;
	}
	
	public int getLX() {
		return lx;
	}
	public int getLY() {
		return ly;
	}
	
	public int getRX() {
		return rx;
	}
	public int getRY() {
		return ry;
	}
	
	public int getBX() {
		return bx;
	}
	public int getBY() {
		return by;
	}
	
	public BlockType(int id, int tx, int ty, int fx, int fy, int bkx, int bky, int lx, int ly, int rx, int ry, int bx, int by) {
		this.id = id;
		this.tx = tx;
		this.ty = ty;
		this.fx = fx;
		this.fy = fy;
		this.bkx = bkx;
		this.bky = bky;
		this.lx = lx;
		this.ly = ly;
		this.rx = rx;
		this.ry = ry;
		this.bx = bx;
		this.by = by;
	}
	
	public BlockType(int id, int x, int y) {
		this(id, x, y, x, y, x, y, x, y, x, y, x, y);
	}
	public BlockType(int id, int tx, int ty, int sx, int sy, int bx, int by) {
		this(id, tx, ty, sx, sy, sx, sy, sx, sy, sx, sy, bx, by);
	}
}

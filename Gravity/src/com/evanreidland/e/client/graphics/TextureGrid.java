package com.evanreidland.e.client.graphics;

public class TextureGrid {
	private float width, height;
	
	public float x(float u) {
		return u/width;
	}
	public float y(float v) {
		return v/height;
	}
	
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	
	public TextureGrid(int width, int height) {
		this.width = width;
		this.height = height;
	}
}

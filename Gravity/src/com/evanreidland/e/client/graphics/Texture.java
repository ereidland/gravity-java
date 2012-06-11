package com.evanreidland.e.client.graphics;

import org.lwjgl.opengl.GL11;

public class Texture {
    private int target; 
    private int textureID;
    private int height;
    private int width;
    private int texWidth;
    private int texHeight;
    private float widthRatio;
    private float heightRatio;
    
    public String urlString = "";
    
    public static int minFilter = GL11.GL_LINEAR;
    public static int magFilter = GL11.GL_LINEAR;
    
    public static void unbind() {
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
    
    public int getID() {
    	return textureID;
    }
    
    public Texture(int target, int textureID) {
        this.target = target;
        this.textureID = textureID;
    }
    public void bind() {
    	GL11.glBindTexture(target, textureID);
    }
    
    public void setHeight(int height) {
        this.height = height;
        setHeight();
    }
    public void setWidth(int width) {
        this.width = width;
        setWidth();
    }
    public int getImageHeight() {
        return height;
    }
    public int getImageWidth() {
        return width;
    }
    public float getHeight() {
        return heightRatio;
    }
    public float getWidth() {
        return widthRatio;
    }
    
    public float x(float r) {
    	return r*widthRatio;
    }
    
    public float y(float r) {
    	return r*heightRatio;
    }
    public void setTextureHeight(int texHeight) {
        this.texHeight = texHeight;
        setHeight();
    }
    public void setTextureWidth(int texWidth) {
        this.texWidth = texWidth;
        setWidth();
    }
    private void setHeight() {
        if (texHeight != 0) {
            heightRatio = ((float) height)/texHeight;
        }
    }
    private void setWidth() {
        if (texWidth != 0) {
            widthRatio = ((float) width)/texWidth;
        }
    }
}

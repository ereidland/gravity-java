package com.evanreidland.e.graphics;

import com.evanreidland.e.Resource;
import com.evanreidland.e.ResourceType;

//TODO finish.
public class FontGraphic extends Resource {
	private Resource fontTexture;
	
	public Resource getTexture() {
		return fontTexture;
	}
	
	private float[] widths;
	private float texSize, padding;
	
	public float getPadding() {
		return padding;
	}
	
	public float getTextureRatio() {
		return texSize;
	}
	
	public float tx(char ch) {
		return ((ch % 16)/16f)*texSize;
	}
	public float ty(char ch) {
		return ((ch / 16)/16f)*texSize;
	}
	
	public float interval() {
		return texSize*(0.0625f);
	}
	
	public float interval(char ch) {
		return interval()*getCharWidth(ch);
	}
	
	public float getCharWidth(char ch) {
		return (ch >= 0 && ch < 256 ? widths[ch] : 0);
	}
	
	public float[] getWidths() {
		return widths;
	}
	
	public float getStringWidth(String str, float size) {
		float len = 0;
		for ( int i = 0; i < str.length(); i++ ) {
			len += getCharWidth(str.charAt(i))*size*(i == 0 || i == str.length() - 1 ? 0.5f : 1);
		}
		return len;
	}
	
	public Quad buildQuad(char ch) {
		Quad q = new Quad();
		float xsize = getCharWidth(ch);
		q.vert[0].pos.setAs(-xsize*0.5f,  0.5f, 0);
		q.vert[1].pos.setAs( xsize*0.5f,  0.5f, 0);
		q.vert[2].pos.setAs( xsize*0.5f, -0.5f, 0);
		q.vert[3].pos.setAs(-xsize*0.5f, -0.5f, 0);
		
		float tx = tx(ch),
			  ty = ty(ch);
		
		float xinterval = interval(ch);
		float yinterval = interval();
		
		q.vert[0].tx = tx; 				q.vert[0].ty = ty;
		q.vert[1].tx = tx + xinterval; 	q.vert[1].ty = ty;
		q.vert[2].tx = tx + xinterval; 	q.vert[2].ty = ty + yinterval;
		q.vert[3].tx = tx; 				q.vert[3].ty = ty + yinterval;
		
		return q;
	}
	
	public FontGraphic() {
		super(ResourceType.Font, null, false);
		fontTexture = null;
		widths = new float[256];
		texSize = padding = 0;
	}
	
	
	
	public FontGraphic(Resource fontTexture, float[] widths, float texSize, float padding) {
		super(ResourceType.Font, fontTexture, true);
		this.fontTexture = fontTexture;
		this.widths = widths;
		this.texSize = texSize;
		this.padding = padding;
		
		float imageSize = fontTexture.info.get("width").toFloat();
		if ( imageSize != 0 ) {
			this.texSize /= imageSize;
			this.padding /= imageSize;
		}
	}
}

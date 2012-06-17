package com.evanreidland.e.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import com.evanreidland.e.Resource;
import com.evanreidland.e.ResourceType;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;

public class font {
    public static final byte version = 1;
    
    public static boolean buildFont(String fontName, int baseSize, boolean bold, boolean antiAlias) {
    	try {
    		Font font = new Font(fontName, bold ? Font.BOLD : Font.PLAIN, baseSize);
    		int newSize = 128;
    		int padding = baseSize/5;
    		
    		while ( newSize < (baseSize + padding)*16) {
    			newSize *= 2;
    		}
    		BufferedImage fontImage = new BufferedImage(newSize, newSize, BufferedImage.TYPE_4BYTE_ABGR);
    		
    		Graphics2D tg = fontImage.createGraphics();
				tg.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
				tg.fillRect(0, 0, newSize, newSize);
			tg.dispose();
			
            Graphics2D g = (Graphics2D)fontImage.getGraphics();
            g.setFont(font);
    		FontMetrics fontMetrics = fontImage.getGraphics().getFontMetrics(font);
    		
    		String fname = engine.getPath() + "sprites/font/" + fontName + "x" + baseSize;
    		
    		DataOutputStream str = new DataOutputStream(new FileOutputStream(fname + ".efont"));
    		int[] widths = new int[256];
    		for ( int i = 0; i < widths.length; i++ ) {
    			widths[i] = fontMetrics.charWidth(i);
    			//System.out.println(widths[i]);
    		}
    		str.writeByte(version);
    		str.writeByte(bold ? 1 : 0);
    		str.writeByte(antiAlias ? 1 : 0);
    		str.writeByte(baseSize);
    		str.writeByte(padding);
    		str.writeShort((baseSize + padding)*16);
    		for ( int i = 0; i < widths.length; i++ ) {
    			str.writeByte(widths[i]);
    		}
    		str.close();
			
			g.setColor(Color.white);
			
			fontMetrics = g.getFontMetrics();
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
    		for ( int i = 0; i < 256; i++ ) {
    			int x = (i % 16)*(baseSize + padding);
    			int y = (i / 16)*(baseSize + padding);
    			
    			String ch = new String(new char[] {(char)i});
    			g.drawString(ch, x, y + fontMetrics.getAscent());
    		}
    		ImageIO.write(fontImage, "png", new File(fname + ".png"));
    	} catch ( Exception e ) {
    		e.printStackTrace();
    		return false;
    	}
    	
    	return true;
    }
    
    public static void Render(Resource res, String str, Vector3 pos, Vector3 right, Vector3 up, double size, boolean bCenter) {
    	if ( res.getType() == ResourceType.Font ) {
    		graphics.setTexture((Resource)((FontGraphic)res).getObject());
    	}
    	graphics.passTriangleList(renderToTriList(res, str, pos, right, up, size, bCenter));
    }
    
    public static TriList renderToTriList(Resource res, String str, Vector3 pos, Vector3 right, Vector3 up, double size, boolean bCenter) {
    	TriList list = new TriList();
    	if ( res.getType() == ResourceType.Font ) {
    		FontGraphic font = (FontGraphic)res;
    		
    		double curLen = 0;
    		
    		Vector3 basePos = bCenter ? pos.minus(right.multipliedBy(font.getStringWidth(str, size)*0.5f)) : pos.cloned();
    		
    		for ( int i = 0; i < str.length(); i++ ) {
    			char ch = str.charAt(i);
    			Quad q = font.buildQuad(ch);
    			double chWidth = font.getCharWidth(ch)*size;
    			q.applyToProjection(basePos.plus(right.multipliedBy(curLen + chWidth*0.5f)), right.multipliedBy(size), up.multipliedBy(size));
    			list.addQuad(q);
    			curLen += chWidth;
    		}
    	}
    	return list;
    }
    
    public static void Render3d(Resource res, String str, Vector3 pos, double size, boolean bCenter) {
    	Render(res, str, pos, graphics.right, graphics.up, size, bCenter);
    }
    
    public static void Render2d(Resource res, String str, Vector3 pos, double size, boolean bCenter) {
    	Render(res, str, pos, new Vector3(1, 0, 0), new Vector3(0, 1, 0), size, bCenter);
    }
}

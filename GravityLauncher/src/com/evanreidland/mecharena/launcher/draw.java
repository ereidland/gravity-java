package com.evanreidland.mecharena.launcher;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class draw {
	private static Graphics2D graphics = null;
	public static int x = 0, y = 0, width = 0, height = 0;
	private static Color color = Color.white;
	private static int lineWidth;
	public static Font font = new Font("Courier New Monospace", Font.BOLD, 16);
	
	public static void setPos(Vector2 pos) {
		x = (int)pos.x;
		y = (int)pos.y;
	}
	
	public static void setSize(Vector2 size) {
		width = (int)size.x;
		height = (int)size.y;
	}
	
	public static int getLineWidth() {
		return lineWidth;
	}
	
	public static Graphics2D getGraphics() {
		return graphics;
	}
	
	public static void setGraphics(Graphics2D graphics) {
		draw.graphics = graphics;
		graphics.setStroke(new BasicStroke(lineWidth));
		graphics.setColor(color);
		
		//graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		
		graphics.addRenderingHints( new RenderingHints( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON ));
	}
	
	public static void setLineWidth(int newWidth) {
		lineWidth = newWidth;
		graphics.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
	}
	
	public static Color getColor() {
		return draw.color;
	}
	public static void setColor(Color color) {
		draw.color = color;
		draw.graphics.setColor(color);
	}
	
	public static void setSize(int width, int height) {
		draw.width = width;
		draw.height = height;
	}
	public static void setPos(int x, int y) {
		draw.x = x;
		draw.y = y;
	}
	public static void Circle() {
		graphics.drawOval(draw.x - width/2, draw.y - height/2, width, height);
	}
	public static void solidCircle() {
		graphics.fillOval(draw.x - width/2, draw.y - height/2, width, height);
	}
	public static void Box() {
		draw.graphics.fillRect(x, y, width, height);
	}
	public static void Text(String text) {
		draw.graphics.setFont(draw.font);
		draw.graphics.drawString(text, x - getTextWidth(text)/2, y - graphics.getFont().getSize()/2);
	}
	public static int getTextWidth(String text) {
		return text.length() > 0 ? (int)draw.graphics.getFont().getStringBounds(text, draw.graphics.getFontRenderContext()).getWidth()
								 : 0;
	}
	public static void Line(int targX, int targY) {
		draw.graphics.drawLine(x, y, targX, targY);
	}
	
	public static void Line(Vector2 p) {
		draw.graphics.drawLine(x, y, (int)p.x, (int)p.y);
	}
	
	public static void Image(BufferedImage image) {
		draw.graphics.drawImage(image, (int)(draw.x - draw.width*0.5f), (int)(draw.y - draw.height*0.5f), draw.width, draw.height, null);
	}
	
	public static BufferedImage loadImage(String path) {
		try {
			BufferedImage b = ImageIO.read(new File(path));
			BufferedImage surf = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			
			Graphics2D g = surf.createGraphics();
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
			g.fillRect(0, 0, b.getWidth(), b.getHeight());
			g.drawImage(b, 0, 0, b.getWidth(), b.getHeight(), null);
			g.dispose();
			return b;
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
	}
}

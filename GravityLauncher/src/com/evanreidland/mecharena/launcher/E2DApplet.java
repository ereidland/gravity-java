 package com.evanreidland.mecharena.launcher;


import java.applet.Applet;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.*; 
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;


public abstract class E2DApplet extends Applet
implements Runnable, KeyListener, MouseListener, MouseMotionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6943999207698127211L;

	public int xmouse, ymouse;
	private long curTime;
	private float delta;
	private char[] keys;
	private Vector<EventKey> events;
	private Thread thread;
	public Image screen;
	public Graphics2D screenGraphics;
	public Color clearColor;
	//private boolean running = false; 
	
	public static boolean fitWindow = true;
	
	public static long sleepTime = 5;
	
	public static boolean bRunning = true;
	
	public static int scalar = 1;
	
	StringBuilder typed;
	
	public String getTyped() {
		return typed.toString();
	}
	
	public long getTime() { 
		return curTime;
	}
	public float getDelta() {
		return delta;
	}
	
	public String[] getSaveFile(String file, String title, String localDir){
		try {
			FileDialog fd = new FileDialog(new Frame(), title, FileDialog.SAVE);
		    fd.setFile(file);
		    fd.setLocation(50, 50);
		    String dir = new File(".").getCanonicalPath() +  "/" + localDir;
		    fd.setDirectory(dir);
		    System.out.println(dir);
		    System.out.println(fd.getDirectory());
		    fd.setVisible(true);
	    	return new String[] { fd.getDirectory(), fd.getFile()};
	    } catch ( Exception e) {
	    	return null;
	    }
	}
	
	public String[] getLoadFile(String file, String title, String localDir) {
		try {
			FileDialog fd = new FileDialog(new Frame(), title, FileDialog.LOAD);
		    fd.setFile(file);
		    fd.setLocation(50, 50);
		    String dir = new File(".").getCanonicalPath() +  "/" + localDir;
		    fd.setDirectory(dir);
		    
		    fd.setVisible(true);
		    return new String[] { fd.getDirectory(), fd.getFile()};
	    } catch ( Exception e) {
	    	return null;
	    }
	}
	
	public int getXMouse() { return xmouse; }
	public int getYMouse() { return ymouse; }
	
	public void sleepFor(long delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean isKeyDown(int key) {
		if ( key < 0 || key > 255 )
			return false;
		return keys[key] == EventKey.KEY_DOWN;
	}
	public boolean isKeyUp(int key) {
		if ( key < 0 || key > 255 )
			return false;
		return keys[key] == EventKey.KEY_UP;
	}
	public boolean getKeyState(int key) {
		if ( key < 0 || key > 255 )
			return false;
		return keys[key] == EventKey.KEY_PRESSED || keys[key] == EventKey.KEY_DOWN;
	}
	
	public void addEventKey(EventKey event) {
		events.add(event);
		if ( (event.state == EventKey.KEY_DOWN && keys[event.key] != EventKey.KEY_PRESSED ) || event.state != EventKey.KEY_DOWN ) {
			keys[event.key] = (char)event.state;
		}
	}
	
	private void keyFrame() {
		if ( !events.isEmpty() ) {
			int i = 0;
			while ( i < events.size() ) {
				EventKey cur = events.elementAt(i);
				if ( cur.state == EventKey.KEY_DOWN ) {
					keys[cur.key] = EventKey.KEY_PRESSED;
					events.remove(i);
					i--;
				} else if ( cur.state == EventKey.KEY_UP ) {
					keys[cur.key] = EventKey.KEY_NONE;
					events.remove(i);
					i--;
				}
				i++;
			}
		}
	}
	
	public void mouseMoved(MouseEvent event) {
		xmouse = (int) (event.getX() * ((float)screen.getWidth(null)/(float)getWidth())); 
        ymouse = (int) (event.getY() * ((float)screen.getHeight(null)/(float)getHeight()));
	}
	public void mouseDragged(MouseEvent event) {
		
		xmouse = (int) (event.getX() * ((float)screen.getWidth(null)/(float)getWidth())); 
        ymouse = (int) (event.getY() * ((float)screen.getHeight(null)/(float)getHeight()));
    }
	public void mousePressed(MouseEvent event) {
		xmouse = (int) (event.getX() * ((float)screen.getWidth(null)/(float)getWidth())); 
        ymouse = (int) (event.getY() * ((float)screen.getHeight(null)/(float)getHeight()));
        addEventKey(new EventKey(EventKey.KEY_DOWN, (char)event.getButton()));
	}
	public void mouseClicked(MouseEvent event) {
		xmouse = (int) (event.getX() * ((float)screen.getWidth(null)/(float)getWidth())); 
        ymouse = (int) (event.getY() * ((float)screen.getHeight(null)/(float)getHeight()));
	}
	public void mouseReleased(MouseEvent event) {
		xmouse = (int) (event.getX() * ((float)screen.getWidth(null)/(float)getWidth())); 
        ymouse = (int) (event.getY() * ((float)screen.getHeight(null)/(float)getHeight()));
        addEventKey(new EventKey(EventKey.KEY_UP, (char)event.getButton()));
	}
	
	public int getScreenWidth() {
		return screen.getWidth(null);
	}
	
	public int getScreenHeight() {
		return screen.getHeight(null);
	}
	
 	public void init() {
 		typed = new StringBuilder();
 		clearColor = new Color(0x888888);
 		screen = createImage(getWidth()/scalar, getHeight()/scalar);
 		screenGraphics = (Graphics2D)screen.getGraphics();
 		screenGraphics.setColor(Color.WHITE);
 		//screenGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0));
 		
 		keys = new char[256];
 		events = new Vector<EventKey>();
 		
 		for ( int i = 0; i < keys.length; i++ ) {
 			keys[i] = EventKey.KEY_NONE;
 		}
 		
 		addKeyListener(this);
 		addMouseMotionListener(this);
 		addMouseListener(this);
 		
 		input.baseURL = getCodeBase();
 		input.app = this;
 		System.out.println("Base URL: " + input.baseURL);
 		
 		draw.setGraphics(screenGraphics);
 		
 		onInit();
 		
 		//running = true;
  	}
    public void start() {
    	thread = new Thread(this);
    	thread.start();
    }
    public void stop() {
    	thread = null;
    }
    public void destroy() {
    }
    
    public abstract void onUpdate();
    public abstract void onRender();
    public abstract void onInit();
    
    public void onResize() {
    	draw.setGraphics(screenGraphics);
    }
    
    public void frame() {   
    	if ( System.currentTimeMillis() >= curTime + sleepTime ) {
    		delta = ((float)(System.currentTimeMillis() - curTime))/1000f;
    		curTime = System.currentTimeMillis();
    		
    		onUpdate(); // once every ~30th of a second.
    		repaint();
    		keyFrame();
    		typed = new StringBuilder();
    	} else {
    		long offset = (curTime + sleepTime) - System.currentTimeMillis();
    		if ( offset > 2 ) {
    			sleepFor(offset - 1);
    		}
    	}
    }
    public void run() {
    	curTime = System.currentTimeMillis();
    	while (Thread.currentThread() == thread && bRunning) {
    	    frame();
    	}
    }
    
    public Graphics2D createScreenGraphics() {
    	return (Graphics2D)((BufferedImage)screen).createGraphics();
    }

    public void update(Graphics g) {
    	if ( fitWindow ) {
			if ( screen.getWidth(null) != getWidth()/scalar || screen.getHeight(null) != getHeight()/scalar ) {
				setGraphicSize(getWidth()/scalar, getHeight()/scalar);
				onResize();
			}
    	}

		screenGraphics.setColor(clearColor);
    	screenGraphics.fillRect(0, 0, screen.getWidth(null), screen.getHeight(null));
    	
    	screenGraphics.setColor(Color.black);
    	
    	draw.setGraphics(screenGraphics);
    	
    	onRender();
    	
    	g.drawImage(screen, 0, 0, getWidth(), getHeight(), Color.WHITE, null);
    }
    public void paint(Graphics g) {
    	update(g);
    }
    
    public boolean takeScreenShot(String filename) {
		File newFile = new File(filename);
		try {
			ImageIO.write((RenderedImage)screen, "png", newFile);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
    
    public void repaint() {
    	super.repaint();
    }
    
    protected void setGraphicSize(int newWidth, int newHeight) {
    	screen.flush();
    	screenGraphics.dispose();
    	screen = createImage(newWidth, newHeight);
    	screenGraphics = (Graphics2D) screen.getGraphics();
    	//screenGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0));
	}

	@Override
	public void keyPressed(KeyEvent event) {
		addEventKey(new EventKey(EventKey.KEY_DOWN, (char)event.getKeyCode()));
	}

	@Override
	public void keyReleased(KeyEvent event) {
		addEventKey(new EventKey(EventKey.KEY_UP, (char)event.getKeyCode()));
	}

	@Override
	public void keyTyped(KeyEvent event) {
		if ( !event.isActionKey() && event.getKeyChar() != '\b' && event.getKeyChar() != '\n' ) {
			typed.append(event.getKeyChar());
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}
}
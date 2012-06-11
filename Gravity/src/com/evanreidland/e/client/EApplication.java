package com.evanreidland.e.client;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.InetAddress;

public class EApplication implements AppletStub {
	private Applet applet;
	private Frame frame;
	
	public int width, height;
	public EApplication(Applet applet) {
		this.applet = applet;
		width = 640;
		height = 480;
	}
	
	public Applet getApplet() {
		return applet;
	}
	
	public void runApplet(String title) {
		
		
		frame = new Frame(title);
		frame.setResizable(true);
		frame.setSize(width, height);
		 frame.addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent event) {
				 applet.stop();
				 applet.destroy();
				 System.exit(0);
			 }
		});
		frame.add(applet);
		applet.setStub(this);
		
		/*Display.setTitle(title);
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
		
			Display.setFullscreen(false);
			Display.create();
		}
		catch (LWJGLException e) {
			System.out.println("Could not set display mode " + width + "x" + height + "!");
		}*/
		frame.setVisible(true);
		applet.init();
		frame.pack();
		frame.setSize(width, height);
		applet.start();
	}
	
	public void switchApplet(Applet applet) {
		this.applet.destroy();
		frame.remove(this.applet);
		this.applet = applet;
		frame.add(applet);
		applet.setStub(this);
		applet.resize(width, height);
		applet.init();
		applet.start();
	}
	
	public void setTitle(String newTitle) {
		frame.setTitle(newTitle);
	}

	public void appletResize(int width, int height) {
		applet.resize(width, height);
	}

	public AppletContext getAppletContext () {
		return null;
	}
	
	public java.net.URL getCodeBase() {
		String host = new String();
		try {
			host = new File(".").getCanonicalPath();
		} catch (Exception e) {
			try {
			host = InetAddress.getLocalHost().getHostName();
			} catch ( Exception e2 ) {
				e2.printStackTrace();
			}
		}
		
		java.net.URL u = null;
		try {
			u = new java.net.URL("file:/" + host + '/');
			//System.out.println("run path: " + u);
	    } catch (Exception e){
	    	
	    }
	    return u;
	}
	public java.net.URL getDocumentBase() {
		return getCodeBase();
	}
	public String getParameter(String arg0) {
		return null;
	}

	public boolean isActive() {
		return true;
	}
}
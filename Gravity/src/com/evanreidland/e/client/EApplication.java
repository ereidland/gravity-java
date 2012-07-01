package com.evanreidland.e.client;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.InetAddress;

import javax.swing.JFrame;

public class EApplication implements AppletStub {
	private Applet applet;
	private JFrame frame;
	
	public int width, height;
	public EApplication(Applet applet) {
		this.applet = applet;
		width = 640;
		height = 480;
	}
	
	public Applet getApplet() {
		return applet;
	}
	
	public void runApplet(String title, boolean fullScreen) {
			frame = new JFrame(title);
			if ( fullScreen ) {
				frame.setResizable(false);
				frame.setUndecorated(true);
				
				Toolkit toolkit = Toolkit.getDefaultToolkit();  
				width = (int)toolkit.getScreenSize().getWidth();  
				height = (int)toolkit.getScreenSize().getHeight();
			} else {
				frame.setResizable(true);
			}
			frame.setAlwaysOnTop(false);
			frame.setFocusable(true);
			
			frame.setSize(width, height);
			 frame.addWindowListener(new WindowAdapter() {
				 public void windowLostFocus(WindowEvent event) {
					 System.out.println("!!M!M!M!");
					 frame.setState(Frame.ICONIFIED);
				 }
				 public void windowGainedFocus(WindowEvent event) {
					 System.out.println("WASDASDG");
					 frame.setState(Frame.NORMAL);
				 }
				 public void windowClosing(WindowEvent event) {
					 applet.stop();
					 applet.destroy();
					 System.exit(0);
				 }
			});
			frame.add(applet);
			applet.setStub(this);
			
			
			frame.setVisible(true);
			applet.init();
			frame.pack();
			frame.setSize(width, height);
			applet.start();
	}
	
	public void runApplet(String title) {
		runApplet(title, false);
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
package com.evanreidland.mecharena.launcher;

import java.applet.AppletStub;
import java.applet.Applet;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;

import com.evanreidland.e.ftp.Downloader;
import com.evanreidland.e.ftp.download;

public class GameLauncher extends E2DApplet implements AppletStub  {
	private static final long serialVersionUID = -29181652407622666L;
	
	public static final String downloadAddress = "http://dl.evtr.net/mecharena/";
	public static String path = defaultDirectory() + "/.emecharena/";
	
	public static long version = 0;
	
	public static String defaultDirectory()
	{
	    String OS = System.getProperty("os.name").toUpperCase();
	    if (OS.contains("WIN"))
	        return System.getenv("APPDATA");
	    else if (OS.contains("MAC"))
	        return System.getProperty("user.home") + "/Library/Application Support";
	    else if (OS.contains("NUX"))
	        return System.getProperty("user.home");
	    return System.getProperty("user.dir");
	}
	
	public Applet childApplet = null;
	
	public static class GameLauncherDownloader extends Downloader {
		public void onProgressChange(String curItem) {
			Log("Extracting: " + curItem);
			activeLauncher.message = "Extracting: " + curItem;
		}
		public void onError(String err) {
			Log(err);
			activeLauncher.message = err;
		}
		
		public void onFinish(float progress) {
			Log("(%" + (Math.round(progress*10)/10f)*100 + ") Done extracting from, '" + getAddress() + "'");
			activeLauncher.curProgress = progress;
			
			if ( progress >= 1 ) {
				try {
					DataOutputStream str = new DataOutputStream(new FileOutputStream(path + "version"));
					str.writeLong(version);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				activeLauncher.launchGame();
			}
		}
	}
	
	public void launchGame() {
		Log("Launching game...");
		try {
			String pathURL = "file:/" + path;
			ClassLoader loader = new URLClassLoader(new URL[] {
					new URL(pathURL + "bin/mecharena.jar"),
					new URL(pathURL + "bin/lwjgl.jar"),
					new URL(pathURL + "bin/lwjgl_util.jar") });
			
			Log("Path: " + pathURL + "bin/mecharena.jar");
			
			Class<?> aclass = loader.loadClass("com.evanreidland.e.client.mecharenaGame");
			Class<?> bclass = loader.loadClass("com.evanreidland.e.engine");
			Class<?> cclass = loader.loadClass("com.evanreidland.e.client.EApplet");
			Class<?> dclass = loader.loadClass("com.evanreidland.e.Game");
			
			bclass.getMethod("setGame", dclass).invoke(null, aclass.newInstance());
			
			Applet app = (Applet)cclass.newInstance();
			
			childApplet = app;
			
			getParent().add(app);
			getParent().remove(this);
			
			app.setStub(this);
			app.setSize(getWidth(), getHeight());
			app.init();
			app.start();
			
			bRunning = false;
			
			//aclass.getMethod("main", String[].class).invoke(null, new Object[] { new String[0] });
			Log("Launcher: Fin.");
		} catch ( Exception e ) {
			e.printStackTrace();
			Log("Exception: " + e.getMessage());
		}
	}
	
	public static long getOnlineVersion() {
		try {
			DataInputStream reader = new DataInputStream(new URL(downloadAddress + "version").openStream());
			long tversion = reader.readLong();
			Log("Updated game version: " + tversion);
			return tversion;
		} catch ( Exception e ) {
			e.printStackTrace();
			Log("Couldn't find updated game version... Going to have to just go with it.");
		}
		return 0;
	}
	
	public static GameLauncher activeLauncher;
	
	public String message = "", osName = "", binZip = "";
	public float curProgress = 0;
	
	public void onUpdate() {
		
	}
	
	public void onRender() {
		if ( !bRunning ) return;
		Vector2 center = new Vector2(getWidth()/2, getHeight()/2);
		Vector2 size2 = center.cloned().multipliedBy(0.7071f);
		
		draw.setPos(center);
		int s = 16;
		draw.setSize(getWidth() - s, getHeight() - s);
		
		draw.setColor(Color.gray.brighter());
		draw.solidCircle();
		
		draw.setColor(Color.gray.brighter().brighter());
		draw.setLineWidth(s/2);
		draw.Circle();
		
		draw.setPos(center.plus(size2));
		draw.setSize(size2.multipliedBy(0.5f));
		draw.setColor(curProgress >= 0.5f ? Color.white : Color.gray.brighter());
		draw.solidCircle();
		draw.setColor(Color.gray.brighter().brighter());
		draw.Circle();
		
		draw.setPos(center.plus(size2.multipliedBy(1, -1)));
		draw.setSize(size2.multipliedBy(0.5f));
		draw.setColor(curProgress >= 0.75f ? Color.white : Color.gray.brighter());
		draw.solidCircle();
		draw.setColor(Color.gray.brighter().brighter());
		draw.Circle();
		
		draw.setPos(center.plus(size2.multipliedBy(-1, -1)));
		draw.setSize(size2.multipliedBy(0.5f));
		draw.setColor(curProgress >= 0.25f ? Color.white : Color.gray.brighter());
		draw.solidCircle();
		draw.setColor(Color.gray.brighter().brighter());
		draw.Circle();
		
		draw.setPos(center.plus(size2.multipliedBy(-1, 1)));
		draw.setSize(size2.multipliedBy(0.5f));
		draw.setColor(curProgress >= 0.5f ? Color.white : Color.gray.brighter());
		draw.solidCircle();
		draw.setColor(Color.gray.brighter().brighter());
		draw.Circle();
		
		draw.setColor(Color.yellow);
		draw.setPos((int)(getWidth()*0.5f), (int)(getHeight()*0.5f));
		draw.Text(message);
	}
	
	public static PrintStream str = null;
	
	public static void Log(String txt) {
		System.out.println(txt);
		str.println(txt);
		activeLauncher.message = txt;
	}
	
	public void onInit() {
		/*try {
			DataOutputStream str = new DataOutputStream(new FileOutputStream(path + "version"));
			str.writeLong(0);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		activeLauncher = this;
		download.setDownloaderClass(GameLauncherDownloader.class);
		try {
			File f = new File(path);
			f.mkdir();
			str = new PrintStream(new FileOutputStream(path + "llog.txt"));
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		osName = System.getProperty("os.name");
		if (osName.startsWith("Win")) {
			if (System.getProperty("os.arch").endsWith("64")) {
				binZip = "windows_bin.zip";
			} else {
				binZip = "windows_bin.zip";
			}
		} else if (osName.startsWith("Linux")) {
			if (System.getProperty("os.arch").endsWith("64")) {
				binZip = "linux_bin.zip";
			} else {
				binZip = "linux_bin.zip";
			}

		} else if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
			binZip = "mac_bin.zip";
		} else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
			binZip = "solaris_bin.zip";
		} else if (osName.startsWith("FreeBSD")) {
			binZip = "linux_bin.zip";
		} else {
			message = "Error: OS (" + osName + ") not supported. :(";
			return;
		}
		
		try {
			DataInputStream data = new DataInputStream(new FileInputStream(path + "version"));
			version = data.readLong();
			Log("Game version: " + version );
			long olversion = getOnlineVersion();
			if ( olversion > version ) {
				Log("Current version is outdated by " + (olversion - version) + " update" + ((olversion - version) > 1 ? "s." : "." ));
				version = olversion;
				download.downloadList("http://dl.evtr.net/mecharena/", "downloadlist.txt " + binZip, path);
			} else if ( version > olversion ) {
				Log("Apparently this launch version is newer than the online version?...");
				launchGame();
			} else {
				Log("Current game is updated. Congrats.");
				launchGame();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			Log("Exception: " + e.getMessage());
			Log("Could not open version file, 'version'. Assuming launcher is outdated.");
			download.downloadList("http://dl.evtr.net/mecharena/", "downloadlist.txt " + binZip, path);
		}
	}
	
	public static void main(String[] args) {
		E2DApplication app = new E2DApplication(new GameLauncher());
		app.width = 640;
		app.height = 480;
		app.runApplet("MechArena Launcher");
	}
	
	public void resize(int width, int height) {
		super.resize(width, height);
		if ( childApplet != null ) {
			appletResize(width, height);
		}
	}

	public void appletResize(int width, int height) {
		if ( childApplet != null ) {
			childApplet.resize(width, height);
		}
	}
}

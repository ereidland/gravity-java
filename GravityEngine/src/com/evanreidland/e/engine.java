package com.evanreidland.e;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import com.evanreidland.e.audio.sound;
import com.evanreidland.e.graphics.FontResourceManager;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.script.Variable;

public class engine {
	private static ResourceManager[] managers;
	
	private static long curID = 0;
	
	private static float delta;
	private static long lastTime;
	
	public static final float Pi = (float)Math.PI,
							  Pi_2 = (float)(Math.PI*0.5),
							  Pi_4 = (float)(Math.PI*0.25),
							  Pi2 = (float)(Math.PI*2);
	
	public static Game game;
	
	public static String version = "";
	
	public static float getDelta() {
		return delta;
	}
	
	public static void updateTime() {
		lastTime = System.currentTimeMillis();
	}
	
	private static Vector<String> log = new Vector<String>();
	public static int maxLogs = 4;
	
	public static Vector<String> getLog() {
		return new Vector<String>(log);
	}
	
	public static boolean printErrors = true;
	
	private static String dataPath = "";
	
	public static String getPath() {
		return dataPath;
	}
	
	public static String getLastLog() {
		return log.lastElement();
	}
	
	public static void setGame(Game game) {
		engine.game = game; 
	}
	
	public static void createVersionFile(long version, String fname) {
		try {
			new DataOutputStream(new FileOutputStream(new File(fname))).writeLong(version);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public static void Update() {
		long diffTime = System.currentTimeMillis() - lastTime;
		if ( diffTime <= 0 ) return;
		
		delta = ((float)diffTime)/1000f;
		
		if ( game != null ) {
			sound.goToCamera();
			game.onUpdate();
		}
		lastTime = System.currentTimeMillis();
	}
	
	public static void Render() {
		if ( game != null ) {
			graphics.forward = graphics.camera.getForward();
			graphics.up = graphics.camera.getUp();
			graphics.right = graphics.camera.getRight();
			graphics.setCamera(graphics.camera);
			graphics.beginFrame();
			game.onRender();
			graphics.endFrame();
		}
	}
	public static void RenderHUD() {
		if ( game != null ) {
			graphics.setCamera2D(graphics.camera);
			graphics.beginFrame();
			game.onRenderHUD();
			graphics.endFrame();
		}
	}
	
	public static void Log(String str) {
		log.add(str);
		if ( log.size() > maxLogs ) {
			log = new Vector<String>(log.subList(log.size() - maxLogs, log.size()));
		}
		if ( printErrors ) System.out.println(getLastLog());
	}
	
	public static void Error(String err) {
		Log("Error: " + err);
	}
	
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
	
	public static void Initialize() {
		managers = new ResourceManager[ResourceType.values().length];
		for ( int i = 0; i < ResourceType.values().length; i++ ) {
			managers[i] = new ResourceManager(ResourceType.None);
		}
		
		dataPath = defaultDirectory() + "/.emecharena/";
		new File(dataPath).mkdir();
		
		System.out.println(dataPath);
		
		try {
			File f = new File(dataPath + "version");
		
			FileReader r = new FileReader(f);
			BufferedReader str = new BufferedReader(r);
			version = str.readLine();
		} catch (Exception e ) {
			version = "(Version unknown.)";
		}
		
		addResourceManager(ResourceType.Font, new FontResourceManager()); // Only default resource manager.
		
		lastTime = System.currentTimeMillis();
		game.onInit();
		lastTime = System.currentTimeMillis();
	}
	
	public static void addResourceManager(ResourceType type, ResourceManager m) {
		managers[type.ordinal()] = m;
	}
	
	public static void reloadAll(ResourceType type) {
		managers[type.ordinal()].reloadAll();
	}
	
	public static Resource loadResource(ResourceType type, String path) {
		String base = "";
		switch ( type ) {
			case Object:
				break;
			case Texture:
				base = "sprites/";
				break;
			case Font:
				base = "sprites/font/";
				break;
			case Sound:
				base = "sound/";
				break;
			case String:
				base = "text/";
				break;
			case Model:
				base = "models/";
				break;
			case None:
				break;
		}
		String fullName = base + path;
		boolean alreadyLoaded = managers[type.ordinal()].hasLoaded(fullName);
		Resource res = managers[type.ordinal()].load(fullName);
		if ( !alreadyLoaded ) {
			res.info.add(new Variable.Constant("path", new Value(path)));
			if ( res.isValid() ) {
				engine.Log("Loaded " + type.toString() + ": " + fullName);
			} else {
				engine.Log("Error loading " + type.toString() + ": " + fullName);
			}
		}
		return res;
	}
	
	public static Resource loadTexture(String path) {
		return loadResource(ResourceType.Texture, path);
	}
	
	public static Resource loadFont(String path) {
		return loadResource(ResourceType.Font, path);
	}
	
	public static Resource loadSound(String path) {
		return loadResource(ResourceType.Sound, path);
	}
	
	public static Resource loadString(String path) {
		return loadResource(ResourceType.String, path);
	}
	
	public static long newID() {
		return curID++;
	}
	
	public static boolean hasID(long id) {
		for ( int i = 0; i < ResourceType.values().length; i++ ) {
			if ( managers[i].hasID(id) ) {
				return true;
			}
		}
		return false;
	}
}

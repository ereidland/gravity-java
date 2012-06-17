package com.evanreidland.e.audio;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;

public class sound {
	private static AudioManager manager = null;
	public static Resource Load(String path) {
		return engine.loadSound(path);
	}
	
	public static void setAudioManager(AudioManager m) {
		manager = m;
	}
	
	public static void Play(Resource sound, Vector3 pos, double pitch) {
		if ( manager != null ) {
			manager.setPos(pos);
			manager.setPitch(pitch);
			manager.Play(sound);
		}
	}
	
	public static void Stop(Resource sound) {
		if ( manager != null ) {
			manager.Stop(sound);
		}
	}
	
	public static void goToCamera() {
		if ( manager != null ) {
			manager.goToCamera();
		}
	}
}

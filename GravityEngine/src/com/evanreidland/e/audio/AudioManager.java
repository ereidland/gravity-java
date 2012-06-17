package com.evanreidland.e.audio;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;

public abstract class AudioManager {
	public abstract Resource load(String file);
	public abstract void Play(Resource sound);
	public abstract void Stop(Resource sound);
	public abstract void setPos(Vector3 pos);
	public abstract void setPitch(double pitch);
	public abstract void goToCamera();
}

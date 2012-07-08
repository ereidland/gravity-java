package com.evanreidland.e.client.audio;

import com.evanreidland.e.Resource;
import com.evanreidland.e.ResourceType;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.audio.AudioManager;

public class ALAudioManager extends AudioManager
{

	public Resource load(String file)
	{
		return engine.loadSound(file);
	}

	public void Play(Resource sound)
	{
		if (sound.getType() == ResourceType.Sound && sound.isValid())
		{
			alsound.playSound((String) sound.getObject());
		}
	}

	public void Stop(Resource sound)
	{
		// TODO code.
	}

	public void setPos(Vector3 pos)
	{
		alsound.sourcePos.setAs(pos);
	}

	public void setPitch(double pitch)
	{
		alsound.pitch = pitch;
	}

	public void goToCamera()
	{
		alsound.goToCamera();
	}

}

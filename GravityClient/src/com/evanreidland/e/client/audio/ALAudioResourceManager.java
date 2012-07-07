package com.evanreidland.e.client.audio;

import com.evanreidland.e.Resource;
import com.evanreidland.e.ResourceManager;
import com.evanreidland.e.ResourceType;
import com.evanreidland.e.engine;

public class ALAudioResourceManager extends ResourceManager
{
	public Resource load(String address)
	{
		Long id = resID.get(address);
		if (id != null)
		{
			Resource r = get(id);
			if (r.isValid())
			{
				return r;
			}
		}
		if (alsound.loadSound(engine.getPath() + address, address))
		{
			Resource r = new Resource(ResourceType.Sound, address, true);
			res.put(r.getID(), r);
			resID.put(address, r.getID());
			return r;
		}
		return Resource.newInvalid();
	}

	public ALAudioResourceManager()
	{
		super(ResourceType.Sound);
	}

}

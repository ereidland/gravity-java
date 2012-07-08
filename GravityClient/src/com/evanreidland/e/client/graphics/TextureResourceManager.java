package com.evanreidland.e.client.graphics;

import com.evanreidland.e.Resource;
import com.evanreidland.e.ResourceManager;
import com.evanreidland.e.ResourceType;
import com.evanreidland.e.engine;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.script.Variable;

public class TextureResourceManager extends ResourceManager
{

	private TextureLoader loader = null;

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

		Texture tex = loader.getTexture(engine.getPath() + address);
		if (tex != null)
		{
			Resource r = new Resource(ResourceType.Texture, tex, true);
			r.info.add(new Variable.Constant("width", new Value(tex
					.getImageWidth())));
			r.info.add(new Variable.Constant("height", new Value(tex
					.getImageHeight())));
			res.put(r.getID(), r);
			resID.put(address, r.getID());
			return r;
		}
		return Resource.newInvalid();
	}

	public boolean reloadAll()
	{
		// TODO code.
		return false;
	}

	public TextureResourceManager()
	{
		super(ResourceType.Texture);
		loader = new TextureLoader();
	}
}

package com.evanreidland.e.graphics;

import java.io.DataInputStream;
import java.io.FileInputStream;

import com.evanreidland.e.Resource;
import com.evanreidland.e.ResourceManager;
import com.evanreidland.e.ResourceType;
import com.evanreidland.e.engine;

public class FontResourceManager extends ResourceManager {
	
	public Resource load(String address) {
		Long id = resID.get(address);
		if ( id != null ) {
			Resource r = get(id);
			if ( r.isValid() ) {
				return r;
			}
		}
    	try {
    		DataInputStream str = new DataInputStream(new FileInputStream(engine.getPath() + address + ".efont"));
    		byte version = str.readByte();
    		if ( version != 1 ) {
    			engine.Error("Can't load font, '" + address + "' because of bad version (" + version + ").");
    			return Resource.newInvalid();
    		}
    		str.readByte(); // Anti-alias. Not stored in FontGraphic
    		str.readByte(); // Bold. Likewise.
    		System.out.println(engine.getPath() + "font/" + address + ".png");
    		Resource r = engine.loadTexture(address.substring("sprites/".length()) + ".png");
    		if ( !r.isValid() ) {
    			engine.Error("Can't load font, '" + address + "' because of missing texture (.png expected).");
    			return Resource.newInvalid();
    		}
    		
    		float baseSize = str.readByte();
    		float padding = str.readByte();
    		float areaSize = str.readShort();
    		float[] widths = new float[256];
    		for ( int i = 0; i < widths.length; i++ ) {
    			widths[i] = str.readByte()/baseSize;
    			
    			//System.out.println(widths[i]);
    		}
    		
    		FontGraphic fnt = new FontGraphic(r, widths, areaSize, padding);
    		
    		res.put(fnt.getID(), fnt);
			resID.put(address, fnt.getID());
			
			return fnt;
    	} catch ( Exception e ) {
    		e.printStackTrace();
    	}
    	return Resource.newInvalid();
	}
	public FontResourceManager() {
		super(ResourceType.Font);
	}

}

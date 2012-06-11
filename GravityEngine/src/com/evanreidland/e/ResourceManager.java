package com.evanreidland.e;

import java.util.HashMap;

public class ResourceManager {
	private ResourceType type;
	protected HashMap<Long, Resource> res;
	protected HashMap<String, Long> resID;
	public ResourceType getType() {
		return type;
	}
	
	public boolean hasLoaded(String address) {
		return resID.containsKey(address);
	}
	
	public Resource load(String address) {
		return Resource.newInvalid();
	}
	
	public boolean reloadAll() {
		return false;
	}
	
	public Resource get(long id) {
		Resource r = res.get(id);
		if ( r == null ) {
			return Resource.newInvalid();
		} else {
			return r;
		}
	}
	
	public boolean hasID(long id) {
		Resource r = res.get(id);
		return r != null && r.isValid();
	}
	
	public ResourceManager(ResourceType type ) {
		res = new HashMap<Long, Resource>();
		resID = new HashMap<String, Long>();
		this.type = type;
	}
}

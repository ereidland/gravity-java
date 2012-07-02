package com.evanreidland.e.ent;

import com.evanreidland.e.Flags;
public class EObject {
	public Flags flags;
	private String className;
	private long id;
	
	public boolean matchesFlags(Flags oflags, boolean strict) {
		return flags.matchesOther(oflags, strict);
	}
	
	public long getID() {
		return id;
	}
	public boolean isValid() {
		return id != 0;
	}
	public String getClassName() {
		return className;
	}
	
	public void Be(long targetID) {
		id = ID.registerID(targetID, this);
	}
	public void Be() {
		Be(id);
	}
	
	protected void beForced() {
		ID.forceID(id, this);
	}
	
	public EObject(String className, long id) {
		this.className = className;
		
		flags = new Flags();
		this.id = id;
	}
	
	public EObject(String className) {
		this(className, 0);
	}
}

package com.evanreidland.e.ent;

import com.evanreidland.e.script.ProxyVariable;

public class NetworkedVariable extends ProxyVariable
{
	private Entity ent;
	
	protected void onSet()
	{
		super.onSet();
		if (ent != null)
			ent.setNWVar(getName(), this);
	}
	
	public NetworkedVariable(Entity ent, String name)
	{
		super(name, ent.vars.get(name));
		this.ent = ent;
	}
}

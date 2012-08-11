package com.evanreidland.e.script;

public class ProxyVariable extends Variable
{
	private Variable other;
	
	protected void onGet()
	{
		if (other != null)
			set(other);
	}
	
	protected void onSet()
	{
		if (other != null)
			other.set(this);
	}
	
	public ProxyVariable(String name, Variable other)
	{
		super(name, other);
		this.other = other;
	}
}

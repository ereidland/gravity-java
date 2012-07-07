package com.evanreidland.e.net;

public abstract class PacketReceiver
{
	private long id;

	public long getID()
	{
		return id;
	}

	public abstract void onReceive(Bits data);

	public PacketReceiver(long id)
	{
		this.id = id;
	}
}

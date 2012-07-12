package com.evanreidland.e.net;

public class TCPPacket
{
	private boolean isEvent;
	private TCPEvent event;
	
	private long id;
	public Bits bits;
	
	public long getID()
	{
		return id;
	}
	
	public boolean isEvent()
	{
		return isEvent;
	}
	
	public TCPEvent getEvent()
	{
		return event;
	}
	
	public TCPPacket(long id, TCPEvent event)
	{
		isEvent = true;
		this.id = id;
		this.event = event;
	}
	
	public TCPPacket(long id, Bits bits)
	{
		isEvent = false;
		event = null;
		this.id = id;
		this.bits = bits;
	}
}

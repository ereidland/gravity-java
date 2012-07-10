package com.evanreidland.e.net;

import java.util.Vector;

public class PacketBuffer
{
	private Vector<Bits> toSend;
	
	public void push(Bits bits)
	{
		toSend.add(bits);
	}
	
	public boolean canPull()
	{
		return toSend.size() > 0;
	}
	
	public Bits pull()
	{
		return toSend.remove(0);
	}
	
	public PacketBuffer()
	{
		toSend = new Vector<Bits>();
	}
}

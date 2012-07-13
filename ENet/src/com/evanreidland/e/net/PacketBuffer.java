package com.evanreidland.e.net;

import java.util.Vector;

public class PacketBuffer extends Aquireable
{
	private Vector<Bits> toSend;
	
	public void push(Bits bits)
	{
		aquire();
		toSend.add(bits);
		release();
	}
	
	public boolean canPull()
	{
		boolean can;
		aquire();
		can = toSend.size() > 0;
		release();
		return can;
	}
	
	public Bits pull()
	{
		Bits bits;
		aquire();
		bits = toSend.size() > 0 ? toSend.remove(0) : null;
		release();
		return bits;
	}
	
	public int getSize()
	{
		int count;
		aquire();
		count = toSend.size();
		release();
		return count;
	}
	
	public PacketBuffer()
	{
		toSend = new Vector<Bits>();
	}
}

package com.evanreidland.e.net;

import java.util.Vector;

public class SendBuffer
{
	private Vector<Bits> toSend;
	
	public void push(Bits bits)
	{
		System.out.println("Pushed.");
		toSend.add(bits);
	}
	
	public boolean canPull()
	{
		return toSend.size() > 0;
	}
	
	public Bits pull()
	{
		System.out.println("Pulled.");
		return toSend.remove(0);
	}
	
	public SendBuffer()
	{
		toSend = new Vector<Bits>();
	}
}

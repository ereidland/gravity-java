package com.evanreidland.e.net.test3;

import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.ConnectionState;
import com.evanreidland.e.net.NetLog;
import com.evanreidland.e.net.Packet;
import com.evanreidland.e.net.UDPHandler;

public class ENetStressServer
{
	private static class Server extends UDPHandler
	{
		long lastTime = 0;// , received = 0;
		
		public void onReceive(Packet p)
		{
			p.logInfo();
			long time = p.data.readLong();
			if (time < lastTime)
			{
				NetLog.Log("Error: Packet out of order (" + time + ").");
			}
			lastTime = time;
			/*
			 * received++; NetLog.Log(p.getSource().toString() + ": " + time);
			 * NetLog.Log("Received: " + received);
			 */
			Send(new Bits().writeLong(time).getBytes(), p.getSource(),
					p.getSourcePort());
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		Server server = new Server();
		server.Listen(27015);
		
		while (server.getState() == ConnectionState.LISTENING);
	}
}

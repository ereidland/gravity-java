package com.evanreidland.e.net.test2;

import com.evanreidland.e.net.ConnectionState;
import com.evanreidland.e.net.NetLog;
import com.evanreidland.e.net.Packet;
import com.evanreidland.e.net.UDPHandler;

public class ENetServer
{
	private static class Server extends UDPHandler
	{
		public void onReceive(Packet p)
		{
			p.logInfo();
			String str = new String(p.data.getBytes());
			NetLog.Log("Server - Received from " + p.getSource().toString()
					+ ": " + str);

			str = str.toUpperCase();
			Send(str.getBytes(), p.getSource(), p.getSourcePort());
		}

	}

	public static void main(String args[]) throws Exception
	{
		NetLog.addLogger(new NetLog.SystemOutLog());

		Server server = new Server();
		server.Listen(27015);

		while (server.getState() == ConnectionState.LISTENING)
			;
	}
}

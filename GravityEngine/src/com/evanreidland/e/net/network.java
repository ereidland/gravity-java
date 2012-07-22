package com.evanreidland.e.net;

public class network
{
	private static TCPServer server = null;
	private static TCPClient client = null;
	
	public static void setServer(TCPServer server)
	{
		network.server = server;
		if (client != null)
		{
			client.close();
			client = null;
		}
	}
	
	public static void setClient(TCPClient client)
	{
		network.client = client;
		if (server != null)
		{
			server.close();
			server = null;
		}
	}
	
	public static void Send(Bits bits)
	{
		if (server != null)
			server.broadcastData(bits);
		else if (client != null)
			client.Send(bits);
	}
	
	public static boolean isClient()
	{
		return client != null;
	}
	
	public static boolean isServer()
	{
		return server != null;
	}
}

package com.evanreidland.e.net.test4;

import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.TCPServer;

public class TCPServerTest
{
	private static class Server extends TCPServer
	{
		public void onDisconnect(long id)
		{
			System.out.println("Client disconnected: " + getFullAddress(id));
		}
		
		public void onNewConnection(long id)
		{
			System.out.println("New connection: " + getFullAddress(id));
		}
		
		public void onReceiveData(long id, Bits data)
		{
			String str = "From " + getFullAddress(id) + ": "
					+ data.readString();
			System.out.println(str);
			broadcastData(new Bits().writeString(str));
		}
		
		public void onListenException(Exception e)
		{
			System.out.println("On listen exception.");
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		Server server = new Server();
		server.Listen(27016);
		
		while (server.isListening());
		
		System.out.println("Done!");
	}
}

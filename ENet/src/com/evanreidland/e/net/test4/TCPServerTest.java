package com.evanreidland.e.net.test4;

import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.TCPPacket;
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
		
		public void onReceive(long id, Bits data)
		{
			String str = "From " + getFullAddress(id) + ": "
					+ data.readString();
			System.out.println(str);
			broadcastData(new Bits().writeString(str));
		}
		
		public void Update()
		{
			TCPPacket packet;
			while ((packet = pull()) != null)
			{
				if (packet.isEvent())
				{
					switch (packet.getEvent())
					{
						case CONNECT:
							startListening(packet.getID());
							onNewConnection(packet.getID());
							break;
						case DISCONNECT:
							onDisconnect(packet.getID());
							break;
						default:
							System.out.println("What? " + packet.getEvent());
							break;
					}
				}
				else
				{
					System.out.println("In length: "
							+ packet.bits.getRemainingBits());
					onReceive(packet.getID(), packet.bits);
				}
			}
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
		
		while (server.isListening())
		{
			server.Update();
		}
		
		System.out.println("Done!");
	}
}

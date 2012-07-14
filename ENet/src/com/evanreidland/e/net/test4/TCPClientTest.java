package com.evanreidland.e.net.test4;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.TCPClient;
import com.evanreidland.e.net.TCPEvent;
import com.evanreidland.e.net.TCPPacket;

public class TCPClientTest
{
	private static class Client extends TCPClient
	{
		public void onReceive(Bits data)
		{
			System.out.println("Received: " + data.readString());
		}
		
		public void onConnect()
		{
			System.out
					.println("Connected to " + getAddress() + ":" + getPort());
			startListening();
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
							onConnect();
							break;
						case DISCONNECT:
							System.out.println("Disconnected!");
							break;
					}
				}
				else
				{
					System.out.println("In length: "
							+ packet.bits.getRemainingBits());
					onReceive(packet.bits);
				}
			}
		}
		
		public void onException(Exception e, TCPEvent event)
		{
			System.out.println("Exception on event " + event.toString());
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]) throws Exception
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Please enter address: ");
		
		String[] split = in.readLine().split(":");
		
		String addr = split.length > 0 ? split[0] : "localhost";
		int port = 27016;
		
		try
		{
			if (split.length > 1)
			{
				port = Integer.valueOf(split[1]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Client client = new Client();
		client.Connect(addr, port);
		
		while (client.isConnected() || client.isConnecting())
		{
			if (!client.isConnecting())
			{
				client.Update();
				if (in.ready())
				{
					String str = in.readLine();
					System.out.println("Sending:" + str);
					client.Send(new Bits().writeString(str));
				}
			}
		}
		
		System.out.println("Done!");
	}
}

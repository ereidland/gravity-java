package com.evanreidland.e.net.test5;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.NetLog;
import com.evanreidland.e.net.StringTable;
import com.evanreidland.e.net.TCPClient;
import com.evanreidland.e.net.TCPEvent;

public class StringTableClientTest
{
	
	private static class Client extends TCPClient
	{
		StringTable table = null;
		
		public void onReceive(Bits data)
		{
			if (table == null)
			{
				table = new StringTable(false);
				table.setupFromBits(data);
			}
			else
			{
				NetLog.Log("Received string: " + table.getString(data));
			}
		}
		
		public void onConnect()
		{
			System.out
					.println("Connected to " + getAddress() + ":" + getPort());
		}
		
		public void onException(Exception e, TCPEvent event)
		{
			System.out.println("Exception on event " + event.toString());
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception
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
				String str = in.readLine();
				client.Send(new Bits().writeBytes(str.getBytes()));
			}
		}
		
		System.out.println("Done!");
	}
}

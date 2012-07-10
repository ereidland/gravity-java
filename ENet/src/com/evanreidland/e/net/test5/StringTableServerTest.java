package com.evanreidland.e.net.test5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.StringTable;
import com.evanreidland.e.net.TCPServer;

public class StringTableServerTest
{
	
	private static class Server extends TCPServer
	{
		private StringTable globalTable;
		private HashMap<Long, StringTable> clientTables;
		
		public void onDisconnect(long id)
		{
			clientTables.remove(id);
			System.out.println("Client disconnected: " + getFullAddress(id));
		}
		
		public void onNewConnection(long id)
		{
			StringTable table = new StringTable(globalTable, true, true);
			clientTables.put(id, table);
			sendData(id, table.toBits());
			System.out.println("New connection: " + getFullAddress(id));
		}
		
		public void sendMessage(String str)
		{
			globalTable.add(str);
			Vector<Long> clients = getClients();
			for (int i = 0; i < clients.size(); i++)
			{
				long id = clients.get(i);
				sendData(id, clientTables.get(id).getBits(str));
			}
		}
		
		public void onReceiveData(long id, Bits data)
		{
			String str = "From " + getFullAddress(id) + ": "
					+ new String(data.readRemaining());
			System.out.println(str);
		}
		
		public void onListenException(Exception e)
		{
			System.out.println("On listen exception.");
			e.printStackTrace();
		}
		
		public Server()
		{
			clientTables = new HashMap<Long, StringTable>();
			globalTable = new StringTable(true);
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		Server server = new Server();
		server.Listen(27016);
		
		while (server.isListening())
		{
			String str = in.readLine();
			server.sendMessage(str);
		}
		
		System.out.println("Done!");
	}
}

package com.evanreidland.e.net;

public class EServer
{

	public class EUDPServer extends UDPHandler
	{
		public void onReceive(Packet p)
		{

		}
	}

	public class ETCPServer extends TCPServer
	{
		public void onDisconnect(long id)
		{
		}

		public void onNewConnection(long id)
		{
			Bits toSend = new Bits();
			toSend.writeByte((byte) NetCode.CONNECT_UDP.ordinal());
			toSend.writeLong(id);
			toSend.writeShort((short) udpServer.getPort());
			sendData(id, toSend);
		}

		public void onReceiveData(long id, Bits data)
		{

		}

		public void onListenException(Exception e)
		{

		}
	}

	public EUDPServer udpServer;
	public ETCPServer tcpServer;

	public void Listen(int tcpPort, int udpPort)
	{
		udpServer.Listen(udpPort);
		tcpServer.Listen(tcpPort);
	}

	public EServer()
	{
		udpServer = new EUDPServer();
		tcpServer = new ETCPServer();
	}
}

package com.evanreidland.e.net;

public class EClient
{
	public class EUDPClient extends UDPHandler
	{
		public void onReceive(Packet p)
		{
		}
	}

	public class ETCPClient extends TCPClient
	{
		public void onReceive(Bits data)
		{
			byte b = data.readByte();
			if (b < NetCode.values().length)
			{
				NetCode code = NetCode.values()[b];
				switch (code)
				{
				case CONNECT_UDP:
					long id = data.readLong();
					short port = data.readShort();
					udpClient.Connect(getAddress(), port);
					udpClient.Send(new Bits().writeLong(id));
				default:
					onException(new Exception("Unused NetCode: " + code),
							TCPEvent.RECEIVE);
				}
			}
			else
			{
				onException(new Exception("Bad NetCode index: " + b),
						TCPEvent.RECEIVE);
			}
		}

		public void onConnect()
		{
		}

		public void onException(Exception e, TCPEvent event)
		{
		}

	}

	public EUDPClient udpClient;
	public ETCPClient tcpClient;
}

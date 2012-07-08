package com.evanreidland.e.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class UDPHandler
{
	private InetAddress addr;
	private int port;

	private ConnectionState cstate;
	private DatagramSocket socket;

	public String getAddress()
	{
		return addr != null ? addr.toString() : "";
	}

	public int getPort()
	{
		return port;
	}

	public ConnectionState getState()
	{
		return cstate;
	}

	public void Close()
	{
		cstate = ConnectionState.DISCONNECTED;
		socket = null;
	}

	public abstract void onReceive(Packet p);

	private class ListenThread implements Runnable
	{
		public void run()
		{
			try
			{
				byte[] data = new byte[Packet.maxUDPSize];
				while (cstate == ConnectionState.CONNECTED
						|| cstate == ConnectionState.LISTENING)
				{
					DatagramPacket receivePacket = new DatagramPacket(data,
							data.length);
					socket.receive(receivePacket);

					onReceive(Packet
							.fromBytes(receivePacket.getData(),
									receivePacket.getAddress(),
									receivePacket.getPort()));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				cstate = ConnectionState.DISCONNECTED;
			}
		}
	}

	Thread listenThread;

	public void Send(byte[] data, InetAddress addr, int port, long targetID)
	{
		try
		{
			Packet p = new Packet();
			p.data.writeBytes(data);
			p.timeStamp = System.currentTimeMillis();
			p.targetID = targetID;
			byte[] sendData = p.toBytes();
			DatagramPacket sendPacket = addr == null ? new DatagramPacket(
					sendData, sendData.length, this.addr, this.port)
					: new DatagramPacket(sendData, sendData.length, addr, port);
			socket.send(sendPacket);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			cstate = ConnectionState.DISCONNECTED;
		}
	}

	public void Send(byte[] data, InetAddress addr, int port)
	{
		Send(data, addr, port, 0);
	}

	public void Send(byte[] data)
	{
		Send(data, addr, port);
	}

	public void Send(byte[] data, long targetID)
	{
		Send(data, addr, port, targetID);
	}

	public void Send(byte[] data, String addr, int port)
	{
		try
		{
			Send(data, InetAddress.getByName(addr), port);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			cstate = ConnectionState.DISCONNECTED;
		}
	}

	public void Send(Bits data)
	{
		Send(data.readRemaining(), addr, port);
	}

	public void Send(Bits data, InetAddress addr, int port)
	{
		Send(data.readRemaining(), addr, port);
	}

	public void Send(Bits data, String addr, int port)
	{
		Send(data.readRemaining(), addr, port);
	}

	// KIND of pointless.
	public void Connect(String address, int port)
	{
		try
		{
			addr = InetAddress.getByName(address);
			this.port = port;
			cstate = ConnectionState.CONNECTED;

			socket = new DatagramSocket();

			listenThread = new Thread(new ListenThread());
			listenThread.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			addr = null;
			this.port = 0;
			cstate = ConnectionState.DISCONNECTED;
		}
	}

	public void Listen(int port)
	{
		try
		{
			addr = null;
			this.port = port;
			cstate = ConnectionState.LISTENING;

			socket = new DatagramSocket(port);

			listenThread = new Thread(new ListenThread());
			listenThread.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			addr = null;
			this.port = 0;
			cstate = ConnectionState.DISCONNECTED;
		}
	}

	public UDPHandler()
	{
		addr = null;
		port = 0;
		cstate = ConnectionState.DISCONNECTED;
		listenThread = null;
		socket = null;
	}
}

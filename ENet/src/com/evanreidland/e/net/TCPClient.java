package com.evanreidland.e.net;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Vector;

public abstract class TCPClient extends Aquireable
{
	public static long waitTime = 5;
	private Socket socket;
	
	private Thread receiveThread, sendThread;
	private String addr;
	private int port;
	
	private boolean isConnecting;
	
	private PacketBuffer queue;
	
	private Vector<TCPPacket> packets;
	
	public int getPacketCount()
	{
		int count;
		aquire();
		count = packets.size();
		release();
		return count;
	}
	
	public TCPPacket pull()
	{
		TCPPacket packet;
		aquire();
		packet = packets.size() > 0 ? packets.remove(0) : null;
		release();
		return packet;
	}
	
	public boolean isConnecting()
	{
		return isConnecting;
	}
	
	public String getAddress()
	{
		return addr;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public boolean isConnected()
	{
		return socket != null && socket.isConnected();
	}
	
	private class SendThread implements Runnable
	{
		
		public void run()
		{
			NetLog.Log("Send thread initialized.");
			try
			{
				while (socket != null)
				{
					Bits data = queue.pull();
					if (data != null)
					{
						Bits finalData = new Bits();
						int len = data.getRemainingBytes();
						finalData.writeInt(len);
						finalData.write(data);
						socket.getOutputStream().write(
								finalData.readRemaining());
					}
					else
					{
						try
						{
							Thread.sleep(waitTime);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			catch (Exception e)
			{
				onException(e, TCPEvent.SEND);
			}
		}
		
		public SendThread()
		{
		}
	}
	
	private class ReceiveThread implements Runnable
	{
		public void run()
		{
			NetLog.Log("Receive thread initialized.");
			try
			{
				while (socket != null)
				{
					DataInputStream str = new DataInputStream(
							socket.getInputStream());
					int size = str.readInt();
					if (size > 0)
					{
						byte[] bytes = new byte[size];
						str.readFully(bytes, 0, size);
						
						aquire();
						packets.add(new TCPPacket(0, new Bits()
								.writeBytes(bytes)));
						release();
					}
				}
			}
			catch (Exception e)
			{
				onException(e, TCPEvent.RECEIVE);
			}
		}
	}
	
	private class ConnectThread implements Runnable
	{
		public String addr;
		public int port;
		
		public void run()
		{
			try
			{
				socket = new Socket(addr, port);
			}
			catch (Exception e)
			{
				onException(e, TCPEvent.CONNECT);
			}
			
			if (socket != null && socket.isConnected())
			{
				TCPClient.this.addr = addr;
				TCPClient.this.port = port;
				aquire();
				packets.add(new TCPPacket(0, TCPEvent.CONNECT));
				release();
			}
			else
			{
				onException(new Exception("Failed connection"),
						TCPEvent.CONNECT);
			}
			isConnecting = false;
		}
		
		public ConnectThread(String addr, int port)
		{
			this.addr = addr;
			this.port = port;
		}
	}
	
	public void Connect(String addr, int port)
	{
		isConnecting = true;
		Thread thread = new Thread(new ConnectThread(addr, port));
		thread.setName("Client.Connect");
		thread.start();
	}
	
	public void startListening()
	{
		receiveThread = new Thread(new ReceiveThread());
		receiveThread.setName("Client.Receive");
		receiveThread.start();
		
		sendThread = new Thread(new SendThread());
		sendThread.setName("Client.Send");
		sendThread.start();
	}
	
	public void Send(Bits data)
	{
		queue.push(data);
	}
	
	public abstract void onException(Exception e, TCPEvent event);
	
	public void close()
	{
		try
		{
			socket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		receiveThread = null;
		sendThread = null;
		addr = "localhost";
		port = 27015;
	}
	
	public TCPClient()
	{
		socket = null;
		receiveThread = null;
		sendThread = null;
		
		remainingBits = 0;
		formingPacket = new Bits();
		
		queue = new PacketBuffer();
		packets = new Vector<TCPPacket>();
	}
}

package com.evanreidland.e.net;

import java.net.Socket;
import java.util.Vector;

public abstract class TCPClient extends Aquireable
{
	public static long waitTime = 5;
	private Socket socket;
	
	private Thread receiveThread, sendThread;
	private String addr;
	private int port;
	
	private int remainingBits;
	private Bits formingPacket;
	
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
	
	public void processData(Bits data)
	{
		if (remainingBits == 0)
		{
			Bits basePacket = formingPacket.skipTo(0).getRemainingBits() > 0 ? new Bits()
					.write(formingPacket).write(data) : data;
			if (basePacket.getRemainingBits() >= 32)
			{
				formingPacket = new Bits();
				remainingBits = data.readInt();
				processData(basePacket);
			}
			else
			{
				formingPacket = new Bits();
				formingPacket.write(basePacket.skipTo(0));
			}
		}
		else
		{
			if (data.getRemainingBits() >= remainingBits)
			{
				formingPacket.writeBits(data.readBits(remainingBits),
						remainingBits);
				aquire();
				packets.add(new TCPPacket(0, formingPacket));
				release();
				
				formingPacket = new Bits();
				remainingBits = 0;
				
				if (data.getRemainingBits() > 0)
				{
					processData(data);
				}
			}
			else if (data.getRemainingBytes() > 0)
			{
				int fremainingBits = data.getRemainingBits();
				remainingBits -= fremainingBits;
				formingPacket.writeBits(data.readRemaining(), fremainingBits);
			}
		}
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
						int len = data.getRemainingBytes() * 8;
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
					int size = socket.getInputStream().available();
					if (size > 0)
					{
						byte[] bytes = new byte[size];
						socket.getInputStream().read(bytes);
						processData(new Bits().writeBytes(bytes));
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

package com.evanreidland.e.net;

import java.net.Socket;

public abstract class TCPClient
{
	
	private Socket socket;
	
	private Thread receiveThread;
	private String addr;
	private int port;
	
	private int remainingBits;
	private Bits formingPacket;
	
	private boolean isConnecting;
	
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
			int rbits = data.getRemainingBits();
			if (rbits >= 8)
			{
				if (data.readBit())
				{
					remainingBits = data.readByte();
				}
				else if (rbits >= 16 && data.readBit())
				{
					remainingBits = data.readShort();
				}
				else if (rbits >= 32)
				{
					remainingBits = data.readInt();
				}
				processData(data);
			}
		}
		else
		{
			if (data.getRemainingBits() >= remainingBits)
			{
				formingPacket.writeBits(data.readBits(remainingBits),
						remainingBits);
				onReceive(formingPacket.trim());
				
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
		public Bits data;
		
		public void run()
		{
			try
			{
				Bits finalData = new Bits();
				int len = (int) Math.ceil(data.getRemainingBits() / 8f) * 8;
				finalData.writeBit(data.getRemainingBits() <= Byte.MAX_VALUE);
				if (len <= Byte.MAX_VALUE)
				{
					finalData.writeByte((byte) len);
				}
				else if (len <= Short.MAX_VALUE)
				{
					finalData.writeBit(true);
					finalData.writeShort((short) len);
				}
				else
				{
					finalData.writeBit(false);
					finalData.writeInt(len);
				}
				finalData.writeBits(data.readRemaining(), len);
				socket.getOutputStream().write(finalData.readRemaining());
			}
			catch (Exception e)
			{
				onException(e, TCPEvent.SEND);
			}
		}
		
		public SendThread(Bits data)
		{
			this.data = data;
		}
	}
	
	private class ReceiveThread implements Runnable
	{
		public void run()
		{
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
				onSocketConnect();
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
		new Thread(new ConnectThread(addr, port)).start();
	}
	
	private void onSocketConnect()
	{
		receiveThread = new Thread(new ReceiveThread());
		receiveThread.start();
		
		onConnect();
	}
	
	public void Send(Bits data)
	{
		new Thread(new SendThread(data)).start();
	}
	
	public abstract void onReceive(Bits data);
	
	public abstract void onConnect();
	
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
		addr = "localhost";
		port = 27015;
	}
	
	public TCPClient()
	{
		socket = null;
		receiveThread = null;
		
		remainingBits = 0;
		formingPacket = new Bits();
	}
}

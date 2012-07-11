package com.evanreidland.e.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public abstract class TCPServer
{
	private ServerSocket socket;
	private int port;
	
	private long lastID = 0;
	private Vector<Client> clients;
	
	private Thread listenThread;
	
	public int getPort()
	{
		return port;
	}
	
	private class Client
	{
		public long id;
		public Socket socket;
		public Thread receiveThread;
		
		public Bits formingPacket;
		public int remainingBits;
		
		public PacketBuffer queue;
		public SendThread activeSender;
		
		public void onException(Exception e, TCPEvent event)
		{
			e.printStackTrace();
			onDisconnect(id);
			clients.remove(this);
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
					onReceiveData(id, formingPacket.trim());
					
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
					formingPacket.writeBits(data.readRemaining(),
							fremainingBits);
				}
			}
		}
		
		public void Send(Bits data)
		{
			queue.push(data);
			if (activeSender == null)
			{
				new Thread(new SendThread(this, queue)).start();
			}
		}
		
		public void close()
		{
			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		public void listenForData()
		{
			receiveThread = new Thread(new ReceiveThread(this));
			receiveThread.start();
		}
		
		public Client(Socket socket)
		{
			id = ++lastID;
			receiveThread = null;
			formingPacket = new Bits();
			remainingBits = 0;
			queue = new PacketBuffer();
			this.socket = socket;
		}
	}
	
	public Vector<Long> getClients()
	{
		Vector<Long> list = new Vector<Long>();
		for (int i = 0; i < clients.size(); i++)
		{
			list.add(clients.get(i).id);
		}
		return list;
	}
	
	public String getAddress(long id)
	{
		Client client = getClient(id);
		return client != null ? client.socket.getInetAddress().toString() : "";
	}
	
	public int getPort(long id)
	{
		Client client = getClient(id);
		return client != null ? client.socket.getPort() : 0;
	}
	
	public String getFullAddress(long id)
	{
		return getAddress(id) + ":" + getPort(id);
	}
	
	public abstract void onDisconnect(long id);
	
	public abstract void onNewConnection(long id);
	
	public abstract void onReceiveData(long id, Bits data);
	
	public abstract void onListenException(Exception e);
	
	public void broadcastData(long allBut, Bits data)
	{
		for (int i = 0; i < clients.size(); i++)
		{
			Client client = clients.get(i);
			if (client.id != allBut)
			{
				client.Send(data);
			}
		}
	}
	
	public void broadcastData(Bits data)
	{
		broadcastData(-1, data);
	}
	
	private Client getClient(long id)
	{
		for (int i = 0; i < clients.size(); i++)
		{
			Client client = clients.get(i);
			if (client.id == id)
			{
				return client;
			}
		}
		return null;
	}
	
	public void sendData(long id, Bits data)
	{
		Client client = getClient(id);
		if (client != null)
		{
			client.Send(data);
		}
	}
	
	private class SendThread implements Runnable
	{
		public Client client;
		public PacketBuffer buffer;
		
		public void run()
		{
			try
			{
				while (buffer.canPull())
				{
					Bits data = buffer.pull();
					Bits finalData = new Bits();
					int len = (int) Math.ceil(data.getRemainingBits() / 8f) * 8;
					finalData
							.writeBit(data.getRemainingBits() <= Byte.MAX_VALUE);
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
					client.socket.getOutputStream().write(
							finalData.readRemaining());
				}
			}
			catch (Exception e)
			{
				client.onException(e, TCPEvent.SEND);
			}
			client.activeSender = null;
		}
		
		public SendThread(Client client, PacketBuffer buffer)
		{
			this.client = client;
			this.buffer = buffer;
			client.activeSender = this;
		}
	}
	
	private class ReceiveThread implements Runnable
	{
		public Client client;
		
		public void run()
		{
			while (client.socket != null)
			{
				try
				{
					if (!client.socket.isConnected())
					{
						onDisconnect(client.id);
						clients.remove(client);
					}
					else
					{
						int size = client.socket.getInputStream().available();
						if (size > 0)
						{
							byte[] bytes = new byte[size];
							client.socket.getInputStream().read(bytes);
							client.processData(new Bits().writeBytes(bytes));
						}
					}
				}
				catch (Exception e)
				{
					client.onException(e, TCPEvent.RECEIVE);
					break;
				}
			}
			
			onDisconnect(client.id);
			clients.remove(client);
		}
		
		public ReceiveThread(Client client)
		{
			this.client = client;
		}
	}
	
	public boolean isListening()
	{
		return socket != null;
	}
	
	private class ListenThread implements Runnable
	{
		public void run()
		{
			try
			{
				while (socket != null)
				{
					Socket clientSocket = socket.accept();
					Client client = new Client(clientSocket);
					clients.add(client);
					onNewConnection(client.id);
					client.listenForData();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				onListenException(e);
			}
		}
	}
	
	public void Listen(int port)
	{
		this.port = port;
		
		if (listenThread == null)
		{
			try
			{
				socket = new ServerSocket(port);
			}
			catch (Exception e)
			{
				onListenException(e);
			}
			listenThread = new Thread(new ListenThread());
			listenThread.start();
		}
		else
		{
			onListenException(new Exception("Already listening"));
		}
	}
	
	public void close()
	{
		socket = null;
		listenThread = null;
		for (int i = 0; i < clients.size(); i++)
		{
			clients.get(i).close();
		}
		clients.clear();
		lastID = 1;
	}
	
	public TCPServer()
	{
		clients = new Vector<Client>();
		port = 27015;
		socket = null;
	}
}

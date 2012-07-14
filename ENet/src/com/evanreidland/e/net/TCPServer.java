package com.evanreidland.e.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;

public abstract class TCPServer extends Aquireable
{
	private ServerSocket socket;
	private int port;
	
	private long lastID = 0;
	private Vector<Client> clients;
	
	private Thread listenThread;
	
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
	
	public int getPort()
	{
		return port;
	}
	
	private class Client
	{
		public long id;
		public Socket socket;
		public Thread receiveThread, sendThread;
		
		public Bits formingPacket;
		public int remainingBits;
		
		public PacketBuffer queue;
		
		private class SendThread implements Runnable
		{
			public void run()
			{
				NetLog.Log("Send thread initialized for " + id);
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
								Thread.sleep(TCPClient.waitTime);
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
		
		public void onException(Exception e, TCPEvent event)
		{
			e.printStackTrace();
			aquire();
			packets.add(new TCPPacket(id, TCPEvent.DISCONNECT));
			release();
			clients.remove(this);
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
					remainingBits = basePacket.readInt();
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
					packets.add(new TCPPacket(id, formingPacket));
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
					formingPacket.writeBits(data.readRemaining(),
							fremainingBits);
				}
			}
		}
		
		public void Send(Bits data)
		{
			queue.push(data);
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
		
		private class ReceiveThread implements Runnable
		{
			public void run()
			{
				NetLog.Log("Receive thread initialized for " + id);
				while (socket != null)
				{
					try
					{
						if (!socket.isConnected())
						{
							aquire();
							packets.add(new TCPPacket(id, TCPEvent.DISCONNECT));
							release();
							clients.remove(this);
						}
						else
						{
							int size = socket.getInputStream().available();
							if (size > 0)
							{
								byte[] bytes = new byte[size];
								socket.getInputStream().read(bytes);
								processData(new Bits().writeBytes(bytes));
							}
							else
							{
								try
								{
									Thread.sleep(TCPClient.waitTime);
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
						onException(e, TCPEvent.RECEIVE);
						break;
					}
				}
				
				aquire();
				packets.add(new TCPPacket(id, TCPEvent.DISCONNECT));
				release();
				clients.remove(this);
			}
			
			public ReceiveThread()
			{
			}
		}
		
		public void startListening()
		{
			receiveThread = new Thread(new ReceiveThread());
			receiveThread.setName("Server.Receive." + id);
			receiveThread.start();
			
			sendThread = new Thread(new SendThread());
			sendThread.setName("Server.Send." + id);
			sendThread.start();
		}
		
		public Client(Socket socket)
		{
			aquire();
			id = ++lastID;
			release();
			receiveThread = null;
			sendThread = null;
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
	
	public void startListening(long clientID)
	{
		Client client = getClient(clientID);
		if (client != null)
		{
			client.startListening();
		}
		else
		{
			NetLog.logger.log(Level.SEVERE, "Client does not exist: "
					+ clientID);
		}
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
	
	public abstract void onListenException(Exception e);
	
	public void broadcastData(long allBut, Bits data)
	{
		for (int i = 0; i < clients.size(); i++)
		{
			Client client = clients.get(i);
			if (allBut == -1 || client.id != allBut)
			{
				client.Send(data.copy());
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
					aquire();
					packets.add(new TCPPacket(client.id, TCPEvent.CONNECT));
					release();
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
			listenThread.setName("Server.Listen");
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
		packets = new Vector<TCPPacket>();
		port = 27015;
		socket = null;
	}
}

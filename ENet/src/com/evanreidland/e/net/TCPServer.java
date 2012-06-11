package com.evanreidland.e.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public abstract class TCPServer {
	private ServerSocket socket;
	private int port;
	
	private long lastID = 0;
	private Vector<Client> clients;
	
	private Thread listenThread;
	
	public int getPort() {
		return port;
	}
	
	private class Client {
		public long id;
		public Socket socket;
		public Thread receiveThread;
		
		public Bits formingPacket;
		public int remainingBytes;
		
		public void onException(Exception e, TCPEvent event) {
			e.printStackTrace();
			onDisconnect(id);
			clients.remove(this);
		}
		
		public void processData(Bits data) {
			if ( remainingBytes == 0 ) {
				if ( data.getRemainingBytes() > 0 ) {
					if ( data.readBit() ) {
						remainingBytes = data.readByte();
					} else {
						remainingBytes = data.readShort();
					}
					processData(data);
				}
			} else {
				if ( data.getRemainingBytes() >= remainingBytes ) {
					formingPacket.writeBytes(data.readBytes(remainingBytes));
					onReceiveData(id, formingPacket.trim());
					
					formingPacket = new Bits();
					remainingBytes = 0;
					
					if ( data.getRemainingBytes() > 0 ) {
						processData(data);
					}
				} else if ( data.getRemainingBytes() > 0) {
					remainingBytes -= data.getRemainingBytes();
					formingPacket.writeBytes(data.readRemaining());
				}
			}
		}
		
		public void Send(byte[] data) {
			new Thread(new SendThread(this, data)).start();
		}
		
		public void close() {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void listenForData() {
			receiveThread = new Thread(new ReceiveThread(this));
			receiveThread.start();
		}
		
		public Client(Socket socket) {
			id = ++lastID;
			receiveThread = null;
			formingPacket = new Bits();
			remainingBytes = 0;
			this.socket = socket;
		}
	}
	
	
	public String getAddress(long id) {
		Client client = getClient(id);
		return client != null ? client.socket.getInetAddress().toString() : "";
	}
	
	public int getPort(long id) {
		Client client = getClient(id);
		return client != null ? client.socket.getPort() : 0;
	}
	
	public String getFullAddress(long id) {
		return getAddress(id) + ":" + getPort(id);
	}
	
	public abstract void onDisconnect(long id);
	public abstract void onNewConnection(long id);
	public abstract void onReceiveData(long id, Bits data);
	public abstract void onListenException(Exception e);
	
	public void broadcastData(long allBut, byte[] data) {
		for ( int i = 0; i < clients.size(); i++ ) {
			Client client = clients.get(i);
			if ( client.id != allBut ) {
				client.Send(data);
			}
		}
	}
	
	public void broadcastData(byte[] data) {
		broadcastData(-1, data);
	}
	
	private Client getClient(long id) {
		for ( int i = 0; i < clients.size(); i++ ) {
			Client client = clients.get(i);
			if ( client.id == id ) {
				return client;
			}
		}
		return null;
	}
	
	public void sendData(long id, byte[] data) {
		Client client = getClient(id);
		if ( client != null ) {
			client.Send(data);
		}
	}
	
	private class SendThread implements Runnable {
		public Client client;
		public byte[] data;
		public void run() {
			try {
				Bits finalData = new Bits();
				finalData.writeBit(data.length <= 255);
				if ( data.length <= 255 ) {
					finalData.writeByte((byte)data.length);
				} else {
					finalData.writeShort((short)data.length);
				}
				finalData.writeBytes(data);
				client.socket.getOutputStream().write(finalData.trim().getBytes());
			} catch ( Exception e ) {
				client.onException(e, TCPEvent.SEND);
			}
		}
		
		public SendThread(Client client, byte[] data) {
			this.client = client;
			this.data = data;
		}
	}
	private class ReceiveThread implements Runnable {
		public Client client;
		public void run() {
			while ( client.socket != null ) {
				try {
					if ( !client.socket.isConnected() ) {
						onDisconnect(client.id);
						clients.remove(client);
					} else {
						int size = client.socket.getInputStream().available();
						if ( size > 0 ) {
							byte[] bytes = new byte[size];
							client.socket.getInputStream().read(bytes);
							client.processData(new Bits().writeBytes(bytes));
						}
					}
				} catch ( Exception e ) {
					client.onException(e, TCPEvent.RECEIVE);
					break;
				}
			}
			
			onDisconnect(client.id);
			clients.remove(client);
		}
		
		public ReceiveThread(Client client) {
			this.client = client;
		}
	}
	
	public boolean isListening() {
		return socket != null;
	}
	
	private class ListenThread implements Runnable {
		public void run() {
			try {
				while ( socket != null ) {
					Socket clientSocket = socket.accept();
					Client client = new Client(clientSocket);
					clients.add(client);
					onNewConnection(client.id);
					client.listenForData();
				}
			} catch ( Exception e ) {
				e.printStackTrace();
				onListenException(e);
			}
		}
	}
	
	public void Listen(int port) {
		this.port = port;
		
		if ( listenThread == null ) {
			try {
				socket = new ServerSocket(port);
			} catch ( Exception e ) {
				onListenException(e);
			}
			listenThread = new Thread(new ListenThread());
			listenThread.start();
		} else {
			onListenException(new Exception("Already listening"));
		}
	}
	
	public void close() {
		socket = null;
		listenThread = null;
		for ( int i = 0; i < clients.size(); i++ ) {
			clients.get(i).close();
		}
		clients.clear();
		lastID = 1;
	}
	
	public TCPServer() {
		clients = new Vector<Client>();
		port = 27015;
		socket = null;
	}
}

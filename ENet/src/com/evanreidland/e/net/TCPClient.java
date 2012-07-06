package com.evanreidland.e.net;

import java.net.Socket;


public abstract class TCPClient {
	
	private Socket socket;
	
	private Thread receiveThread;
	private String addr;
	private int port;
	
	private int remainingBytes;
	private Bits formingPacket;
	
	private boolean isConnecting;
	
	public boolean isConnecting() {
		return isConnecting;
	}
	
	public String getAddress() {
		return addr;
	}
	public int getPort() {
		return port;
	}
	
	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}

	private void processData(Bits data) {
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
				onReceive(formingPacket.trim());
				
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
	
	private class SendThread implements Runnable {
		public byte[] data;
		public void run() {
			try {
				Bits finalData = new Bits();
				finalData.writeBit(data.length <= Byte.MAX_VALUE);
				if ( data.length <= Byte.MAX_VALUE ) {
					finalData.writeByte((byte)data.length);
				} else {
					finalData.writeShort((short)data.length);
				}
				finalData.writeBytes(data);
				socket.getOutputStream().write(finalData.readRemaining());
			} catch ( Exception e ) {
				onException(e, TCPEvent.SEND);
			}
		}
		
		public SendThread(byte[] data) {
			this.data = data;
		}
	}
	
	private class ReceiveThread implements Runnable {
		public void run() {
			try {
				while ( socket != null ) {
					int size = socket.getInputStream().available();
					if ( size > 0 ) {
						byte[] bytes = new byte[size];
						socket.getInputStream().read(bytes);
						processData(new Bits().writeBytes(bytes));
					}
				}
			} catch ( Exception e ) {
				onException(e, TCPEvent.RECEIVE);
			}
		}
	}
	private class ConnectThread implements Runnable {
		public String addr;
		public int port;
		public void run() {
			try {
				socket = new Socket(addr, port);
			} catch ( Exception e ) {
				onException(e, TCPEvent.CONNECT);
			}
			
			if ( socket != null && socket.isConnected() ) {
				TCPClient.this.addr = addr;
				TCPClient.this.port = port;
				onSocketConnect();
			} else {
				onException(new Exception("Failed connection"), TCPEvent.CONNECT);
			}
			isConnecting = false;
		}
		public ConnectThread(String addr, int port) {
			this.addr = addr;
			this.port = port;
		}
	}
	
	public void Connect(String addr, int port) {
		isConnecting = true;
		new Thread(new ConnectThread(addr, port)).start();
	}
	
	private void onSocketConnect() {
		receiveThread = new Thread(new ReceiveThread());
		receiveThread.start();
		
		onConnect();
	}
	
	public void Send(byte[] data) {
		new Thread(new SendThread(data)).start();
	}
	
	public abstract void onReceive(Bits data);
	public abstract void onConnect();
	public abstract void onException(Exception e, TCPEvent event);
	
	public void close() {
		try {
			socket.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		receiveThread = null;
		addr = "localhost";
		port = 27015;
	}
	
	public TCPClient() {
		socket = null;
		receiveThread = null;
		
		remainingBytes = 0;
		formingPacket = new Bits();
	}
}

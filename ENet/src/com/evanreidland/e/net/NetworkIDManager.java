package com.evanreidland.e.net;

import java.util.HashMap;

public class NetworkIDManager {
	private boolean bServer;
	private long lastID;
	
	public long newID() {
		return ++lastID;
	}
	
	public boolean isServer() {
		return bServer;
	}
	
	private HashMap<Long, PacketReceiver> receivers;
	
	public PacketReceiver getReceiver(long id) {
		return receivers.get(id);
	}
	
	public PacketReceiver addReceiver(PacketReceiver receiver) {
		receivers.put(receiver.getID(), receiver);
		return receiver;
	}
	
	public NetworkIDManager(boolean bServer) {
		this.bServer = bServer;
		lastID = 0;
	}
}

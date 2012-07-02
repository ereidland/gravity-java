package com.evanreidland.e.server;

import com.evanreidland.e.engine;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.TCPServer;

public class GravityServer extends TCPServer {
	public static GravityServer global;
	public void onDisconnect(long id) {
		broadcastData(id, ("Lost connection: " + getFullAddress(id)).getBytes());
	}
	
	public void onNewConnection(long id) {
		broadcastData(("New connection: " + getFullAddress(id)).getBytes());
	}

	public void onReceiveData(long id, Bits data) {
		engine.Log(getFullAddress(id) + ": " + new String(data.getBytes()));
		broadcastData(id, (id + ": " + new String(data.getBytes())).getBytes());
	}

	public void onListenException(Exception e) {
		engine.Log("Exception: " + e.getMessage());
	}

	public GravityServer() {
		global = this;
	}
}

package com.evanreidland.e.gravity;

import com.evanreidland.e.engine;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.TCPClient;
import com.evanreidland.e.net.TCPEvent;

public class GravityNetClient extends TCPClient {
	public static GravityNetClient global;
	public void onReceive(Bits data) {
		engine.Log(new String(data.getBytes()));
	}

	public void onConnect() {
		engine.Log("Connected!");
	}

	public void onException(Exception e, TCPEvent event) {
		engine.Log("Exception: " + e.getMessage() + " on event " + event.toString());
	}

	public GravityNetClient() {
		global = this;
	}
}

package com.evanreidland.e.client;

import com.evanreidland.e.engine;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.TCPClient;
import com.evanreidland.e.net.TCPEvent;
import com.evanreidland.e.shared.enums.MessageCode;
import com.evanreidland.e.shared.net.message;

public class GravityNetClient extends TCPClient {
	public static GravityNetClient global;
	public void onReceive(Bits data) {
		while ( data.getRemainingBits() >= 8 ) {
			MessageCode code = message.getCode(data.readByte());
			switch ( code ) {
				case MESSAGE:
					engine.Log("Message: " + data.readString());
					continue;
				default:
					break;
			}
		}
	}

	public void onConnect() {
		engine.Log("Connected!");
	}

	public void onException(Exception e, TCPEvent event) {
		engine.Log("Exception: " + e.getMessage() + " on event " + event.toString());
		e.printStackTrace();
	}

	public GravityNetClient() {
		global = this;
	}
}
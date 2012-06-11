package com.evanreidland.e.net.test2;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.evanreidland.e.net.ConnectionState;
import com.evanreidland.e.net.NetLog;
import com.evanreidland.e.net.Packet;
import com.evanreidland.e.net.UDPHandler;

public class ENetClient {
	private static class Client extends UDPHandler {
		public void onReceive(Packet p) {
			p.logInfo();
			String str = new String(p.data.getBytes());
			NetLog.Log("Client - Received from " + p.getSource().toString() + ":" + str);
		}
		
	}
	public static void main(String args[]) throws Exception {
		NetLog.addLogger(new NetLog.SystemOutLog());
		
		Client client = new Client();
		client.Connect("127.0.0.1", 27015);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while ( client.getState() == ConnectionState.CONNECTED ) {
			String str = in.readLine();
			client.Send(str.getBytes());
		}
	}
}

package com.evanreidland.e.client;

import com.evanreidland.e.net.Bits;
import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.shared.enums.MessageCode;
import com.evanreidland.e.shared.net.message;

public class clientfunctions {
	public static class Connect extends Function {

		public Value Call(Stack args) {
			if ( args.size() > 1 ) {
				String addr = args.at(0).toString();
				int port = args.at(1).toInt(27016);
				GravityNetClient.global.Connect(addr, port);
				return new Value("Connecting to " + addr + ":" + port + "...");
			}
			return new Value("Not enough arguments. Format: connect <address> <port>");
		}
		
		public Connect() {
			super("connect");
		}
	}
	
	public static class Send extends Function {
		public Value Call(Stack args) {
			if ( args.size() > 0 ) {
				String str = "";
				for ( int i = 0; i < args.size(); i++ ) {
					str += args.at(i).toString() + " ";
				}
				
				GravityNetClient.global.Send(new Bits().writeByte(message.toByte(MessageCode.MESSAGE)).writeBytes(str.getBytes()).readRemaining());
				return new Value();
			}
			return new Value("Not enough arguments. Format: send <data>");
		}
		
		public Send() {
			super("send");
		}
	}
	
	public static void registerAll(Stack env) {
		env.addFunction(new Connect());
		env.addFunction(new Send());
	}
}

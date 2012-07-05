package com.evanreidland.e.shared.console;

import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.server.GravityServer;

public class serverfunctions {
	public static class Listen extends Function {
		public Value Call(Stack args) {
			int port = 27016;
			if ( args.size() > 0 ) {
				port = args.at(0).toInt(port);
			}
			
			
			GravityServer.global.Listen(port);
			return new Value("Listening on port " + port + "...");
		}
		public Listen() {
			super("listen");
		}
	}
	public static void registerAll(Stack env) {
		env.addFunction(new Listen());
	}
}

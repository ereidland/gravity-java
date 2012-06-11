package com.evanreidland.e.net;

import java.util.Vector;

public class NetLog {
	public abstract static class Logger {
		public abstract void Log(String str);
	}
	
	public static class SystemOutLog extends Logger {
		public void Log(String str) {
			System.out.println(str);
		}
	}
	
	private static Vector<Logger> loggers = new Vector<Logger>();
	
	public static void addLogger(Logger logger) {
		if ( logger != null ) {
			loggers.add(logger);
		}
	}
	
	public static void Log(String str) {
		for ( int i = 0; i < loggers.size(); i++ ) {
			loggers.get(i).Log(str);
		}
	}
}

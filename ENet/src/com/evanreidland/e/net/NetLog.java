package com.evanreidland.e.net;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NetLog
{
	public static Logger logger = Logger.getLogger("com.evanreidland.e");
	
	public static void Log(String str)
	{
		logger.log(Level.INFO, str);
	}
}

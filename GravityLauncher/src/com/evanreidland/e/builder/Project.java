package com.evanreidland.e.builder;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evanreidland.e.launcher.GravityLauncherGUI;

public class Project
{
	public static Logger logger = Logger.getLogger("com.evanreidland.e");
	private Vector<String> sourceFiles;
	private String name;
	
	public String getName()
	{
		return name;
	}
	
	public void add(String source)
	{
		sourceFiles.add(source);
	}
	
	public void add(Vector<String> sourceFiles)
	{
		this.sourceFiles.addAll(sourceFiles);
	}
	
	public static class LogStream extends PrintWriter
	{
		private Logger logger;
		
		public void println(Object msg)
		{
			println(msg.toString());
		}
		
		public void println(String msg)
		{
			logger.log(Level.INFO, msg.toString());
		}
		
		public LogStream(Logger logger, String fout) throws Exception
		{
			super(fout);
			this.logger = logger;
		}
	}
	
	private static String getDefaultDirectory()
	{
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN"))
			return System.getenv("APPDATA");
		else if (OS.contains("MAC"))
			return System.getProperty("user.home")
					+ "/Library/Application Support";
		else if (OS.contains("NUX"))
			return System.getProperty("user.home");
		return System.getProperty("user.dir");
	}
	
	public static String defaultDirectory()
	{
		return getDefaultDirectory().replace('\\', '/');
	}
	
	public boolean buildClasses(String outputFolder)
	{
		int p = 0;
		String[] args = new String[sourceFiles.size() + p];
		
		for (int i = 0; i < sourceFiles.size(); i++)
		{
			args[i + p] = sourceFiles.get(i);
		}
		try
		{
			GravityLauncherGUI.Log("Building " + args.length + " files.");
			com.sun.tools.javac.Main.compile(args, new PrintWriter(
					new FileWriter(outputFolder + "/buildlog.txt")));
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Project(String name)
	{
		this.name = name;
		
		sourceFiles = new Vector<String>();
		
	}
}

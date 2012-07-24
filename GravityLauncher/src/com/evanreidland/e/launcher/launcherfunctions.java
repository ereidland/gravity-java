package com.evanreidland.e.launcher;

import java.util.logging.Level;

import com.evanreidland.e.builder.Project;
import com.evanreidland.e.ftp.DownloadInfo;
import com.evanreidland.e.ftp.download;
import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;

public class launcherfunctions
{
	public static DownloadInfo lastDownload = new DownloadInfo();
	
	public static class Print extends Function
	{
		public Value Call(Stack args)
		{
			String str = "";
			for (int i = 0; i < args.size(); i++)
			{
				String tstr = args.at(i).toString();
				if (tstr.length() > 0)
					str += tstr + " ";
			}
			return new Value(str);
		}
		
		public Print()
		{
			super("print");
		}
	}
	
	private static class DownloadThread implements Runnable
	{
		private String addr, to;
		private boolean isZip;
		
		public void run()
		{
			DownloadInfo info = lastDownload = isZip ? download.Extract(addr,
					to) : download.File(addr, to);
			GravityLauncherGUI.logger.log(info.success ? Level.INFO
					: Level.WARNING, info.success ? "Success! " : "Failure.");
			GravityLauncherGUI.Log(info.totalSize + " bytes extracted with "
					+ info.entryNames.size() + " entries.");
		}
		
		public DownloadThread(String addr, String to, boolean isZip)
		{
			this.addr = addr;
			this.to = to;
			this.isZip = isZip;
		}
		
	}
	
	public static class DownloadZip extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 0)
			{
				String to = args.context.get("path").toString();
				if (to.isEmpty())
					to = "./downloads/";
				String addr = args.at(0).toString();
				new Thread(new DownloadThread(addr, to, true)).start();
				return new Value("Downloading from \"" + addr + "\" to \"" + to
						+ "\"...");
			}
			else
			{
				return new Value(
						"Not enough arguments. Format: download <source (zip)>");
			}
		}
		
		public DownloadZip()
		{
			super("dl.zip");
		}
	}
	
	public static class Download extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 0)
			{
				String to = args.context.get("path").toString();
				if (to.isEmpty())
					to = "./downloads/";
				else if (!to.endsWith("/"))
					to += "/";
				String addr = args.at(0).toString();
				to += addr.substring(addr.lastIndexOf('/') + 1, addr.length());
				new Thread(new DownloadThread(addr, to, false)).start();
				return new Value("Downloading from \"" + addr + "\" to \"" + to
						+ "\"...");
			}
			else
			{
				return new Value(
						"Not enough arguments. Format: download <source>");
			}
		}
		
		public Download()
		{
			super("dl");
		}
	}
	
	public static class BuildDownload extends Function
	{
		public Value Call(Stack args)
		{
			if (lastDownload.success && !lastDownload.entryNames.isEmpty())
			{
				GravityLauncherGUI.Log("Setting up to build last download ("
						+ lastDownload.entryNames.size() + " entries).");
				
				Project project = new Project(lastDownload.entryNames.get(0));
				
				String to = args.context.get("path").toString();
				if (to.isEmpty())
					to = "./build/bin/";
				
				if (!to.endsWith("/"))
					to += "/";
				
				GravityLauncherGUI.Log("Output directory: " + to);
				
				for (int i = 0; i < lastDownload.entryNames.size(); i++)
				{
					String entry = lastDownload.entryNames.get(i);
					if (entry.endsWith(".java"))
					{
						GravityLauncherGUI.Log("Added " + entry);
						project.add(to + entry);
					}
				}
				
				project.buildClasses(to);
				
				return new Value("Done!");
			}
			else
			{
				return new Value(
						"Error: Could not build because the last download was unsuccessful.");
			}
		}
		
		public BuildDownload()
		{
			super("dl.build");
		}
	}
}

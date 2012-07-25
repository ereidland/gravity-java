package com.evanreidland.e.launcher;

import java.util.logging.Level;

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
			GravityLauncherGUI.Log(str);
			return new Value();
		}
		
		public Print()
		{
			super("print");
		}
	}
	
	private static class DownloadAction implements Runnable
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
		
		public DownloadAction(String addr, String to, boolean isZip)
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
				new DownloadAction(addr, to, true).run();
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
				new DownloadAction(addr, to, false).run();
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
	
}

package com.evanreidland.e.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
	
	public static class DownloadKeep extends Function
	{
		public Value Call(Stack args)
		{
			download.deleteOnExit = false;
			return new Value("Keeping downloads.");
		}
		
		public DownloadKeep()
		{
			super("dl.keep");
		}
	}
	
	public static class DownloadTemp extends Function
	{
		public Value Call(Stack args)
		{
			download.deleteOnExit = true;
			return new Value("Deleting downloads after exit.");
		}
		
		public DownloadTemp()
		{
			super("dl.temp");
		}
	}
	
	private static void Copy(String to, String local, File[] files)
	{
		if (files == null)
			return;
		byte[] buff = new byte[1024];
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].isFile())
			{
				try
				{
					String outName = to + files[i].getName(), inName = local
							+ files[i].getName();
					new File(outName.substring(0, outName.lastIndexOf('/')))
							.mkdir();
					FileOutputStream out = new FileOutputStream(outName);
					FileInputStream in = new FileInputStream(inName);
					int len;
					while ((len = in.read(buff)) > 0)
					{
						out.write(buff, 0, len);
					}
					in.close();
					out.close();
					System.out.println("Copied \"" + inName + "\" to \"" + to
							+ "\"");
				}
				catch (Exception e)
				{
					e.printStackTrace();
					
					System.out.println("Exception: " + e.toString());
				}
			}
			else if (files[i].isDirectory())
			{
				Copy(to + files[i].getName() + "/", local + files[i].getName()
						+ "/", files[i].listFiles());
			}
		}
	}
	
	public static class Copy extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 0)
			{
				String from = args.at(0).toString(), to;
				if (args.size() > 1)
					to = args.at(1).toString();
				else
					to = args.context.get("path").toString();
				
				to = to.replace('\\', '/');
				if (!to.endsWith("/"))
					to += "/";
				
				from = from.replace('\\', '/');
				if (!from.endsWith("/"))
					from += "/";
				
				Copy(to, from, new File(from).listFiles());
				
				return new Value("Copied from \"" + from + "\" to \"" + to
						+ "\"");
			}
			else
			{
				return new Value("Not enough arguments! Format: " + getName()
						+ " <from> <|to>");
			}
		}
		
		public Copy()
		{
			super("cp");
		}
	}
}

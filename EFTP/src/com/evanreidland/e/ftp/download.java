package com.evanreidland.e.ftp;

import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

public class download
{
	private static Class<? extends Downloader> downloaderClass = Downloader.class;
	
	public static void setDownloaderClass(
			Class<? extends Downloader> downloadClass)
	{
		downloaderClass = downloadClass;
	}
	
	private static class DownloadListThread implements Runnable
	{
		public String[] items;
		public String baseOutput;
		
		public void run()
		{
			for (int i = 0; i < items.length; i++)
			{
				Downloader d = download.newDownloader(items[i], baseOutput);
				d.downloadZip();
				d.onFinish(((float) i + 1) / (float) (items.length));
			}
		}
		
		public DownloadListThread(String address, String[] items,
				String baseOutput)
		{
			this.items = items;
			this.baseOutput = baseOutput;
		}
	}
	
	public static Downloader newDownloader(String from, String to)
	{
		try
		{
			Downloader d = (Downloader) downloaderClass.newInstance();
			d.setAddress(from);
			d.setOutputPath(to);
			return d;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static int downloadList(String address, String file,
			String baseOutput)
	{
		try
		{
			String[] split = file.split(" ");
			Vector<String> items = new Vector<String>();
			if (split.length > 1)
			{
				file = split[0];
				for (int i = 1; i < split.length; i++)
				{
					items.add(split[i]);
				}
			}
			InputStream r = new URL(address + file).openStream();
			StringBuilder s = new StringBuilder();
			int i = 0;
			while ((i = r.read()) != -1)
			{
				s.append((char) i);
			}
			String[] listItems = s.toString().split("\n");
			for (i = 0; i < listItems.length; i++)
			{
				items.add(i < listItems.length - 1 ? listItems[i].substring(0,
						listItems[i].length() - 1) : listItems[i]);
			}
			String[] listItemsFull = new String[items.size()];
			for (i = 0; i < items.size(); i++)
			{
				listItemsFull[i] = address + items.get(i);
			}
			new Thread(new DownloadListThread(address, listItemsFull,
					baseOutput)).start();
			return listItems.length;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Downloader d = newDownloader(address, baseOutput);
			if (d != null)
			{
				d.onError("Exception: " + e.getMessage());
			}
			else
			{
				System.out.println("Exception: " + e.getMessage()
						+ "\nError: Downloader class not set.");
			}
			return 0;
		}
	}
	
	public static DownloadInfo Extract(String address, String output)
	{
		DownloadInfo info = new DownloadInfo();
		Downloader d = newDownloader(address, output);
		if (d != null)
		{
			info = d.downloadZip();
		}
		return info;
	}
	
	public static DownloadInfo File(String address, String output)
	{
		DownloadInfo info = new DownloadInfo();
		Downloader d = newDownloader(address, output);
		if (d != null)
		{
			info = d.Download();
		}
		return info;
	}
}

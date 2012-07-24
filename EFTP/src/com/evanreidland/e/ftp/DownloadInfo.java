package com.evanreidland.e.ftp;

import java.util.Vector;

public class DownloadInfo
{
	
	public Vector<String> entryNames;
	public long totalSize;
	
	public boolean success;
	
	public DownloadInfo()
	{
		entryNames = new Vector<String>();
		totalSize = 0;
		success = false;
	}
}

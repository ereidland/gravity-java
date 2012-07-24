package com.evanreidland.e.launcher;

import com.evanreidland.e.ftp.Downloader;

public class LauncherDownloader extends Downloader
{
	public void onProgressChange(String curItem)
	{
		super.onProgressChange(curItem);
		GravityLauncherGUI.Log(curItem);
	}
	
	public void onError(String err)
	{
		super.onError(err);
		GravityLauncherGUI.Log("Error: " + err);
	}
}

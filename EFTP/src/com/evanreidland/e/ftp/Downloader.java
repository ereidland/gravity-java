package com.evanreidland.e.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Downloader {
	private String from;
	private String to;
	public String getAddress() {
		return from;
	}
	public String getOutputPath() {
		return to;
	}
	public void onProgressChange(String curItem) {
		System.out.println("Extracting: " + curItem);
	}
	public void onError(String err) {
		System.out.println(err);
	}
	
	public void onFinish(float prrogress) {
		System.out.println("Done extracting from, '" + from + "'");
	}
	public void Download() {
		try {
			int BUFFER = 2048;
			BufferedInputStream fis = new BufferedInputStream(new URL(from).openStream());
			
			new File(to).mkdir();

			BufferedOutputStream dest = null;
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				onProgressChange(entry.getName() + " (" + entry.getSize() + " bytes)");
				int count;
				byte data[] = new byte[BUFFER];
				try {
					new File(to + "/" + entry.getName().substring(0, entry.getName().lastIndexOf('/'))).mkdir();
				} catch ( Exception e ) {
					
				}
				FileOutputStream fos = new FileOutputStream(to + "/" + entry.getName());
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1 ) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			zis.close();
		} catch ( Exception e ) {
			e.printStackTrace();
			onError("Failed to download from, '" + from + "'");
		}
	}
	public void setAddress(String addr) {
		from = addr;
	}
	public void setOutputPath(String path) {
		to = path;
	}
	public Downloader() {
		from = "";
		to = "";
	}
}
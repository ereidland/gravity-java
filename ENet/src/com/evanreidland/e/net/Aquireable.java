package com.evanreidland.e.net;

import java.util.concurrent.Semaphore;

public class Aquireable
{
	private Semaphore semaphore;
	public static boolean on = true; // For experimentation.
	
	public boolean aquire()
	{
		if (on)
		{
			try
			{
				semaphore.acquire();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
	
	public void release()
	{
		if (!on)
			return;
		if (semaphore.availablePermits() <= 0)
		{
			semaphore.release();
		}
		else
		{
			System.out.println("Releasing ungrabbed Semaphore?");
		}
	}
	
	public Aquireable()
	{
		semaphore = new Semaphore(1, true);
	}
}

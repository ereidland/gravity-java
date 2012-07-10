package com.evanreidland.e.net.test3;

import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.ConnectionState;
import com.evanreidland.e.net.NetLog;
import com.evanreidland.e.net.Packet;
import com.evanreidland.e.net.UDPHandler;

public class ENetStressClient
{
	public static final long TEST_TIME = 5;
	
	private static class Client extends UDPHandler
	{
		public long lastTime = 0, sent = 0, received = 0, firstTime = 0;
		
		public void onReceive(Packet p)
		{
			p.logInfo();
			long time = p.data.readLong();
			if (time < lastTime)
			{
				NetLog.Log("Error: Packet out of order (" + time + ").");
			}
			lastTime = time;
			received++;
			
			float timePassed = (System.currentTimeMillis() - firstTime) / 1000f;
			
			if (timePassed > TEST_TIME)
			{
				NetLog.Log("Sent: " + sent + " Received: " + received
						+ " Dropped: " + (sent - received));
				if (timePassed != 0)
				{
					NetLog.Log("Sent per second: " + (sent / timePassed)
							+ " Dropped: " + ((sent - received) / timePassed));
				}
				Close();
			}
		}
		
	}
	
	public static void main(String args[]) throws Exception
	{
		Client client = new Client();
		client.Connect("127.0.0.1", 27015);
		
		client.firstTime = System.currentTimeMillis();
		
		while (client.getState() == ConnectionState.CONNECTED)
		{
			client.Send(new Bits().writeLong(System.currentTimeMillis())
					.getBytes());
			client.sent++;
			
			try
			{
				Thread.sleep(10);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}

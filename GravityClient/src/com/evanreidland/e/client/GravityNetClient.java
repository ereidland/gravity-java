package com.evanreidland.e.client;

import com.evanreidland.e.engine;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.TCPClient;
import com.evanreidland.e.net.TCPEvent;
import com.evanreidland.e.shared.enums.MessageCode;
import com.evanreidland.e.shared.net.message;

public class GravityNetClient extends TCPClient
{
	public static GravityNetClient global;
	
	public void onReceive(Bits data)
	{
		while (data.getRemainingBits() >= 8)
		{
			MessageCode code = message.getCode(data.readByte());
			switch (code)
			{
				case MESSAGE:
					engine.Log("Message: " + data.readString());
					continue;
				case ENT_NEW:
					long id = data.readLong();
					String className = data.readString();
					Entity ent = ents.createWithID(className, id);
					if (ent != null)
					{
						ent.setupFromBits(data);
						ent.flags.add("!spawned");
						engine.Log("Spawned " + className + "/" + id);
					}
					else
					{
						engine.Log("Invalid entity: " + className);
					}
				default:
					break;
			}
		}
	}
	
	public void onConnect()
	{
		engine.Log("Connected!");
	}
	
	public void onException(Exception e, TCPEvent event)
	{
		engine.Log("Exception: " + e.getMessage() + " on event "
				+ event.toString());
		e.printStackTrace();
	}
	
	public GravityNetClient()
	{
		global = this;
	}
}
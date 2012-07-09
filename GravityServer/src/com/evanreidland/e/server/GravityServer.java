package com.evanreidland.e.server;

import java.util.Vector;

import com.evanreidland.e.engine;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.event.ent.EntityDestroyedEvent;
import com.evanreidland.e.event.ent.EntitySpawnedEvent;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.TCPServer;
import com.evanreidland.e.shared.Player;
import com.evanreidland.e.shared.enums.MessageCode;
import com.evanreidland.e.shared.net.message;

//TODO: Move to GravityServer project and set it up.
public class GravityServer extends TCPServer
{
	public static GravityServer global;
	
	private Vector<Player> mPlayersToAddOnInit;
	private GravityServerGame gravityGame;
	
	public static void Log(String str)
	{
		GravityServerGUI.Log(str);
		
	}
	
	public void onDisconnect(long id)
	{
		Log("Server.onDisconnect: " + id + getFullAddress(id));
	}
	
	public void onNewConnection(long id)
	{
		Log("Server.onNewConnection: " + id + getFullAddress(id));
		if (gravityGame != null)
		{
			gravityGame.addNewPlayer(new Player(id));
		}
		else
		{
			mPlayersToAddOnInit.add(new Player(id));
		}
		sendAllEntities(id);
	}
	
	public void sendEntitySpawn(long to, Entity ent)
	{
		Bits bits = new Bits();
		bits.writeByte(message.toByte(MessageCode.ENT_NEW));
		bits.writeLong(ent.getID());
		bits.writeString(ent.getClassName());
		bits.write(ent.toBits());
		sendData(to, bits);
	}
	
	public void sendAllEntities(long id)
	{
		Bits bits = new Bits();
		for (int i = 0; i < ents.list.getSize(); i++)
		{
			sendEntitySpawn(id, ents.list.get(i));
		}
		
		Log("Num ents: " + ents.list.getSize());
		if (bits.getEnd() > 0)
		{
			sendData(id, bits);
		}
	}
	
	public void broadcastMessage(long ignoreID, String str)
	{
		broadcastData(ignoreID,
				new Bits().writeByte(message.toByte(MessageCode.MESSAGE))
						.writeString(str));
	}
	
	public void onReceiveData(long id, Bits data)
	{
		try
		{
			while (data.getRemainingBits() >= 8)
			{
				byte b = data.readByte();
				Log("Byte: " + b);
				MessageCode code = message.getCode(b);
				switch (code)
				{
					case MESSAGE:
						String message = getFullAddress(id) + ": "
								+ data.readString();
						Log(message);
						broadcastMessage(id, message);
						continue;
					default:
						break;
				}
			}
		}
		catch (Exception e)
		{
			StackTraceElement[] el = e.getStackTrace();
			Log(e.toString());
			for (int i = 0; i < el.length; i++)
			{
				Log(el[i].toString());
			}
		}
	}
	
	public void processCustom()
	{
		
	}
	
	public void onListenException(Exception e)
	{
		Log("Exception: " + e.getMessage());
	}
	
	public void onEntitySpawned(EntitySpawnedEvent event)
	{
	}
	
	public void onEntityDestroyed(EntityDestroyedEvent event)
	{
		
	}
	
	public void setupGame()
	{
		gravityGame = new GravityServerGame();
		
		for (Player player : mPlayersToAddOnInit)
		{
			gravityGame.addNewPlayer(player);
		}
		
		engine.game = gravityGame;
		engine.Initialize();
	}
	
	public void updateGame()
	{
		engine.Update();
	}
	
	public GravityServer()
	{
		mPlayersToAddOnInit = new Vector<Player>();
		global = this;
	}
}

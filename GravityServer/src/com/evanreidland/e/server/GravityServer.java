package com.evanreidland.e.server;

import java.util.Vector;
import java.util.logging.Level;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.roll;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.event.ent.EntityDestroyedEvent;
import com.evanreidland.e.event.ent.EntitySpawnedEvent;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.TCPServer;
import com.evanreidland.e.server.ent.ServerShip;
import com.evanreidland.e.shared.Player;
import com.evanreidland.e.shared.enums.MessageCode;

public class GravityServer extends TCPServer
{
	public static GravityServer global;
	
	private Vector<Player> players;
	private GravityServerGame gravityGame;
	
	public Player getPlayer(long id)
	{
		for (int i = 0; i < players.size(); i++)
		{
			Player player = players.get(i);
			if (player.getID() == id)
			{
				return player;
			}
		}
		return null;
	}
	
	public Player getPlayerOnShip(long shipID)
	{
		for (int i = 0; i < players.size(); i++)
		{
			Player player = players.get(i);
			if (player.getShipID() == shipID)
			{
				return player;
			}
		}
		return null;
	}
	
	public static void Log(String str)
	{
		GravityServerGUI.Log(str);
	}
	
	public void onDisconnect(long id)
	{
		engine.aquire();
		Log("Server.onDisconnect: " + id + getFullAddress(id));
		
		Player player = getPlayer(id);
		if (player != null)
		{
			Entity ent = ents.get(player.getShipID());
			if (ent != null)
			{
				ent.bDead = true;
			}
			players.remove(player);
		}
		engine.release();
	}
	
	public void onNewConnection(long id)
	{
		engine.aquire();
		Log("Server.onNewConnection: " + id + getFullAddress(id));
		
		players.add(new Player(id));
		
		Entity ent = ents.Create("ship");
		if (ent != null)
		{
			ent.pos.setAs(Vector3.RandomNormal().multipliedBy(
					roll.randomDouble(1, 2)));
			ent.flags.add("player targetable");
			
			Bits bits = new Bits();
			bits.write(getEntitySpawnBits(ent));
			bits.write(getShipForPlayerBits(id, ent));
			
			sendData(id, bits);
		}
		else
		{
			engine.logger.log(Level.SEVERE,
					"Entity type, \"ship\" does not exist!");
		}
		engine.release();
		
	}
	
	public Bits getEntitySpawnBits(Entity ent)
	{
		ent.bSent = true;
		Bits bits = new Bits();
		bits.writeByte(MessageCode.ENT_NEW.toByte());
		bits.writeLong(System.currentTimeMillis());
		bits.writeLong(ent.getID());
		bits.writeString(ent.getClassName());
		bits.write(ent.toBits());
		return bits;
	}
	
	public void sendEntitySpawn(long to, Entity ent)
	{
		sendData(to, getEntitySpawnBits(ent));
	}
	
	public void sendEntitySpawn(Entity ent)
	{
		broadcastData(getEntitySpawnBits(ent));
	}
	
	public void sendAllEntities(long id)
	{
		for (int i = 0; i < ents.list.size(); i++)
		{
			Log("In sendAllEntities");
			sendEntitySpawn(id, ents.list.get(i));
		}
	}
	
	public void sendEntityDeath(Entity ent)
	{
		Bits bits = new Bits();
		bits.writeByte(MessageCode.ENT_REM.toByte());
		bits.writeLong(ent.getID());
		
		broadcastData(bits);
	}
	
	public void sendEntityPos(Entity ent)
	{
		Bits bits = new Bits();
		bits.writeByte(MessageCode.ENT_UPDATE.toByte());
		bits.writeLong(Game.getTime());
		bits.writeLong(ent.getID());
		bits.write(ent.pos.toBits());
		bits.write(ent.vel.toBits());
		bits.write(ent.angle.toBits());
		bits.write(ent.angleVel.toBits());
		broadcastData(bits);
	}
	
	public Bits getShipForPlayerBits(long playerID, Entity ent)
	{
		Bits bits = new Bits();
		Player player = getPlayer(playerID);
		if (player != null && ent != null)
		{
			player.setPlayerShip(ent.getID());
			bits.writeByte(MessageCode.SELECT_SHIP.toByte());
			bits.writeLong(ent.getID());
		}
		else
		{
			if (player == null)
			{
				engine.logger.log(Level.SEVERE, "Player with ID " + playerID
						+ " does not exist!");
			}
			else
			{
				engine.logger.log(Level.SEVERE, "Entity does not exist!");
			}
		}
		return bits;
	}
	
	public void broadcastMessage(long ignoreID, String str)
	{
		broadcastData(
				ignoreID,
				new Bits().writeByte(MessageCode.MESSAGE.toByte()).writeString(
						str));
	}
	
	public void onReceiveData(long id, Bits data)
	{
		engine.aquire();
		try
		{
			while (data.getRemainingBits() >= 8)
			{
				byte b = data.readByte();
				MessageCode code = MessageCode.from(b);
				if (code == null)
					break;
				Player player;
				Entity ent;
				ServerShip ship;
				switch (code)
				{
					case MESSAGE:
						String message = getFullAddress(id) + ": "
								+ data.readString();
						Log(message);
						broadcastMessage(id, message);
						break;
					case ENT_UPDATETHRUST:
						player = getPlayer(id);
						if (player != null)
						{
							ent = ents.get(player.getShipID());
							if (ent != null)
							{
								Vector3 velThrust = Vector3.fromBits(data), angleThrust = Vector3
										.fromBits(data);
								ship = (ServerShip) ent;
								ship.velThrust.setAs(velThrust);
								ship.angleThrust.setAs(angleThrust);
							}
							else
							{
								Vector3.fromBits(data);
								Vector3.fromBits(data);
								engine.logger
										.log(Level.WARNING,
												"Player "
														+ id
														+ " tried to update thrust when they do not own a ship.");
							}
						}
						else
						{
							Vector3.fromBits(data);
							Vector3.fromBits(data);
							engine.logger.log(Level.SEVERE, "Player with ID "
									+ id + " does not exist!");
						}
						break;
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
		engine.release();
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
		
		engine.game = gravityGame;
		engine.Initialize();
	}
	
	public void updateGame()
	{
		engine.Update();
	}
	
	public GravityServer()
	{
		players = new Vector<Player>();
		global = this;
	}
}

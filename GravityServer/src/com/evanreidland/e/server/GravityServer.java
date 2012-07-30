package com.evanreidland.e.server;

import java.util.Vector;
import java.util.logging.Level;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.roll;
import com.evanreidland.e.action.Action;
import com.evanreidland.e.action.ActionListener;
import com.evanreidland.e.action.Permissions;
import com.evanreidland.e.action.act;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.event.ent.EntityDestroyedEvent;
import com.evanreidland.e.event.ent.EntitySpawnedEvent;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.MessageCode;
import com.evanreidland.e.net.TCPPacket;
import com.evanreidland.e.net.TCPServer;
import com.evanreidland.e.script.Variable;
import com.evanreidland.e.shared.Player;

public class GravityServer extends TCPServer implements ActionListener
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
	}
	
	public void onNewConnection(long id)
	{
		Log("Server.onNewConnection: " + id + getFullAddress(id));
		
		Player player = new Player(id, true);
		players.add(player);
		
		sendAllEntities(id);
		
		Entity ent = ents.Create("ship");
		if (ent != null)
		{
			ent.pos.setAs(Vector3.RandomNormal().multipliedBy(
					roll.randomDouble(1, 2)));
			ent.flags.add("player targetable");
			
			Permissions perms = player.permissions.get(ent.getID());
			perms.grant("ent_move");
			perms.grant("ent_stop");
			perms.grant("weapons");
			
			sendEntitySpawn(ent);
			
			Bits bits = new Bits();
			bits.write(getShipForPlayerBits(id, ent));
			sendData(id, bits);
		}
		else
		{
			engine.logger.log(Level.SEVERE,
					"Entity type, \"ship\" does not exist!");
		}
		
	}
	
	public Bits getEntitySpawnBits(Entity ent, Player player)
	{
		
		ent.bSent = true;
		Bits bits = new Bits();
		bits.writeByte(MessageCode.ENT_NEW.toByte());
		bits.writeLong(System.currentTimeMillis());
		bits.writeLong(ent.getID());
		bits.write(player.getTable().getBits(ent.getClassName()));
		bits.write(ent.toBits());
		return bits;
	}
	
	public void sendEntitySpawn(long to, Entity ent)
	{
		Player player = getPlayer(to);
		sendData(to, getEntitySpawnBits(ent, player));
	}
	
	public void sendEntitySpawn(Entity ent)
	{
		for (int i = 0; i < players.size(); i++)
		{
			sendEntitySpawn(players.get(i).getID(), ent);
		}
	}
	
	public void sendAllEntities(long id)
	{
		Player player = getPlayer(id);
		for (int i = 0; i < ents.list.size(); i++)
		{
			sendData(id, getEntitySpawnBits(ents.list.get(i), player));
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
	
	public void onStarted(Action action)
	{
		if (action.getActor() == null)
		{
			engine.Log("Action, \"" + action.getName() + "\" had a null actor!");
			return;
		}
		for (int i = 0; i < players.size(); i++)
		{
			Player player = players.get(i);
			Bits bits = new Bits();
			bits.writeByte(MessageCode.ACT_START.toByte());
			bits.writeLong(action.getActor().getID());
			bits.write(player.getTable().getBits(action.getName()));
			bits.write(action.toBits());
			
			sendData(player.getID(), bits);
		}
	}
	
	public void onReceive(long id, Bits data)
	{
		try
		{
			if (data.getRemainingBits() >= 8)
			{
				byte b = data.readByte();
				MessageCode code = MessageCode.from(b);
				if (code == null)
				{
					engine.Log("Null code!");
					return;
				}
				Player player = getPlayer(id);
				Entity ent;
				switch (code)
				{
					case MESSAGE:
						String message = getFullAddress(id) + ": "
								+ data.readString();
						Log(message);
						broadcastMessage(id, message);
						break;
					case ENT_SET_VAR:
						ent = ents.get(data.readLong());
						if (ent != null)
						{
							Permissions perm = player.permissions.get(
									ent.getID(), false);
							if (perm != null)
							{
								String varName = data.readString();
								Variable var = new Variable(varName);
								var.loadBits(data);
								
								if (perm.has("ent_set_allvars")
										|| perm.has("ent_set_var:"
												+ var.getName()))
								{
									ent.vars.add(var);
								}
								else
								{
									engine.logger
											.log(Level.WARNING,
													"During ENT_SET_VAR: Player "
															+ id
															+ " does not have permissions for variable "
															+ var.getName());
								}
							}
							else
							{
								engine.logger
										.log(Level.WARNING,
												"During ENT_SET_VAR: Player "
														+ id
														+ " does not have permissions for "
														+ ent.toString());
							}
						}
						else
						{
							engine.logger
									.log(Level.WARNING,
											"Player "
													+ id
													+ " tried to set var for an entity that did not exist!");
						}
						break;
					case ENT_SET_FLAG:
						ent = ents.get(data.readLong());
						if (ent != null)
						{
							Permissions perm = player.permissions.get(
									ent.getID(), false);
							if (perm != null)
							{
								String flagName = data.readString();
								boolean state = data.readBit();
								
								if (perm.has("ent_set_allflags")
										|| perm.has("ent_set_flag:" + flagName))
								{
									ent.flags.set(flagName, state);
								}
								else
								{
									engine.logger
											.log(Level.WARNING,
													"During ENT_SET_FLAG: Player "
															+ id
															+ " does not have permissions for flag "
															+ flagName);
								}
							}
							else
							{
								engine.logger
										.log(Level.WARNING,
												"During ENT_SET_FLAG: Player "
														+ id
														+ " does not have permissions for "
														+ ent.toString());
							}
						}
						else
						{
							engine.logger
									.log(Level.WARNING,
											"Player "
													+ id
													+ " tried to set flag for an entity that did not exist!");
						}
						break;
					case ACT_REQ:
						long actorID = data.readLong();
						ent = ents.get(actorID);
						if (ent != null)
						{
							String type = player.getTable().getString(data);
							Action action = act.Create(type);
							if (action != null)
							{
								action.loadBits(data);
								if (action.validate(player.permissions))
								{
									act.Start(ent, action);
								}
								else
								{
									engine.Log("Action \"" + type
											+ "\" failed to validate.");
								}
							}
							else
							{
								engine.Log("Action requested with undefined type: \""
										+ type + "\"");
							}
						}
						else
						{
							engine.Log("Action requested for null entity: "
									+ actorID);
						}
						break;
					default:
						engine.Log("Unused code: " + code.toString());
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
	
	public void Update()
	{
		engine.Update();
		TCPPacket packet;
		
		while ((packet = pull()) != null)
		{
			if (packet.isEvent())
			{
				switch (packet.getEvent())
				{
					case CONNECT:
						startListening(packet.getID());
						onNewConnection(packet.getID());
						break;
					case DISCONNECT:
						onDisconnect(packet.getID());
						break;
				}
			}
			else
			{
				onReceive(packet.getID(), packet.bits);
			}
		}
	}
	
	public GravityServer()
	{
		players = new Vector<Player>();
		global = this;
		
		act.addListener(this);
	}
}

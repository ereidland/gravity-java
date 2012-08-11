package com.evanreidland.e.client;

import java.util.logging.Level;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.action.Action;
import com.evanreidland.e.action.act;
import com.evanreidland.e.client.ent.ClientShip;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.MessageCode;
import com.evanreidland.e.net.StringTable;
import com.evanreidland.e.net.TCPClient;
import com.evanreidland.e.net.TCPEvent;
import com.evanreidland.e.net.TCPPacket;
import com.evanreidland.e.script.Variable;

public class GravityClient extends TCPClient
{
	public static GravityClient global;
	public static GravityGame game;
	public StringTable table;
	
	public void onReceive(Bits data)
	{
		try
		{
			if (data.getRemainingBits() >= 8)
			{
				byte b = data.readByte();
				if (b < 0)
				{
					switch (b)
					{
						case -1: // Kicked!
							String reason = data.readString();
							engine.Log("Kicked: " + reason);
							GravityGame.active.messageArea.addItem("Kicked: "
									+ reason);
							break;
						default:
							engine.Log("Unused internal code: " + b);
					}
				}
				else
				{
					MessageCode code = MessageCode.from(b);
					if (code == null)
					{
						engine.Log("Null code!");
						return;
					}
					Entity ent = null;
					long time, id;
					switch (code)
					{
						case MESSAGE:
							String str = data.readString();
							engine.Log("Message: " + str);
							GravityGame.active.messageArea.addItem(str);
							break;
						case ENT_NEW:
							time = data.readLong();
							id = data.readLong();
							String className = table.getString(data);
							ent = ents.createWithID(className, id);
							if (ent != null)
							{
								ent.loadBits(data);
								ent.bSpawned = false;
								ent.shiftByTimeOffset(time);
							}
							else
							{
								engine.logger.log(Level.SEVERE,
										"Invalid entity: " + className);
							}
							break;
						case ENT_REM:
							ent = ents.get(data.readLong());
							if (ent != null)
							{
								ent.bDead = true;
							}
							break;
						case ENT_UPDATE:
							time = data.readLong();
							ent = ents.get(data.readLong());
							if (ent != null)
							{
								ent.pos.setAs(Vector3.fromBits(data));
								ent.vel.setAs(Vector3.fromBits(data));
								ent.angle.setAs(Vector3.fromBits(data));
								ent.angleVel.setAs(Vector3.fromBits(data));
								ent.shiftByTimeOffset(time);
							}
							else
							{
								for (int i = 0; i < 4; i++)
								{
									Vector3.fromBits(data);
								}
								engine.logger
										.log(Level.WARNING,
												"Tried to update entity that did not exist!");
							}
							break;
						case ENT_SET_VAR:
							id = data.readLong();
							ent = ents.get(id);
							if (ent != null)
							{
								String varName = data.readString();
								Variable var = new Variable(varName);
								var.loadBits(data);
								ent.vars.add(var);
							}
							else
							{
								engine.logger
										.log(Level.WARNING,
												"Tried to set var for an entity that did not exist!");
							}
							break;
						case ENT_SET_FLAG:
							id = data.readLong();
							ent = ents.get(id);
							if (ent != null)
							{
								String flagName = data.readString();
								ent.flags.set(flagName, data.readBit());
							}
							else
							{
								engine.logger
										.log(Level.WARNING,
												"Tried to set flag for an entity that did not exist!");
							}
							break;
						case SELECT_SHIP:
							id = data.readLong();
							ent = ents.get(id);
							if (ent != null)
							{
								try
								{
									engine.Log("Player ship selected as "
											+ ent.toString());
									game.ship = (ClientShip) ent;
								}
								catch (Exception e)
								{
									e.printStackTrace();
									engine.logger
											.log(Level.SEVERE,
													"Target entity type, \""
															+ ent.getClassName()
															+ "\" should have been a ClientShip.");
								}
							}
							else
							{
								engine.logger.log(Level.SEVERE,
										"Target entity (" + id
												+ ") did not exist!");
							}
							break;
						case ACT_START:
							long actorID = data.readLong();
							String name = table.getString(data);
							ent = ents.get(actorID);
							Action action = act.Create(name);
							
							if (ent != null && action != null)
							{
								action.loadBits(data);
								act.Start(ent, action);
							}
							else
							{
								if (action != null)
								{
									engine.Log("DAYUM, " + name
											+ " must not exist.");
								}
								else
								{
									engine.Log("ID " + actorID
											+ " must not exist.");
								}
							}
							break;
						case VERSION:
							String version = data.readString();
							if (!version.equals(engine.getVersion()))
							{
								engine.logger.log(
										Level.SEVERE,
										"Version mismatch for server and client: "
												+ version + " != "
												+ engine.getVersion());
							}
							else
							{
								engine.Log("Matched version with server: "
										+ version);
							}
						default:
							engine.Log("Unused code: " + code.toString());
							break;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void requestAction(Entity actor, Action action)
	{
		Bits bits = new Bits();
		bits.writeByte(MessageCode.ACT_REQ.toByte());
		bits.writeLong(actor.getID());
		bits.write(table.getBits(action.getName(), false));
		bits.write(action.toBits());
		Send(bits);
	}
	
	public void onConnect()
	{
		engine.Log("Connected!");
		startListening();
		Send(new Bits().writeByte(MessageCode.VERSION.toByte()).writeString(
				engine.getVersion()));
	}
	
	public void Update()
	{
		try
		{
			TCPPacket packet;
			while ((packet = pull()) != null)
			{
				if (packet.isEvent())
				{
					switch (packet.getEvent())
					{
						case CONNECT:
							onConnect();
							break;
						case DISCONNECT:
							engine.Log("Disconnected!");
							break;
					}
				}
				else
				{
					onReceive(packet.bits);
				}
			}
		}
		catch (Exception e)
		{
			engine.logger.log(Level.SEVERE, "Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void onException(Exception e, TCPEvent event)
	{
		engine.Log("Exception: " + e.getMessage() + " on event "
				+ event.toString());
		e.printStackTrace();
		
		GravityClient.game.ship = null;
		close();
		
		ents.list.killAll();
	}
	
	public GravityClient(GravityGame game)
	{
		global = this;
		GravityClient.game = game;
		table = new StringTable(false);
	}
}
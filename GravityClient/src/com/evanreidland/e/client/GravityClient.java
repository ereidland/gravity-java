package com.evanreidland.e.client;

import java.util.logging.Level;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.action.Action;
import com.evanreidland.e.action.Actor;
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
				MessageCode code = MessageCode.from(data.readByte());
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
						engine.Log("Message: " + data.readString());
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
							engine.logger.log(Level.SEVERE, "Invalid entity: "
									+ className);
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
									.log(Level.SEVERE,
											"Tried to update entity that didn't exist!");
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
							engine.logger.log(Level.SEVERE, "Target entity ("
									+ id + ") did not exist!");
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
						break;
					default:
						engine.Log("Unused code: " + code.toString());
						break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void requestAction(Actor actor, Action action)
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
	}
	
	public GravityClient(GravityGame game)
	{
		global = this;
		GravityClient.game = game;
		table = new StringTable(false);
	}
}
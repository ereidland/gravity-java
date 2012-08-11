package com.evanreidland.e.server;

import java.util.Vector;
import java.util.logging.Level;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.roll;
import com.evanreidland.e.action.Permissions;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;
import com.evanreidland.e.shared.Player;

public class serverfunctions
{
	public static class Listen extends Function
	{
		public Value Call(Stack args)
		{
			int port = 27016;
			if (args.size() > 0)
			{
				port = args.at(0).toInt(port);
			}
			
			GravityServer.global.Listen(port);
			return new Value("Listening on port " + port + "...");
		}
		
		public Listen()
		{
			super("listen");
		}
	}
	
	public static class Print extends Function
	{
		public Value Call(Stack args)
		{
			String str = "";
			for (int i = 0; i < args.size(); i++)
			{
				str += args.at(i).toString() + " ";
			}
			return new Value(str);
		}
		
		public Print()
		{
			super("print");
		}
	}
	
	public static class StartGame extends Function
	{
		public Value Call(Stack args)
		{
			Value returnValue = new Script(args.context).Execute("listen "
					+ args.at(0).toString());
			GravityServer.global.setupGame();
			return new Value(returnValue.toString() + "and Starting game!");
		}
		
		public StartGame()
		{
			super("start");
		}
	}
	
	public static class Status extends Function
	{
		public Value Call(Stack args)
		{
			if (GravityServer.global.isListening())
			{
				engine.Log("Listenig on port " + GravityServer.global.getPort());
				Vector<Long> clients = GravityServer.global.getClients();
				if (clients.size() > 0)
				{
					engine.Log("Clients: " + clients.size());
					for (int i = 0; i < clients.size(); i++)
					{
						Player player = GravityServer.global.getPlayer(clients
								.get(i));
						engine.Log("ID: "
								+ player.getID()
								+ " Ship: "
								+ player.getShipID()
								+ " IP: "
								+ GravityServer.global.getFullAddress(player
										.getID()));
					}
				}
			}
			return new Value();
		}
		
		public Status()
		{
			super("status");
		}
	}
	
	public static class Kick extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 0)
			{
				long id = args.at(0).toLong();
				String reason = args.at(1).toString();
				if (GravityServer.global.getPlayer(id) != null)
				{
					String addr = GravityServer.global.getFullAddress(id);
					GravityServer.global.Kick(id, reason);
					GravityServer.global.broadcastMessage(0, "Kicked " + addr
							+ (reason.isEmpty() ? "" : " " + reason));
					return new Value("Kicked " + id + ": " + addr);
				}
				else
				{
					return new Value("Player with id " + id
							+ " does not exist.");
				}
			}
			else
			{
				return new Value("Not enough arguments. Format: " + getName()
						+ " <id> <|reason>");
			}
		}
		
		public Kick()
		{
			super("kick");
		}
	}
	
	public static class Respawn extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 0)
			{
				long id = args.at(0).toLong();
				Player player = GravityServer.global.getPlayer(id);
				if (player != null)
				{
					Entity ent = ents.Create("ship");
					if (ent != null)
					{
						ent.pos.setAs(Vector3.RandomNormal().multipliedBy(
								roll.randomDouble(1, 2),
								roll.randomDouble(1, 2), 0));
						ent.flags.add("player targetable");
						
						Permissions perms = player.permissions.get(ent.getID());
						perms.grant("ent_manmove");
						perms.grant("weapons");
						
						GravityServer.global.sendEntitySpawn(ent);
						
						Bits bits = new Bits();
						bits.write(GravityServer.global.getShipForPlayerBits(
								id, ent));
						GravityServer.global.sendData(id, bits);
						
						return new Value("Respawned " + id + "'s ship.");
					}
					else
					{
						engine.logger.log(Level.SEVERE,
								"Entity type, \"ship\" does not exist!");
						return new Value("Could not respawn ship.");
					}
				}
				else
				{
					return new Value("Player " + id + " does not exist.");
				}
			}
			else
			{
				return new Value("Not enough arguments. Format: " + getName()
						+ " <id>");
			}
		}
		
		public Respawn()
		{
			super("respawn");
		}
	}
	
	public static void registerAll(Stack env)
	{
		basefunctions.printFunction = new Print();
		env.registerFunctions(serverfunctions.class, true);
	}
}

package com.evanreidland.e.client;

import com.evanreidland.e.ent.ents;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.MessageCode;
import com.evanreidland.e.net.network;
import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;

public class clientfunctions
{
	public static class Connect extends Function
	{
		
		public Value Call(Stack args)
		{
			if (GravityClient.global != null
					&& GravityClient.global.isConnected())
			{
				return new Value("Already connected. Disconnect first with dc.");
			}
			if (args.size() > 0)
			{
				String addr = args.at(0).toString();
				int port = args.at(1).toInt(27016);
				if (GravityClient.global == null)
				{
					GravityClient.global = new GravityClient(GravityGame.active);
					network.setClient(GravityClient.global);
				}
				GravityClient.global.Connect(addr, port);
				return new Value("Connecting to " + addr + ":" + port + "...");
			}
			return new Value(
					"Not enough arguments. Format: connect <address> <|port>");
		}
		
		public Connect()
		{
			super("connect");
		}
	}
	
	public static class Send extends Function
	{
		public Value Call(Stack args)
		{
			if (GravityClient.global != null
					&& GravityClient.global.isConnected())
			{
				if (args.size() > 0)
				{
					String str = "";
					for (int i = 0; i < args.size(); i++)
						str += args.at(i).toString() + " ";
					
					GravityClient.global.Send(new Bits().writeByte(
							MessageCode.MESSAGE.toByte()).writeString(str));
					return new Value();
				}
				return new Value("Not enough arguments. Format: send <message>");
			}
			return new Value("Cannot send: not connected.");
		}
		
		public Send()
		{
			super("send");
		}
	}
	
	public static class FastStart extends Function
	{
		public Value Call(Stack args)
		{
			return new Script(args.context).Execute("connect localhost 27016");
		}
		
		public FastStart()
		{
			super("go");
		}
	}
	
	public static class Disconnect extends Function
	{
		public Value Call(Stack args)
		{
			if (GravityClient.global != null
					&& GravityClient.global.isConnected())
			{
				GravityClient.global.close();
				GravityClient.global = null;
				network.setClient(null);
				ents.list.killAll();
				return new Value("Disconnected.");
			}
			else
				return new Value("Not connected.");
		}
		
		public Disconnect()
		{
			super("dc");
		}
	}
	
	public static void registerAll(Stack env)
	{
		env.registerFunctions(clientfunctions.class, true);
		env.addFunction(new basefunctions.CallOther("disconnect",
				new Disconnect()));
	}
}

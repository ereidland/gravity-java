package com.evanreidland.e.server;

import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;

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
			Value returnValue = new Script(args.context).Execute("listen");
			GravityServer.global.setupGame();
			return new Value(returnValue.toString() + "and Starting game!");
		}
		
		public StartGame()
		{
			super("start");
		}
	}
	
	public static void registerAll(Stack env)
	{
		basefunctions.printFunction = new Print();
		env.addFunction(new Listen());
		env.addFunction(new StartGame());
	}
}

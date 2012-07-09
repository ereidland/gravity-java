package com.evanreidland.e.commands;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.graphics.font;
import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.script.basefunctions;

public class enginescript
{
	public static class Print extends Function
	{
		public Value Call(Stack args)
		{
			String str = ":";
			for (int i = 0; i < args.size(); i++)
			{
				str += args.at(i).toString() + " ";
			}
			if (str.length() > 1)
			{
				engine.Log(str);
			}
			return new Value();
		}
		
		public Print()
		{
			super("print");
		}
	}
	
	public static class Spawn extends Function
	{
		public Value Call(Stack args)
		{
			Entity ent = ents.Create(args.at(0).toString());
			
			Vector3 pos = new Vector3(args.at(1).toDouble(), args.at(2)
					.toDouble(), args.at(3).toDouble());
			if (ent == null)
			{
				return new Value("Entity class, '" + args.at(0).toString()
						+ "' does not exist.");
			}
			else
			{
				// TODO If server, send off this event. The event system could
				// be useful here, but I want your input first.
				ent.pos.setAs(pos);
				ent.Spawn();
				return new Value("Spawned " + ent.getClassName() + "/"
						+ ent.getID() + " @ " + ent.pos.toRoundedString());
			}
			
		}
		
		public Spawn()
		{
			super("spawn");
		}
	}
	
	public static class BuildFont extends Function
	{
		public Value Call(Stack args)
		{
			boolean bold = false;
			boolean antiAlias = false;
			int size = 10;
			if (args.size() > 3)
			{
				antiAlias = (args.at(3).toInt() == 1);
			}
			if (args.size() > 2)
			{
				bold = (args.at(2).toInt() == 1);
			}
			if (args.size() > 1)
			{
				size = args.at(1).toInt();
				if (size <= 0)
					size = 10;
			}
			String fname = args.at(0).toString();
			if (fname.length() > 0)
			{
				if (font.buildFont(fname, size, bold, antiAlias))
				{
					return new Value();
				}
				else
				{
					return new Value("Error building font.");
				}
			}
			return new Value("Could not build font: no name specified.");
		}
		
		public BuildFont()
		{
			super("build.font");
		}
	}
	
	public static void registerAll(Stack env)
	{
		basefunctions.printFunction = new Print();
		env.addFunction(new Spawn());
		env.addFunction(new basefunctions.CallOther("ent_create", new Spawn()));
		
		env.addFunction(new BuildFont());
		
		// TODO add more functions!
	}
}

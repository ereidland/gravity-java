package com.evanreidland.e.commands;

import java.util.Set;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.graphics.font;
import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.script.Variable;
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
				ent.pos.setAs(pos);
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
	
	public static class Version extends Function
	{
		public Value Call(Stack args)
		{
			return new Value(engine.getVersion());
		}
		
		public Version()
		{
			super("version");
		}
	}
	
	public static class EntSetVar extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 2)
			{
				long id = args.at(0).toLong();
				String name = args.at(1).toString();
				String value = args.at(2).toString();
				
				Entity ent = ents.get(id);
				if (ent != null)
				{
					ent.setNWVar(name, args.at(2));
					return new Value("Set " + ent.toString() + "/vars/" + name
							+ "/" + " to " + value);
				}
				else
				{
					return new Value("Entity " + id + " does not exist.");
				}
			}
			else
			{
				return new Value("Not enough arguments. Format: " + getName()
						+ " <id> <var> <value>");
			}
		}
		
		public EntSetVar()
		{
			super("ent.set");
		}
	}
	
	public static class EntGetVar extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 1)
			{
				long id = args.at(0).toLong();
				String name = args.at(1).toString();
				
				Entity ent = ents.get(id);
				if (ent != null)
					return ent.vars.get(name);
				else
					return new Value("Entity " + id + " does not exist.");
			}
			else
			{
				return new Value("Not enough arguments. Format: " + getName()
						+ " <id> <var>");
			}
		}
		
		public EntGetVar()
		{
			super("ent.get");
		}
	}
	
	public static class EntSetFlag extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 2)
			{
				long id = args.at(0).toLong();
				String name = args.at(1).toString();
				String value = args.at(2).toString();
				
				Entity ent = ents.get(id);
				if (ent != null)
				{
					ent.setNWFlag(name, args.at(2).toBool());
					return new Value("Set " + ent.toString() + "/flags/" + name
							+ "/" + " to " + value);
				}
				else
				{
					return new Value("Entity " + id + " does not exist.");
				}
			}
			else
			{
				return new Value("Not enough arguments. Format: " + getName()
						+ " <id> <flag>");
			}
		}
		
		public EntSetFlag()
		{
			super("ent.flag");
		}
	}
	
	public static class EntGetFlag extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 1)
			{
				long id = args.at(0).toLong();
				String name = args.at(1).toString();
				
				Entity ent = ents.get(id);
				if (ent != null)
					return new Value(ent.flags.get(name).toString());
				else
					return new Value("Entity " + id + " does not exist.");
			}
			else
			{
				return new Value("Not enough arguments. Format: " + getName()
						+ " <id> <flag>");
			}
		}
		
		public EntGetFlag()
		{
			super("ent.getflag");
		}
	}
	
	public static class LogThreads extends Function
	{
		public Value Call(Stack args)
		{
			Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
			Thread[] threadArray = threadSet.toArray(new Thread[threadSet
					.size()]);
			for (int i = 0; i < threadArray.length; i++)
			{
				engine.Log("Thread: " + threadArray[i].getName() + "/"
						+ threadArray[i].getId());
			}
			return new Value();
		}
		
		public LogThreads()
		{
			super("threads");
		}
	}
	
	public static void registerAll(Stack env)
	{
		basefunctions.printFunction = new Print();
		env.registerFunctions(enginescript.class, true);
		env.addFunction(new basefunctions.CallOther("ent_create", new Spawn()));
		
		env.add(new Variable("path", engine.getPath()));
	}
}

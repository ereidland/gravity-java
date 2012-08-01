package com.evanreidland.e.shared.console;

import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.script.text.Script;

public class sharedfunctions
{
	public static class RunScript extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 0)
			{
				return new Script(args.context).Execute("run.file "
						+ args.context.get("path").toString() + "scripts/"
						+ args.at(0).toString());
			}
			else
			{
				return new Value("Not enough arguments. Format: " + getName()
						+ " <file>");
			}
		}
		
		public RunScript()
		{
			super("run");
		}
	}
	
	public static void registerAll(Stack env)
	{
		env.registerFunctions(sharedfunctions.class, true);
	}
}

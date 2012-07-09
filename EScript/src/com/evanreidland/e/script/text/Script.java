package com.evanreidland.e.script.text;

import java.util.Vector;

import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.script.Variable;

public class Script
{
	public Stack env;
	
	public static String[] Split(String str)
	{
		Vector<String> split = new Vector<String>();
		
		StringBuilder cur = new StringBuilder();
		char inQuote = 0;
		for (int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if (c == '\n' || (c == ' ' && inQuote == 0))
			{
				if (cur.length() > 0)
				{
					split.add(cur.toString());
					cur = new StringBuilder();
				}
				inQuote = 0;
			}
			else if ((c == '"' || c == '\''))
			{
				if (inQuote == 0)
				{
					if (cur.length() > 0)
					{
						split.add(cur.toString());
						cur = new StringBuilder();
					}
					inQuote = c;
					cur.append('$');
				}
				else if (inQuote == c)
				{
					if (cur.length() > 0)
					{
						split.add(cur.toString());
						cur = new StringBuilder();
					}
					inQuote = 0;
				}
				else
				{
					cur.append(c);
				}
			}
			else
			{
				cur.append(c);
			}
		}
		
		if (cur.length() > 0)
		{
			split.add(cur.toString());
		}
		
		String[] ret = new String[split.size()];
		for (int i = 0; i < split.size(); i++)
		{
			ret[i] = split.get(i);
		}
		return ret;
	}
	
	public Variable getArg(String base)
	{
		char c = base.charAt(0);
		try
		{
			if (c == '@')
			{
				return env.get(base.substring(1, base.length()).toLowerCase());
			}
			else if (c == '$')
			{
				return new Variable("_l", base.substring(1, base.length()));
			}
			else if ((c == '-' && !base.contains(" "))
					|| (c >= '0' && c <= '9') || c == '.')
			{
				if (base.contains("."))
				{
					return new Variable("_l", Double.valueOf(base));
				}
				else
				{
					return new Variable("_l", Integer.valueOf(base));
				}
			}
			else
			{
				return new Variable("_l", base);
			}
		}
		catch (Exception e)
		{
			return new Variable("_l", base);
		}
	}
	
	public Stack fromArgs(String[] split)
	{
		Stack stack = new Stack();
		if (split.length > 1)
		{
			for (int i = 1; i < split.length; i++)
			{
				Variable v = getArg(split[i]);
				if (v.getName().equals("_l"))
				{
					stack.addValue(new Value(v));
				}
				else
				{
					stack.add(v);
				}
			}
		}
		stack.context = env;
		return stack;
	}
	
	public Stack newStack()
	{
		Stack s = new Stack();
		s.context = env;
		return s;
	}
	
	public Value Execute(String line)
	{
		String[] split = Split(line);
		if (split.length > 0)
		{
			Stack s = fromArgs(split);
			Function f = env.getFunction(split[0].toLowerCase());
			
			if (f.getName() != "_null")
			{
				return f.Call(s);
			}
			else
			{
				return new Value("Error: Function not found, \""
						+ split[0].toLowerCase() + "\".");
			}
		}
		return new Value();
	}
	
	public Script(Stack env)
	{
		this.env = env;
	}
	
	public Script()
	{
		this(new Stack());
	}
}

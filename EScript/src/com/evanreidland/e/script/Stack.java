package com.evanreidland.e.script;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Vector;

public class Stack
{
	private Vector<Variable> vars;
	private Vector<Function> funcs;
	
	public Stack context;
	
	public Variable at(int index)
	{
		return (index >= 0 && index < vars.size()) ? vars.get(index)
				: ((index < 0 && index >= -vars.size()) ? at(index
						+ vars.size()) : new Variable("_null"));
	}
	
	public Function functionAt(int index)
	{
		return (index >= 0 && index < funcs.size()) ? funcs.get(index)
				: ((index < 0 && index >= -funcs.size()) ? functionAt(index
						+ funcs.size()) : new Function.Null());
	}
	
	public boolean hasVar(String name)
	{
		for (int i = 0; i < vars.size(); i++)
		{
			if (vars.get(i).getName().equals(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean hasFunction(String name)
	{
		for (int i = 0; i < funcs.size(); i++)
		{
			if (funcs.get(i).getName().equals(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public Variable get(String name)
	{
		name = name.toLowerCase();
		for (int i = 0; i < vars.size(); i++)
		{
			if (vars.get(i).getName().equals(name))
			{
				return vars.get(i);
			}
		}
		return new Variable("_null");
	}
	
	public Function getFunction(String name)
	{
		for (int i = 0; i < funcs.size(); i++)
		{
			if (funcs.get(i).getName().equals(name))
			{
				return funcs.get(i);
			}
		}
		return new Function.Null();
	}
	
	public Variable add(Variable var)
	{
		if (!hasVar(var.getName()))
		{
			vars.add(var);
		}
		else
		{
			for (int i = 0; i < vars.size(); i++)
			{
				Variable lvar = vars.get(i);
				if (lvar.getName().equals(var.getName()))
				{
					lvar.set(var);
					break;
				}
			}
		}
		
		return var;
	}
	
	public Variable addValue(Value v)
	{
		Variable var = new Variable("_i" + vars.size(), v);
		// System.out.println(var.getName() + ":" + var.toString() + ", " +
		// var.getType().toString());
		vars.add(var);
		return var;
	}
	
	public Function addFunction(Function func)
	{
		if (!hasFunction(func.getName()))
		{
			funcs.add(func);
		}
		else
		{
			for (int i = 0; i < funcs.size(); i++)
			{
				if (funcs.get(i).getName().equals(func.getName()))
				{
					funcs.set(i, func);
					break;
				}
			}
		}
		
		return func;
	}
	
	public int size()
	{
		return vars.size();
	}
	
	public int numFunctions()
	{
		return funcs.size();
	}
	
	public Stack()
	{
		vars = new Vector<Variable>();
		funcs = new Vector<Function>();
		
		context = null;
	}
	
	public Stack(Value singleValue)
	{
		this();
		addValue(singleValue);
	}
	
	// If explicit is not set to true it will also add the static class
	// methods.
	public void registerFunctions(Class<?> from, boolean explicit)
	{
		Class<?>[] classes = from.getDeclaredClasses();
		
		for (int i = 0; i < classes.length; i++)
		{
			try
			{
				if (Function.class.isAssignableFrom(classes[i])
						&& classes[i].getConstructors().length > 0)
				{
					Constructor<?>[] cons = classes[i].getConstructors();
					for (int j = 0; j < cons.length; j++)
					{
						if (cons[j].getParameterTypes().length == 0)
						{
							addFunction((Function) cons[j].newInstance());
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if (!explicit)
		{
			// TODO code.
		}
	}
	
	public void registerFunctions(Object object)
	{
		if (object instanceof Class<?>)
		{
			registerFunctions((Class<?>) object, false);
		}
		else
		{
			// TODO code.
		}
	}
	
	public void addFields(Object object, String prefix)
	{
		Field[] fields = object.getClass().getFields();
		try
		{
			for (int i = 0; i < fields.length; i++)
			{
				Variable var = ObjectVariable.Create(object, fields[i], prefix);
				if (var != null)
				{
					add(var);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void addFields(Object object)
	{
		addFields(object, "");
	}
}

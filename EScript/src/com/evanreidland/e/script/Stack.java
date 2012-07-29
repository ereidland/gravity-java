package com.evanreidland.e.script;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Vector;

import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.StringTable;

public class Stack implements Bitable
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
		for (int i = 0; i < vars.size(); i++)
		{
			Variable lvar = vars.get(i);
			if (lvar.getName().equals(var.getName()))
			{
				lvar.set(var);
				return var;
			}
		}
		
		vars.add(var);
		
		return var;
	}
	
	public Variable remove(String name)
	{
		for (int i = 0; i < vars.size(); i++)
		{
			Variable lvar = vars.get(i);
			if (lvar.getName().equals(name))
			{
				return vars.remove(i);
			}
		}
		return null;
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
	
	// Note: only exports variables at the present.
	public Bits toBits()
	{
		Bits bits = new Bits();
		bits.writeSize(vars.size());
		for (int i = 0; i < vars.size(); i++)
		{
			Variable var = vars.get(i);
			bits.writeString(var.getName());
			bits.write(var.toBits());
		}
		return bits;
	}
	
	public Bits toBits(StringTable table, boolean create)
	{
		Bits bits = new Bits();
		bits.writeSize(vars.size());
		for (int i = 0; i < vars.size(); i++)
		{
			Variable var = vars.get(i);
			bits.write(table.getBits(var.getName(), create));
			bits.write(var.toBits());
		}
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		int size = (int) bits.readSize();
		for (int i = 0; i < size; i++)
		{
			Variable var = new Variable(bits.readString());
			var.loadBits(bits);
			add(var);
		}
	}
	
	public void loadBits(Bits bits, StringTable table)
	{
		int size = (int) bits.readSize();
		for (int i = 0; i < size; i++)
		{
			Variable var = new Variable(table.getString(bits));
			var.loadBits(bits);
			add(var);
		}
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
					remove(var.getName());
					vars.add(var);
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

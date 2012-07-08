package com.evanreidland.e.script;

public class Variable extends Value
{
	private String name;

	public String getName()
	{
		return name;
	}

	public Variable(String name)
	{
		super();
		this.name = name;
	}

	public Variable(String name, String value)
	{
		super(value);
		this.name = name;
	}

	public Variable(String name, int value)
	{
		super(value);
		this.name = name;
	}

	public Variable(String name, double value)
	{
		super(value);
		this.name = name;
	}

	public Variable(String name, Value value)
	{
		super(value);
		this.name = name.toLowerCase();
	}

	public static class Constant extends Variable
	{
		public Value setString(String value)
		{
			return this;
		}

		public Value setInt(int value)
		{
			return this;
		}

		public Value setDouble(double value)
		{
			return this;
		}

		public Value set(Value other)
		{
			return this;
		}

		public Constant(String name, Value value)
		{
			super(name);
			super.set(value);
		}
	}
}

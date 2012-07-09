package com.evanreidland.e.script;

public class Value
{
	public static enum Type
	{
		String, Int, Long, Double, Null, // Never supposed to happen.
	}
	
	protected Object object;
	protected Type type;
	
	public Type getType()
	{
		return type;
	}
	
	public Object getObject()
	{
		return object;
	}
	
	protected void onChange()
	{
		
	}
	
	public int toInt(int def)
	{
		try
		{
			switch (type)
			{
				case String:
					return Integer.valueOf((String) object).intValue();
				case Int:
					return ((Integer) object).intValue();
				case Long:
					return ((Long) object).intValue();
				case Double:
					return ((Double) object).intValue();
				case Null:
					return def;
			}
		}
		catch (Exception e)
		{
			return def;
		}
		return def;
	}
	
	public int toInt()
	{
		return toInt(0);
	}
	
	public long toLong(long def)
	{
		try
		{
			switch (type)
			{
				case String:
					return Integer.valueOf((String) object).longValue();
				case Int:
					return ((Integer) object).longValue();
				case Long:
					return ((Long) object).longValue();
				case Double:
					return ((Double) object).longValue();
				case Null:
					return def;
			}
		}
		catch (Exception e)
		{
			return def;
		}
		return def;
	}
	
	public long toLong()
	{
		return toLong(0);
	}
	
	public double toDouble(double def)
	{
		try
		{
			switch (type)
			{
				case String:
					return Double.valueOf((String) object).doubleValue();
				case Int:
					return ((Integer) object).doubleValue();
				case Long:
					return ((Long) object).doubleValue();
				case Double:
					return ((Double) object).doubleValue();
				case Null:
					return def;
			}
		}
		catch (Exception e)
		{
			return def;
		}
		return def;
	}
	
	public double toDouble()
	{
		return toDouble(0);
	}
	
	public boolean toBool(boolean def)
	{
		if (type == Type.Null)
			return def;
		String str = toString().toLowerCase();
		int in = toInt(-1);
		return (in == 1 || in == 0)
				|| (str.equals("true") || str.equals("false")) || def;
	}
	
	public boolean toBool()
	{
		return toBool(false);
	}
	
	public short toShort(short def)
	{
		return (short) toInt(def);
	}
	
	public short toShort()
	{
		return (short) toShort((short) 0);
	}
	
	public byte toByte(byte def)
	{
		return (byte) toInt(def);
	}
	
	public byte toByte()
	{
		return toByte((byte) 0);
	}
	
	public float toFloat(float def)
	{
		return (float) toDouble(def);
	}
	
	public float toFloat()
	{
		return toFloat(0);
	}
	
	public String toString()
	{
		try
		{
			switch (type)
			{
				case String:
					return object != null ? (String) object : "";
				case Int:
					return ((Integer) object).toString();
				case Double:
					return ((Double) object).toString();
				case Null:
					return "";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(); // TODO Remove this line after thorough
									// testing.
			return "";
		}
		return "";
	}
	
	public Value setString(String value)
	{
		type = Type.String;
		object = value;
		onChange();
		return this;
	}
	
	public Value setInt(int value)
	{
		type = Type.Int;
		object = (Integer) value;
		onChange();
		return this;
	}
	
	public Value setLong(long value)
	{
		type = Type.Long;
		object = (Long) value;
		onChange();
		return this;
	}
	
	public Value setDouble(double value)
	{
		type = Type.Double;
		object = (Double) value;
		onChange();
		return this;
	}
	
	public Value setFloat(float value)
	{
		return setDouble(value);
	}
	
	public Value setShort(short value)
	{
		return setInt(value);
	}
	
	public Value setByte(byte value)
	{
		return setInt(value);
	}
	
	public Value setBool(boolean value)
	{
		return setInt(value ? 1 : 0);
	}
	
	public Value set(Value other)
	{
		type = other.type;
		object = other.object;
		onChange();
		return this;
	}
	
	public Value(String value)
	{
		setString(value);
	}
	
	public Value(int value)
	{
		setInt(value);
	}
	
	public Value(double value)
	{
		setDouble(value);
	}
	
	public Value(long value)
	{
		setLong(value);
	}
	
	public Value(Value other)
	{
		set(other);
	}
	
	public Value()
	{
		type = Type.Null;
		object = null;
	}
}

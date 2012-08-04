package com.evanreidland.e.script;

import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;

public class Value implements Bitable
{
	public static enum Type
	{
		String(4), Double(3), Long(2), Int(1), Null(0);
		
		public final int level;
		
		public static Type highType(Type a, Type b)
		{
			return a.level > b.level ? a : b;
		}
		
		public static Type highNumberType(Type a, Type b)
		{
			Type highType = highType(a, b);
			if (highType == String)
				highType = Double;
			return highType;
		}
		
		Type(int level)
		{
			this.level = level;
		}
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
		return (in == 1 || str.equals("true")) ? true : (in == 0 || str
				.equals("false")) ? false : def;
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
					return String.valueOf(toInt());
				case Long:
					return String.valueOf(toLong());
				case Double:
					return String.valueOf(toDouble());
				case Null:
					return "";
			}
		}
		catch (Exception e)
		{
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
	
	public boolean equals(Value other)
	{
		Type highType = Type.highType(type, other.type);
		switch (highType)
		{
			case String:
				return toString().equals(other.toString());
			case Int:
				return toInt() == other.toInt();
			case Long:
				return toLong() == other.toLong();
			case Double:
				return toDouble() == other.toDouble();
			case Null:
				return true;
		}
		return false;
	}
	
	public boolean lt(Value other)
	{
		Type highType = Type.highNumberType(type, other.type);
		switch (highType)
		{
			case Int:
				return toInt() < other.toInt();
			case Long:
				return toLong() < other.toLong();
			case Double:
				return toDouble() < other.toDouble();
		}
		return false;
	}
	
	public boolean gt(Value other)
	{
		Type highType = Type.highNumberType(type, other.type);
		switch (highType)
		{
			case Int:
				return toInt() > other.toInt();
			case Long:
				return toLong() > other.toLong();
			case Double:
				return toDouble() > other.toDouble();
		}
		return false;
	}
	
	public boolean lte(Value other)
	{
		Type highType = Type.highNumberType(type, other.type);
		switch (highType)
		{
			case Int:
				return toInt() <= other.toInt();
			case Long:
				return toLong() <= other.toLong();
			case Double:
				return toDouble() <= other.toDouble();
		}
		return false;
	}
	
	public boolean gte(Value other)
	{
		Type highType = Type.highNumberType(type, other.type);
		switch (highType)
		{
			case Int:
				return toInt() >= other.toInt();
			case Long:
				return toLong() >= other.toLong();
			case Double:
				return toDouble() >= other.toDouble();
		}
		return false;
	}
	
	public boolean compare(Value other, String op)
	{
		if (op.equals("<") || op.equals("lt"))
			return lt(other);
		else if (op.equals(">") || op.equals("gt"))
			return gt(other);
		else if (op.equals("<=") || op.equals("lte"))
			return lte(other);
		else if (op.equals(">=") || op.equals("gte"))
			return gte(other);
		else if (op.equals("==") || op.equals("is"))
			return equals(other);
		else if (op.equals("!=") || op.equals("isnot"))
			return !equals(other);
		return false;
	}
	
	public Value setDouble(double value)
	{
		type = Type.Double;
		object = (Double) value;
		onChange();
		return this;
	}
	
	public Bits toBits()
	{
		Bits bits = new Bits();
		switch (type)
		{
			case Double:
				bits.writeSmallByte(1, 2);
				bits.writeDouble(toDouble());
				break;
			case Long:
				bits.writeSmallByte(2, 2);
				bits.writeLong(toLong());
				break;
			case String:
				bits.writeSmallByte(3, 2);
				bits.writeString(toString());
				break;
			case Int:
			default:
				bits.writeSmallByte(0, 2);
				bits.writeInt(toInt());
				break;
		}
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		byte small = bits.readSmallByte(2);
		switch (small)
		{
			case 0:
				setInt(bits.readInt());
				break;
			case 1:
				setDouble(bits.readDouble());
				break;
			case 2:
				setLong(bits.readLong());
				break;
			case 3:
				setString(bits.readString());
				break;
		}
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

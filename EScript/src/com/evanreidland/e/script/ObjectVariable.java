package com.evanreidland.e.script;

import java.lang.reflect.Field;

public class ObjectVariable extends Variable
{
	public static enum AltType
	{
		Boolean, Byte, Short, Float, None
	}
	
	private Object holdingObject;
	
	private Field field;
	
	private AltType altType;
	
	public AltType getAltType()
	{
		return altType;
	}
	
	public Object getHoldingObject()
	{
		return holdingObject;
	}
	
	public void updateObject()
	{
		try
		{
			
			if (altType == AltType.None)
			{
				object = field.get(holdingObject);
			}
			else
			{
				switch (altType)
				{
					case Boolean:
						object = (Integer) (field.getBoolean(holdingObject) ? 1
								: 0);
						break;
					case Byte:
						object = Integer.valueOf(field.getByte(holdingObject));
						break;
					case Short:
						object = Integer.valueOf(field.getShort(holdingObject));
						break;
					case Float:
						object = Double.valueOf(field.getFloat(holdingObject));
						break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Value setString(String value)
	{
		try
		{
			if (altType == AltType.None)
			{
				switch (type)
				{
					case Int:
						field.set(holdingObject, Integer.valueOf(value));
						break;
					case Long:
						field.set(holdingObject, Long.valueOf(value));
						break;
					case Double:
						field.set(holdingObject, Double.valueOf(value));
						break;
					case String:
						field.set(holdingObject, value);
						break;
				}
			}
			else
			{
				Value v = new Value(value);
				switch (altType)
				{
					case Boolean:
						field.set(holdingObject, v.toBool());
						break;
					case Byte:
						field.set(holdingObject, v.toByte());
						break;
					case Short:
						field.set(holdingObject, v.toShort());
						break;
					case Float:
						field.set(holdingObject, v.toFloat());
						break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		updateObject();
		onSet();
		return this;
	}
	
	public Value setInt(int value)
	{
		try
		{
			if (altType == AltType.None)
			{
				switch (type)
				{
					case Int:
						field.set(holdingObject, value);
						break;
					case Long:
						field.set(holdingObject, (int) value);
						break;
					case Double:
						field.set(holdingObject, (double) value);
						break;
					case String:
						field.set(holdingObject, String.valueOf(value));
						break;
				}
			}
			else
			{
				switch (altType)
				{
					case Boolean:
						field.set(holdingObject, value == 1);
						break;
					case Byte:
						field.set(holdingObject, (byte) value);
						break;
					case Short:
						field.set(holdingObject, (short) value);
						break;
					case Float:
						field.set(holdingObject, (float) value);
						break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		updateObject();
		onSet();
		return this;
	}
	
	public Value setLong(long value)
	{
		try
		{
			if (altType == AltType.None)
			{
				switch (type)
				{
					case Int:
						field.set(holdingObject, (int) value);
						break;
					case Long:
						field.set(holdingObject, value);
						break;
					case Double:
						field.set(holdingObject, (double) value);
						break;
					case String:
						field.set(holdingObject, String.valueOf(value));
						break;
				}
			}
			else
			{
				switch (altType)
				{
					case Boolean:
						field.set(holdingObject, value == 1);
						break;
					case Byte:
						field.set(holdingObject, (byte) value);
						break;
					case Short:
						field.set(holdingObject, (short) value);
						break;
					case Float:
						field.set(holdingObject, (float) value);
						break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		updateObject();
		onSet();
		return this;
	}
	
	public Value setDouble(double value)
	{
		try
		{
			if (altType == AltType.None)
			{
				switch (type)
				{
					case Int:
						field.set(holdingObject, (int) value);
						break;
					case Long:
						field.set(holdingObject, (long) value);
						break;
					case Double:
						field.set(holdingObject, value);
						break;
					case String:
						field.set(holdingObject, String.valueOf(value));
						break;
				}
			}
			else
			{
				switch (altType)
				{
					case Boolean:
						field.set(holdingObject, value == 1);
						break;
					case Byte:
						field.set(holdingObject, (byte) value);
						break;
					case Short:
						field.set(holdingObject, (short) value);
						break;
					case Float:
						field.set(holdingObject, (float) value);
						break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		updateObject();
		onSet();
		return this;
	}
	
	public Value set(Value other)
	{
		switch (other.type)
		{
			case Int:
				setInt(other.toInt());
			case Double:
				setDouble(other.toDouble());
			case Long:
				setLong(other.toLong());
			case String:
				setString(other.toString());
		}
		return this;
	}
	
	public int toInt(int def)
	{
		updateObject();
		return super.toInt(def);
	}
	
	public long toLong(long def)
	{
		updateObject();
		return super.toLong(def);
	}
	
	public double toDouble(double def)
	{
		updateObject();
		return super.toDouble(def);
	}
	
	public String toString()
	{
		updateObject();
		return super.toString();
	}
	
	private ObjectVariable(Object object, Field field, String prefix)
			throws Exception
	{
		super(prefix + field.getName());
		this.holdingObject = object;
		this.object = null;
		this.field = field;
		this.altType = AltType.None;
		
		Class<?> type = field.getType();
		if (type.isPrimitive())
		{
			
			if (type.equals(boolean.class))
			{
				this.type = Value.Type.Int;
				altType = AltType.Boolean;
				setInt(field.getBoolean(object) ? 1 : 0);
			}
			else if (type.equals(byte.class))
			{
				this.type = Value.Type.Int;
				altType = AltType.Byte;
				setInt(field.getByte(object));
			}
			else if (type.equals(short.class))
			{
				this.type = Value.Type.Int;
				altType = AltType.Short;
				setInt(field.getShort(object));
			}
			else if (type.equals(int.class))
			{
				this.type = Value.Type.Int;
				setInt(field.getInt(object));
			}
			else if (type.equals(float.class))
			{
				this.type = Value.Type.Double;
				altType = AltType.Float;
				setDouble(field.getFloat(object));
			}
			else if (type.equals(double.class))
			{
				this.type = Value.Type.Double;
				setDouble(field.getDouble(object));
			}
			else if (type.equals(long.class))
			{
				this.type = Value.Type.Long;
				setLong(field.getLong(object));
			}
			
		}
		else if (String.class.isAssignableFrom(type))
		{
			this.type = Value.Type.String;
			if (object == null)
			{
				setString("");
			}
		}
		else
		{
			throw new Exception();
		}
		
		// System.out.println("Added " + getName() + "/" + getType().toString()
		// + (altType != AltType.None ? "." + altType.toString() : "")
		// + "/" + toString());
	}
	
	public static ObjectVariable Create(Object object, String name,
			String prefix)
	{
		try
		{
			return Create(object, object.getClass().getField(name), prefix);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static ObjectVariable Create(Object object, Field field,
			String prefix)
	{
		try
		{
			return new ObjectVariable(object, field, prefix);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}

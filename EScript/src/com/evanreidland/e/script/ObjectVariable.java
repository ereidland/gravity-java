package com.evanreidland.e.script;

import java.lang.reflect.Field;

public class ObjectVariable extends Variable
{
	private Object holdingObject;
	
	Field field;
	
	public Object getHoldingObject()
	{
		return holdingObject;
	}
	
	public void updateObject()
	{
		try
		{
			object = field.get(holdingObject);
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
			switch (type)
			{
				case Int:
					field.set(holdingObject, Integer.valueOf(value));
					break;
				case Double:
					field.set(holdingObject, Double.valueOf(value));
					break;
				case String:
					field.set(holdingObject, value);
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		updateObject();
		return this;
	}
	
	public Value setInt(int value)
	{
		try
		{
			switch (type)
			{
				case Int:
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
		catch (Exception e)
		{
			e.printStackTrace();
		}
		updateObject();
		return this;
	}
	
	public Value setDouble(double value)
	{
		try
		{
			switch (type)
			{
				case Int:
					field.set(holdingObject, (int) value);
					break;
				case Double:
					field.set(holdingObject, value);
					break;
				case String:
					field.set(holdingObject, String.valueOf(value));
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		updateObject();
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
			case String:
				setString(other.toString());
		}
		return this;
	}
	
	private ObjectVariable(Object object, Field field, String prefix)
			throws Exception
	{
		super(prefix + field.getName());
		this.holdingObject = object;
		this.object = field.get(object);
		this.field = field;
		
		Class<?> type = field.getType();
		if (type.isPrimitive())
		{
			if (type.equals(int.class))
			{
				this.type = Value.Type.Int;
			}
			else if (type.equals(double.class))
			{
				this.type = Value.Type.Double;
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

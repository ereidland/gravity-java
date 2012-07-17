package com.evanreidland.e;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.StringTable;

public class Flags implements Bitable
{
	public static enum State
	{
		True, False, Either, Undef;
	}
	
	public static class Flag
	{
		State state;
		
		public State get()
		{
			return state;
		}
		
		public void set(State state)
		{
			this.state = state;
		}
		
		public Flag()
		{
			this.state = State.Undef;
		}
		
		public Flag(State state)
		{
			this.state = state;
		}
	}
	
	public static class ObjectFlag extends Flag
	{
		private boolean useBoolean;
		private Object object;
		private Field field;
		
		public State get()
		{
			try
			{
				return useBoolean ? (field.getBoolean(object) ? State.True
						: State.False) : (State) field.get(object);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return State.Undef;
			}
		}
		
		public void set(State state)
		{
			this.state = state;
			try
			{
				if (useBoolean)
				{
					field.set(object, state == State.True);
				}
				else
				{
					field.set(object, state);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public ObjectFlag(Object object, Field field) throws Exception
		{
			this.object = object;
			this.field = field;
			
			if (boolean.class.isAssignableFrom(field.getType()))
			{
				useBoolean = true;
			}
			else if (State.class.isAssignableFrom(field.getType()))
			{
				useBoolean = false;
			}
			else
			{
				throw new Exception(
						"Invalid field type (required: boolean or State enum) "
								+ object.getClass().toString() + "."
								+ field.getName());
			}
		}
		
		public ObjectFlag(Object object, String field) throws Exception
		{
			this(object, object.getClass().getField(field));
		}
	}
	
	public static Bits stateToBits(State state)
	{
		Bits bits = new Bits();
		if (state == State.True)
		{
			bits.writeBit(true);
		}
		else
		{
			bits.writeBit(false);
			bits.writeBit(state == State.False);
		}
		return bits;
	}
	
	public static State stateFromBits(Bits bits)
	{
		boolean state = bits.readBit();
		if (state)
		{
			return State.True;
		}
		else
		{
			if (bits.readBit())
			{
				return State.False;
			}
			else
			{
				return State.False;
			}
		}
	}
	
	private HashMap<String, Flag> flags;
	
	// Note: Basic match. Defaults to true. Only returns false if there is a
	// true/false mismatch.
	public boolean matchesOther(Flags oflags)
	{
		if (oflags != null)
		{
			Object[] okeySet = flags.keySet().toArray();
			String[] keySet = new String[okeySet.length];
			
			for (int i = 0; i < keySet.length; i++)
			{
				keySet[i] = (String) okeySet[i];
				State a = get(keySet[i]), b = oflags.get(keySet[i]);
				if ((a == State.False && b == State.True)
						|| (a == State.True && b == State.False))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public String[] getFlagNames()
	{
		Object[] ostrings = flags.keySet().toArray();
		String[] strings = new String[ostrings.length];
		for (int i = 0; i < ostrings.length; i++)
		{
			strings[i] = (String) ostrings[i];
		}
		return strings;
	}
	
	public String[] getFlagCodes()
	{
		String[] flagNames = getFlagNames();
		String[] res = new String[flagNames.length];
		
		for (int i = 0; i < flagNames.length; i++)
		{
			State state = get(flagNames[i]);
			res[i] = (state == State.False ? "!" : state == State.Either ? "|"
					: "") + flagNames[i];
		}
		return res;
	}
	
	public boolean matchesOther(Flags oflags, boolean strict)
	{
		if (!strict)
		{
			return matchesOther(oflags);
		}
		if (oflags != null)
		{
			Object[] keySet = oflags.flags.keySet().toArray();
			
			for (int i = 0; i < keySet.length; i++)
			{
				if (get((String) keySet[i]) != oflags.get((String) keySet[i]))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public State get(String name)
	{
		Flag s = flags.get(name);
		if (s != null)
		{
			return s.get();
		}
		return State.Undef;
	}
	
	public boolean getBoolState(String name)
	{
		return get(name) == State.True;
	}
	
	public Flag getFlag(String name)
	{
		name = name.toLowerCase();
		Flag flag = flags.get(name);
		if (flag == null)
		{
			flag = new Flag();
			flags.put(name, flag);
		}
		return flag;
	}
	
	public State set(String name, State state)
	{
		name = name.toLowerCase();
		Flag fstate = flags.get(name);
		if (fstate != null)
		{
			fstate.set(state);
		}
		else
		{
			flags.put(name, new Flag(state));
		}
		return state;
	}
	
	public State set(String name, boolean state)
	{
		State s = state ? State.True : State.False;
		set(name, s);
		return s;
	}
	
	public State setFlag(String name, Flag flag)
	{
		name = name.toLowerCase();
		flags.put(name, flag);
		return flag.get();
	}
	
	public Bits toBits()
	{
		Bits bits = new Bits();
		String[] flagNames = getFlagNames();
		bits.writeShort((short) flagNames.length);
		for (int i = 0; i < flagNames.length; i++)
		{
			bits.writeString(flagNames[i]);
			bits.write(stateToBits(get(flagNames[i])));
		}
		return bits;
	}
	
	public Bits toBits(StringTable ref, boolean create)
	{
		Bits bits = new Bits();
		String[] flagNames = getFlagNames();
		bits.writeShort((short) flagNames.length);
		for (int i = 0; i < flagNames.length; i++)
		{
			bits.write(ref.getBits(flagNames[i], create));
			bits.write(stateToBits(get(flagNames[i])));
		}
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		short count = bits.readShort();
		for (int i = 0; i < count; i++)
		{
			String str = bits.readString();
			State state = stateFromBits(bits);
			set(str, state);
		}
	}
	
	public void setFromBits(Bits bits, StringTable ref)
	{
		short count = bits.readShort();
		for (int i = 0; i < count; i++)
		{
			String str = ref.getString(bits);
			State state = stateFromBits(bits);
			set(str, state);
		}
		
	}
	
	public State toggleState(String name)
	{
		Flag flag = flags.get(name);
		if (flag != null)
		{
			State s = flag.get();
			flag.set((s == State.True) ? State.False
					: (s == State.False) ? State.True : s);
			return flag.get();
		}
		return State.Undef;
	}
	
	// Format: [symbol][code]. ! for false, empty for true and | for either.
	// e.g.: !canFly
	public void add(String code)
	{
		if (code.contains(" "))
		{
			add(code.split(" "));
		}
		else
		{
			if (code.length() == 0)
				return;
			int type = 0;
			if (code.charAt(0) == '!')
			{
				type = 1;
			}
			else if (code.charAt(0) == '|')
			{
				type = 2;
			}
			
			set(type > 0 ? code.substring(1, code.length()) : code,
					type == 0 ? State.True : type == 1 ? State.False
							: State.Either);
		}
	}
	
	public void add(String[] codes)
	{
		for (int i = 0; i < codes.length; i++)
		{
			add(codes[i]);
		}
	}
	
	public void add(Flags other)
	{
		add(other.getFlagCodes());
	}
	
	public void add(Flags[] flags)
	{
		for (int i = 0; i < flags.length; i++)
		{
			add(flags[i]);
		}
	}
	
	public void addFromObject(Object object, String name)
	{
		addFromObject(object, name, name);
	}
	
	public boolean addFromObject(Object object, String name, String flagname)
	{
		try
		{
			setFlag(flagname, new ObjectFlag(object, name));
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public Flags()
	{
		flags = new HashMap<String, Flag>();
	}
	
	public Flags(String state)
	{
		this();
		add(state.split(" "));
	}
	
	public Flags(String[] list)
	{
		this();
		add(list);
	}
	
	public Flags(Flags[] flags)
	{
		this();
		add(flags);
	}
}

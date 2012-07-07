package com.evanreidland.e;

import java.util.HashMap;

public class Flags
{
	public static enum State
	{
		True, False, Either, Undef
	}

	private HashMap<String, State> flags;

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
				State a = getState(keySet[i]), b = oflags.getState(keySet[i]);
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
			State state = getState(flagNames[i]);
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
				if (getState((String) keySet[i]) != oflags
						.getState((String) keySet[i]))
				{
					return false;
				}
			}
		}
		return true;
	}

	public State getState(String name)
	{
		State s = flags.get(name);
		if (s != null)
		{
			return s;
		}
		return State.Undef;
	}

	public State setState(String name, State state)
	{
		flags.put(name, state);
		return state;
	}

	public State setState(String name, boolean state)
	{
		State s = state ? State.True : State.False;
		flags.put(name, s);
		return s;
	}

	public State toggleState(String name)
	{
		State s = flags.get(name);
		if (s != null)
		{
			setState(name, (s == State.True) ? State.False
					: (s == State.False) ? State.True : s);
			return s;
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

			setState(type > 0 ? code.substring(1, code.length()) : code,
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

	public Flags()
	{
		flags = new HashMap<String, State>();
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

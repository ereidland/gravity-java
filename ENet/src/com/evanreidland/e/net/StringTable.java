package com.evanreidland.e.net;

import java.util.HashMap;

//Added this to be useful for strings that are used many times.
public class StringTable
{
	private short lastID;
	private boolean isOrigin;
	
	public class MappedString
	{
		private short id;
		private String str;
		public boolean sent;
		
		public short getID()
		{
			return id;
		}
		
		public String getString()
		{
			return str;
		}
		
		public MappedString(String str)
		{
			this.id = ++lastID;
			this.str = str;
			sent = false;
		}
		
		public MappedString(short forcedID, String str)
		{
			this.id = forcedID;
			this.str = str;
			sent = false;
		}
	}
	
	private HashMap<String, MappedString> mapped;
	private HashMap<Short, MappedString> mappedID;
	
	public Short[] getMappedIDs()
	{
		return (Short[]) mappedID.keySet().toArray(new Short[] {});
	}
	
	public MappedString getMappedString(String str, boolean create)
	{
		MappedString mappedStr = mapped.get(str);
		if (mappedStr == null && isOrigin && create)
		{
			mappedStr = new MappedString(str);
			addMappedString(mappedStr);
		}
		return mappedStr;
	}
	
	public MappedString getMappedString(String str)
	{
		return getMappedString(str, true);
	}
	
	public MappedString getMappedString(short id)
	{
		return mappedID.get(id);
	}
	
	public String get(short id)
	{
		MappedString mappedStr = getMappedString(id);
		return mappedStr != null ? mappedStr.getString() : "";
	}
	
	public Bits getBits(String str, boolean create)
	{
		MappedString mappedStr = getMappedString(str, create);
		Bits bits = new Bits();
		if (mappedStr != null)
		{
			bits.writeBit(mappedStr.sent);
			if (!mappedStr.sent)
			{
				bits.writeBit(true);
			}
			bits.writeShort(mappedStr.getID());
			if (!mappedStr.sent)
			{
				bits.writeString(mappedStr.getString());
				mappedStr.sent = true;
			}
		}
		else
		{
			bits.writeBit(false).writeBit(false).writeString(str);
		}
		return bits;
	}
	
	public Bits getBits(String str)
	{
		return getBits(str, true);
	}
	
	private void addMappedString(MappedString str)
	{
		mapped.put(str.getString(), str);
		mappedID.put(str.getID(), str);
	}
	
	private void addWithID(short id, String str)
	{
		addMappedString(new MappedString(id, str));
	}
	
	public void add(String str)
	{
		getMappedString(str);
	}
	
	public void addList(String[] str)
	{
		for (int i = 0; i < str.length; i++)
		{
			add(str[i]);
		}
	}
	
	public void addList(String str)
	{
		addList(str.split(" "));
	}
	
	public String getString(Bits bits)
	{
		if (bits.readBit()) // Sent?
		{
			return get(bits.readShort());
		}
		else
		{
			if (bits.readBit()) // Defined with id?
			{
				short id = bits.readShort();
				String str = bits.readString();
				if (!isOrigin)
				{
					NetLog.Log("StringTable << " + id + "/" + str);
					MappedString mappedStr = new MappedString(id, str);
					mappedStr.sent = true;
					addMappedString(mappedStr);
				}
				return str;
			}
			else
			{
				return bits.readString();
			}
		}
	}
	
	public Bits toBits()
	{
		Bits bits = new Bits();
		Short[] mappedIDs = getMappedIDs();
		bits.writeShort((short) mappedIDs.length);
		NetLog.Log("length: " + mappedIDs.length);
		for (int i = 0; i < mappedIDs.length; i++)
		{
			MappedString str = mappedID.get(mappedIDs[i]);
			bits.writeShort(str.getID());
			bits.writeString(str.getString());
		}
		return bits;
	}
	
	public void setupFromBits(Bits bits)
	{
		short count = bits.readShort();
		for (int i = 0; i < count; i++)
		{
			short id = bits.readShort();
			String str = bits.readString();
			NetLog.Log("StringTable << " + id + "/" + str);
			addWithID(id, str);
		}
	}
	
	public StringTable(boolean isOrigin)
	{
		lastID = 0;
		this.isOrigin = isOrigin;
		mapped = new HashMap<String, MappedString>();
		mappedID = new HashMap<Short, MappedString>();
	}
	
	public StringTable(StringTable other, boolean isOrigin, boolean sentStatus)
	{
		this(isOrigin);
		this.isOrigin = other.isOrigin;
		lastID = other.lastID;
		Short[] mappedIDs = other.getMappedIDs();
		for (int i = 0; i < mappedIDs.length; i++)
		{
			MappedString otherStr = other.getMappedString(mappedIDs[i]);
			MappedString str = new MappedString(otherStr.getID(),
					otherStr.getString());
			str.sent = sentStatus;
			
			mapped.put(str.getString(), str);
			mappedID.put(mappedIDs[i], str);
		}
	}
}

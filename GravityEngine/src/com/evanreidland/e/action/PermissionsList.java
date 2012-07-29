package com.evanreidland.e.action;

import java.util.HashMap;

import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;

public class PermissionsList implements Bitable
{
	private HashMap<Long, Permissions> permissions;
	
	// Note: -1 is the key for default permissions. 0 should be used for
	// general permissions.
	public Permissions get(long id, boolean create)
	{
		Permissions perm = permissions.get(id);
		if (perm == null && create)
		{
			perm = new Permissions(permissions.get(-1L));
			permissions.put(id, perm);
		}
		return perm;
	}
	
	public Permissions get(long id)
	{
		return get(id, true);
	}
	
	public boolean remove(long id)
	{
		if (id == -1)
		{
			permissions.put(-1L, new Permissions());
			return false;
		}
		else
		{
			return permissions.remove(id) != null;
		}
	}
	
	public boolean isGranted(String name, long id)
	{
		Permissions perm = get(id, false);
		if (perm != null)
		{
			return perm.has(name);
		}
		return false;
	}
	
	public boolean isDenied(String name, long id)
	{
		return !isGranted(name, id);
	}
	
	public Permissions getDefault()
	{
		return get(-1);
	}
	
	public Permissions getGeneral()
	{
		return get(0);
	}
	
	public Bits toBits()
	{
		Bits bits = new Bits();
		Long[] keys = (Long[]) permissions.keySet().toArray(
				new Long[permissions.size()]);
		bits.writeSize(keys.length);
		for (int i = 0; i < keys.length; i++)
		{
			bits.writeLong(keys[i]);
			bits.write(get(keys[i]).toBits());
		}
		
		return bits;
	}
	
	public void loadBits(Bits bits)
	{
		int size = (int) bits.readSize();
		for (int i = 0; i < size; i++)
		{
			long id = bits.readLong();
			get(id).loadBits(bits);
		}
	}
	
	public PermissionsList()
	{
		permissions = new HashMap<Long, Permissions>();
		permissions.put(-1L, new Permissions());
	}
}

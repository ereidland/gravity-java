package com.evanreidland.e.action;

import java.util.HashMap;
import java.util.Vector;

import com.evanreidland.e.net.Bitable;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.StringTable;

public class Permissions implements Bitable
{
	public boolean hasIfUndefined;
	private HashMap<String, Permission> permissionsMap;
	private Vector<Permission> permissions;
	
	public Permission set(String permission, boolean status)
	{
		Permission perm = permissionsMap.get(permission);
		if (perm != null)
		{
			perm.state = status;
		}
		else
		{
			perm = new Permission(permission, status);
			permissionsMap.put(permission, perm);
			permissions.add(perm);
		}
		return perm;
	}
	
	public Permission grant(String permission)
	{
		return set(permission, true);
	}
	
	public Permission revoke(String permission)
	{
		return set(permission, false);
	}
	
	public void addID(String permission, long id)
	{
		Permission perm = permissionsMap.get(permission);
		if (perm != null)
		{
			perm.addID(id);
		}
	}
	
	public boolean hasID(String permission, long id)
	{
		Permission perm = permissionsMap.get(permission);
		if (perm != null)
		{
			return perm.hasID(id);
		}
		return false;
	}
	
	public void removeID(String permission, long id)
	{
		Permission perm = permissionsMap.get(permission);
		if (perm != null)
		{
			perm.addID(id);
		}
	}
	
	public Permission get(String permission, boolean create, boolean state)
	{
		Permission perm = permissionsMap.get(permission);
		return perm != null ? perm : create ? new Permission(permission, state)
				: null;
	}
	
	public Permission get(String permission, boolean create)
	{
		return get(permission, create, true);
	}
	
	public Permission get(String permission)
	{
		return get(permission, false);
	}
	
	public void remove(String permission)
	{
		Permission perm = get(permission);
		if (perm != null)
		{
			permissionsMap.remove(permission);
			permissions.remove(perm);
		}
	}
	
	public boolean hasDefined(String permission)
	{
		return permissionsMap.get(permission) != null;
	}
	
	public boolean has(String permission)
	{
		Permission perm = permissionsMap.get(permission);
		return perm != null ? perm.state : hasIfUndefined;
	}
	
	public Vector<Permission> getList()
	{
		return new Vector<Permission>(permissions);
	}
	
	public Bits toBits()
	{
		Bits bits = new Bits();
		bits.writeSize(permissions.size());
		for (int i = 0; i < permissions.size(); i++)
		{
			Permission perm = permissions.get(i);
			bits.writeString(perm.getName());
			bits.write(perm.toBits());
		}
		
		return bits;
	}
	
	public Bits toBits(StringTable table, boolean create)
	{
		Bits bits = new Bits();
		bits.writeSize(permissions.size());
		for (int i = 0; i < permissions.size(); i++)
		{
			Permission perm = permissions.get(i);
			bits.write(table.getBits(perm.getName(), create));
			bits.write(perm.toBits());
		}
		
		return bits;
	}
	
	public Bits toBits(StringTable table)
	{
		return toBits(table, true);
	}
	
	public void loadBits(Bits bits)
	{
		int count = (int) bits.readSize();
		
		for (int i = 0; i < count; i++)
		{
			Permission perm = grant(bits.readString());
			perm.loadBits(bits);
		}
	}
	
	public void loadBits(Bits bits, StringTable table)
	{
		int count = (int) bits.readSize();
		
		for (int i = 0; i < count; i++)
		{
			Permission perm = grant(table.getString(bits));
			perm.loadBits(bits);
		}
	}
	
	public Permissions()
	{
		hasIfUndefined = false;
		permissionsMap = new HashMap<String, Permission>();
		permissions = new Vector<Permission>();
	}
}

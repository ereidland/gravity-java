package com.evanreidland.e.shared;

import com.evanreidland.e.action.Permissions;
import com.evanreidland.e.net.StringTable;

public class Player
{
	private StringTable table;
	private long id;
	private long shipID;
	
	public Permissions permissions;
	
	public StringTable getTable()
	{
		return table;
	}
	
	public Player(long id, boolean bServer)
	{
		this.id = id;
		this.shipID = 0;
		this.table = new StringTable(bServer);
		permissions = new Permissions();
	}
	
	public long getID()
	{
		return id;
	}
	
	public long getShipID()
	{
		return shipID;
	}
	
	public void setPlayerShip(long shipID)
	{
		this.shipID = shipID;
	}
}

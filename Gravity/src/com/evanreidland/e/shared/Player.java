package com.evanreidland.e.shared;

public class Player
{
	
	private long id;
	private long shipID;
	
	public Player(long id)
	{
		this.id = id;
		this.shipID = 0;
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

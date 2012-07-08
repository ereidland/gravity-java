package com.evanreidland.e.shared;

import com.evanreidland.e.shared.ent.Ship;

public class Player {
	
	private long mServerId;
	private Ship mOwnedShip;
	
	
	public Player(long serverId) {
		this.mServerId = serverId;
	}
	
	public long getServerId() {
		return mServerId;
	}
	
	public void setPlayerShip(Ship ship) {
		mOwnedShip = ship;
	}
}

package com.evanreidland.e.shared.enums;

public enum MessageCode
{
	ENT_NEW(), // New entity.
	ENT_REM(), // Delete entity.
	ENT_UPDATE(), // Update entity position and velocity.
	ENT_UPDATETHRUST(), // Update entity thrust.
	ENT_TARGET(), // Sent bits directly to an entity. Currently unimplemented.
	SELECT_SHIP(), // Select a player's ship.
	MESSAGE(), // Chat message.
	ACT_START(), // Started action.
	ACT_REQ(); // Request action for unit.
	
	public byte toByte()
	{
		return (byte) ordinal();
	}
	
	public static MessageCode from(byte b)
	{
		return b >= 0 && b < values().length ? values()[b] : null;
	}
}

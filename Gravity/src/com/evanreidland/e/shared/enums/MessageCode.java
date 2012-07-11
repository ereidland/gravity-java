package com.evanreidland.e.shared.enums;

public enum MessageCode
{
	ENT_NEW(), // New entity.
	ENT_REM(), // Delete entity.
	ENT_UPDATE(), // Update entity pos.
	ENT_UPDATETHRUST(), // Update entity thrust.
	SELECT_SHIP(), // Select a player's ship.
	ENT_TARGET(), // Sent bits directly to an entity. Currently unused.
	MESSAGE(); // Chat message.
	
	public byte toByte()
	{
		return (byte) ordinal();
	}
	
	public static MessageCode from(byte b)
	{
		return b >= 0 && b < values().length ? values()[b] : null;
	}
}

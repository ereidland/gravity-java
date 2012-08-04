package com.evanreidland.e.net;

public enum MessageCode
{
	ENT_NEW(), // New entity.
	ENT_REM(), // Delete entity.
	ENT_UPDATE(), // Update entity position and velocity.
	ENT_UPDATETHRUST(), // Update entity thrust.
	ENT_TARGET(), // Sent bits directly to an entity. Currently unimplemented.
	ENT_SET_VAR(), // Set a networked variable for an entity.
	ENT_SET_STACK(), // Set a stack for an entity. Does not clear before adding.
	ENT_SET_FLAG(), // Set a flag for an entity.
	SELECT_SHIP(), // Select a player's ship.
	MESSAGE(), // Chat message.
	VERSION(), // For sending and receiving version information.
	KICK(), // For letting clients know why they were kicked.
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

package com.evanreidland.e.shared.ent;

public class Missle extends Weapon
{

	public Missle(String weaponConfig)
	{
		super(weaponConfig);
	}

	public Missle(String weaponConfig, Ship owner)
	{
		super(weaponConfig, owner);
	}
}

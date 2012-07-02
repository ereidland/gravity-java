package com.evanreidland.e.client.ent;

public class Laser extends Weapon {
	public Laser(String weaponConfig) {
		super(weaponConfig);
	}
	
	public Laser(String weaponConfig, Ship owner) {
		super(weaponConfig, owner);
	}
}

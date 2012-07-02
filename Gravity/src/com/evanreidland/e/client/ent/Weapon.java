package com.evanreidland.e.client.ent;

import com.evanreidland.e.config.ServerConfig;
import com.evanreidland.e.enums.DamageType;

public class Weapon {

	private float mBaseDamage;
	private DamageType mDamageType;
	private Ship mWeaponOwner;
	
	public Weapon() {
		
	}
	
	public Weapon(String weaponConfig, Ship owner) {
		this.mBaseDamage = ServerConfig.getConfigFloat(weaponConfig + "BaseDamage");
		this.mDamageType = DamageType.valueOf(ServerConfig.getConfigString(weaponConfig + "DamageType"));
		this.mWeaponOwner = owner;
	}
}

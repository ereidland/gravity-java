package com.evanreidland.e.shared.ent;

import java.util.HashMap;

import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.shared.config.ServerConfig;

public class Ship extends Entity {
	
	private float mMaxHullDurability;
	private float mCurrentHullDurability;
	private float mMaxShieldPower;
	private float mCurrentShieldPower;
	private float mMaxEnergy;
	private float mCurrentEnergy;
	private float mMaxEnergyUsage;
	private float mCurrentEnergyUsage;
	private int mShipType;
	private int mMaxLoadedMissles;
	
	private java.util.Vector<Weapon> mShipWeapons;
	private java.util.Vector<Weapon> mLoadedWeapons;
 	private java.util.Vector<Laser> mShipLasers;
	
	public Model model;
	public void onThink() {
		super.onThink();
	}
	public void onRender() {
//		if ( model != null ) {
//			model.pos.setAs(pos);
//			model.angle.setAs(angle);
//			model.Render();
//		} else {
//		}
		super.onRender();
	}
	public Ship(long id) {
		super("ship", id);
		
		model = null;
		
		flags.setState("ship", true);
	}
	
	public Ship(String shipType) {
		super("ship");
		
		//grab the different ship settings and set defaults
		mMaxHullDurability = ServerConfig.getConfigFloat(shipType + "HullDurability");
		mCurrentHullDurability = 0;
		mMaxShieldPower = ServerConfig.getConfigFloat(shipType + "ShieldPower");
		mCurrentShieldPower = 0;
		mMaxEnergy = ServerConfig.getConfigFloat(shipType + "MaxEnergy");
		mCurrentEnergy = 0;
		mMaxEnergyUsage = ServerConfig.getConfigFloat(shipType + "MaxEnergyUsage");
		mCurrentEnergyUsage = 0;
		mShipType = ServerConfig.getConfigInt(shipType);
		mMaxLoadedMissles = ServerConfig.getConfigInt(shipType + "MaxLoadedMissles");
		
		mLoadedWeapons = new java.util.Vector<Weapon>();
		mShipWeapons = new java.util.Vector<Weapon>();
		
		//DEBUG CODE
		switch(mShipType) {
		
			case 0:
				mShipWeapons.add(new Missle("homing", this));
				mShipWeapons.add(new Missle("nuke", this));
				mShipWeapons.add(new Missle("homing", this));
				mShipWeapons.add(new Missle("homing", this));
				break;
			case 1:
				break;
			case 2:
				break;
		}
		
		model = null;
		
		flags.add("ship class:" + shipType);
	}
}

package com.evanreidland.e.client.ent;

import java.util.HashMap;

import com.evanreidland.e.config.ServerConfig;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.graphics.Model;

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
	
	public Ship(long id, String shipType) {
		super("ship", id);
		
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
		
		model = null;
		
		flags.setState("ship", true);
	}
}

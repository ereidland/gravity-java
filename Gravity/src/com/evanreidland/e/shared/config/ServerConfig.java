package com.evanreidland.e.shared.config;

import java.util.HashMap;

public class ServerConfig {
	private static HashMap<String, Object> configConstants; 
	
	public static void setupConfigs()
	{
		configConstants = new HashMap<String, Object>();
		
		//SHIP VARIABLES
		configConstants.put("dreadnoughtHullDurability", 10000.0f);
		configConstants.put("dreadnoughtShieldPower", 5000.0f);
		configConstants.put("dreadnoughtMaxEnergy", 5000.0f);
		configConstants.put("dreadnoughtMaxEnergyUsage", 100.0f);
		configConstants.put("dreadnoughtMaxLoadedMissles", 3);
		configConstants.put("dreadnought", 0);
		configConstants.put("battleshipHullDurability", 5000.0f);
		configConstants.put("battleshipShieldPower", 2500.0f);
		configConstants.put("battleshipMaxEnergy", 2500.0f);
		configConstants.put("battleshipMaxEnergyUsage", 100.0f);
		configConstants.put("battleshipMaxLoadedMissles", 4);
		configConstants.put("battleship", 1);
		configConstants.put("interceptorHullDurability", 1000.0f);
		configConstants.put("interceptorShieldPower", 1000.0f);
		configConstants.put("interceptorMaxEnergy", 1000.0f);
		configConstants.put("interceptorMaxEnergyUsage", 200.0f);
		configConstants.put("interceptorMaxLoadedMissles", 2);
		configConstants.put("interceptor", 2);
		
		//WEAPON VARIABLES
		configConstants.put("homingBaseDamage", 400);
		configConstants.put("homingDamageType", "None");
		configConstants.put("nukeBaseDamage", 600);
		configConstants.put("nukeDamageType", "None");
		configConstants.put("disruptorBaseDamage", 100);
		configConstants.put("disruptorDamageType", "Disruptor");
		configConstants.put("laserBaseDamage", 50);
		configConstants.put("laserDamageType", "None");
	}
	
	public static Integer getConfigInt(String name) {
		return (Integer)configConstants.get(name);
	}
	
	public static Double getConfigDouble(String name) {
		return (Double)configConstants.get(name);
	}
	
	public static Float getConfigFloat(String name) {
		return (Float)configConstants.get(name);
	}
	
	public static String getConfigString(String name) {
		return (String)configConstants.get(name);
	}
}

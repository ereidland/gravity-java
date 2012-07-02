package com.evanreidland.e.config;

import java.util.HashMap;

public class ServerConfig {
	private static HashMap<String, Object> configConstants; 
	
	public static void setupConfigs()
	{
		configConstants.put("dreadnoughtHullDurability", 10000.0f);
		configConstants.put("dreadnoughtShieldPower", 5000.0f);
		configConstants.put("dreadnoughtMaxEnergy", 5000.0f);
		configConstants.put("dreadnoughtMaxEnergyUsage", 100.0f);
		configConstants.put("battleshipHullDurability", 5000.0f);
		configConstants.put("battleshipShieldPower", 2500.0f);
		configConstants.put("battleshipMaxEnergy", 2500.0f);
		configConstants.put("battleshipMaxEnergyUsage", 100.0f);
		configConstants.put("interceptorHullDurability", 1000.0f);
		configConstants.put("interceptorShieldPower", 1000.0f);
		configConstants.put("interceptorMaxEnergy", 1000.0f);
		configConstants.put("interceptorMaxEnergyUsage", 200.0f);
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

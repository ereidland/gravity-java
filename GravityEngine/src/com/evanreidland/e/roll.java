package com.evanreidland.e;

import java.util.Random;

public class roll {
	public static Random random = new Random();
	
	public static double Pi = (double)Math.PI;
	public static double Pi2 = (double)Math.PI*2;
	
	public static void setSeed(long newSeed) {
		random.setSeed(newSeed);
	}

	public static int randomInt(int min, int max) {
		return min + Math.round(min + random.nextInt((max + 1) - min));
	}
	
	public static double randomDouble(double min, double max) {
		return min + (random.nextDouble() * (max - min));
	}
	
	public static int Dice(int sides) {
		sides--;
		if ( sides <= 0 ) {
			return 0;
		}
		return 1 + random.nextInt(sides);
	}
	public static int Dice(int sides, long seed) {
		random.setSeed(seed);
		return Dice(sides);
	}
	
	public static boolean randomState() {
		return random.nextBoolean();
	}
}

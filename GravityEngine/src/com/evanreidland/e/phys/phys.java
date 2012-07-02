package com.evanreidland.e.phys;

import com.evanreidland.e.Vector3;

public class phys {
	public static Vector3 getTargetPoint(Vector3 launcherPos, Vector3 launcherVel, Vector3 targetPos, Vector3 targetVel, double shotVel) {
		
		double dist = launcherPos.getDistance(targetPos);
		
		if ( dist != 0 && shotVel != 0 ) {
			double lengthDelta = shotVel - (launcherPos.getDistance(targetPos.plus(targetVel)) - dist);
			return targetPos.plus(targetVel.multipliedBy(dist/lengthDelta));
		}
		return targetPos.cloned();
	}
}

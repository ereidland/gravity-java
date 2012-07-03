package com.evanreidland.e.phys;

import com.evanreidland.e.Vector3;

public class phys {
	public static Vector3 getTargetPoint(Vector3 launcherPos, Vector3 launcherVel, Vector3 targetPos, Vector3 targetVel, double shotVel) {
		
		double dist = launcherPos.getDistance(targetPos);
		double lengthDelta = shotVel - (launcherPos.plus(launcherVel).getDistance(targetPos.plus(targetVel)) - dist);
		
		if ( lengthDelta != 0 ) {
			return targetPos.plus(targetVel.minus(launcherVel).multipliedBy(dist/lengthDelta));
		}
		return targetPos.cloned();
	}
}

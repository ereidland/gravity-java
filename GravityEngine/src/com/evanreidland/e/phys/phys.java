package com.evanreidland.e.phys;

import com.evanreidland.e.Vector3;

public class phys {
	public static Vector3 getTargetPoint(Vector3 launcherPos, Vector3 launcherVel, Vector3 targetPos, Vector3 targetVel, double shotVel) {
		Vector3 pos = targetPos.cloned();
		double lengthDelta = launcherPos.plus(launcherVel).getDistance(targetPos.plus(targetVel)) - launcherPos.getDistance(targetPos);
		if ( lengthDelta != 0 && shotVel != 0 ) {
			double moveScalar = lengthDelta/shotVel;
			return targetPos.plus(targetVel.Normalize().multipliedBy(moveScalar*launcherPos.getDistance(targetPos)));
		}
		return pos;
	}
}

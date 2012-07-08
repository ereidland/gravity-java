package com.evanreidland.e.phys;

import com.evanreidland.e.Vector3;

public class phys
{
	public static class Target
	{
		public Vector3 pos, origin;
		public double time;

		public Target()
		{
			time = 0;
			pos = Vector3.Zero();
			origin = Vector3.Zero();
		}

		public Target(Vector3 pos, double time)
		{
			this.pos = pos.cloned();
			this.time = time;
		}
	}

	public static Target getTarget(Vector3 launcherPos, Vector3 launcherVel,
			Vector3 targetPos, Vector3 targetVel, double shotVel)
	{
		Target target = new Target(targetPos, 0);

		double dist = launcherPos.getDistance(targetPos);
		double lengthDelta = shotVel
				- (launcherPos.plus(launcherVel).getDistance(
						targetPos.plus(targetVel)) - dist);

		if (lengthDelta != 0)
		{
			target.time = dist / lengthDelta;
			target.pos = targetPos.plus(targetVel.minus(launcherVel)
					.multipliedBy(target.time));
			target.origin = launcherPos.cloned();
		}

		return target;
	}

	public static Vector3 getTargetPoint(Vector3 launcherPos,
			Vector3 launcherVel, Vector3 targetPos, Vector3 targetVel,
			double shotVel)
	{
		return getTarget(launcherPos, launcherVel, targetPos, targetVel,
				shotVel).pos;
	}
}

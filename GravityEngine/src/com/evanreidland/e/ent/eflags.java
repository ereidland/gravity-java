package com.evanreidland.e.ent;

import com.evanreidland.e.Flags;

public class eflags
{
	public static final Flags ally = new Flags(new String[]
	{
			"ally", "!enemy", "!dead"
	}), enemy = new Flags(new String[]
	{
			"!ally", "enemy", "!dead"
	}), red = new Flags(new String[]
	{
			"red", "!blue"
	}), blue = new Flags(new String[]
	{
			"!red", "blue"
	}), projectile = new Flags(new String[]
	{
			"projectile", "!solid"
	}), mech = new Flags(new String[]
	{
			"mech", "solid", "!dead"
	}), dead = new Flags(new String[]
	{
		"dead"
	}), alive = new Flags(new String[]
	{
		"!dead"
	});
}

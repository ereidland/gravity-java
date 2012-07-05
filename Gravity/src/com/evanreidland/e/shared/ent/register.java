package com.evanreidland.e.shared.ent;

import com.evanreidland.e.ent.ents;

public class register {
	public static void All() {
		ents.Register("ship", Ship.class);
		ents.Register("planet", Planet.class);
	}
}

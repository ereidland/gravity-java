package com.evanreidland.e.client.action;

import com.evanreidland.e.action.Action;
import com.evanreidland.e.action.act;

public class clientactions
{
	public static final Action client_bezmove = new ClientBezierMoveAction();
	
	public static void registerAll()
	{
		act.Register(client_bezmove);
	}
}

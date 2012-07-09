package com.evanreidland.e.server;

import java.util.Vector;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.server.ent.NotReallyAnEnemy;
import com.evanreidland.e.shared.Player;
import com.evanreidland.e.shared.config.ServerConfig;
import com.evanreidland.e.shared.ent.Ship;
import com.evanreidland.e.shared.enums.MessageCode;

public class GravityServerGame extends Game
{
	
	private Vector<Player> mConnectedPlayers;
	
	public void onUpdate()
	{
	}
	
	public void onInit()
	{
		mConnectedPlayers = new Vector<Player>();
		ServerConfig.setupConfigs();
		
		ents.Register("enemy", NotReallyAnEnemy.class); // >:D
	}
	
	// Unused.
	public void onRender()
	{
	}
	
	public void onRenderHUD()
	{
		
	}
	
	public void addNewPlayer(Player player)
	{
		mConnectedPlayers.add(player);
		// SpawnShipForPlayer(player);
	}
	
	private void SpawnShipForPlayer(Player player)
	{
		Ship playerShip = new Ship("battleship");
		
		playerShip.pos = new Vector3(2, 0, 0);
		playerShip.flags.setState("player", true);
		playerShip.flags.setState("targetable", true);
		playerShip.mass = 0.0001;
		playerShip.bStatic = false;
		playerShip.Spawn();
		
		player.setPlayerShip(playerShip);
		
		Bits bits = new Bits();
		
		bits.writeByte((byte) MessageCode.ENT_NEW.ordinal());
		bits.writeLong(playerShip.getID());
		bits.writeString(playerShip.getClassName());
		bits.write(playerShip.toBits());
		
		GravityServer.global.broadcastData(bits);
	}
}

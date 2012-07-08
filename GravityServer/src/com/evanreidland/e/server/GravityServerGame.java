package com.evanreidland.e.server;

import java.util.Vector;

import com.evanreidland.e.Game;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.ent.EntityMessageCode;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.shared.Player;
import com.evanreidland.e.shared.config.ServerConfig;
import com.evanreidland.e.shared.ent.Ship;
import com.evanreidland.e.shared.enums.MessageCode;

public class GravityServerGame extends Game {
	
	private Vector<Player> mConnectedPlayers;
	
	public void onUpdate() {
	}
	
	public void onInit() {	
		mConnectedPlayers = new Vector<Player>();
		ServerConfig.setupConfigs();
	}

	//Unused.
	public void onRender() {
	}
	
	public void onRenderHUD() {
	}
	
	public void addNewPlayer(Player player) {
		mConnectedPlayers.add(player);
		SpawnShipForPlayer(player);
	}
	
	private void SpawnShipForPlayer(Player player) {
		Ship playerShip = new Ship("battleship");
		
		playerShip.pos = new Vector3(2, 0, 0);
		playerShip.flags.setState("player", true);
		playerShip.flags.setState("targetable", true);
		playerShip.mass = 0.0001;
		playerShip.bStatic = false;
		playerShip.Spawn();
		
		player.setPlayerShip(playerShip);
		
		Bits bits = new Bits();
		
		bits.writeByte((byte)MessageCode.ENT_NEW.ordinal());
		byte[] className = playerShip.getClassName().getBytes();
		bits.writeByte((byte)className.length);
		bits.writeBytes(className);
		
		Vector3 pos = playerShip.pos,
				vel = playerShip.vel;
		
		bits.writeByte((byte)EntityMessageCode.POSITION.ordinal());
		bits.writeDouble(pos.x);
		bits.writeDouble(pos.y);
		bits.writeDouble(pos.z);
		
		bits.writeByte((byte)EntityMessageCode.VELOCITY.ordinal());
		bits.writeDouble(vel.x);
		bits.writeDouble(vel.y);
		bits.writeDouble(vel.z);
		
		bits.writeByte((byte)EntityMessageCode.MASS.ordinal());
		bits.writeDouble(playerShip.mass);
		
		bits.writeByte((byte)EntityMessageCode.RADIUS.ordinal());
		bits.writeDouble(playerShip.radius);
		
		GravityServer.global.broadcastData(bits);
		engine.Log(new String(bits.getBytes()));
	}
}

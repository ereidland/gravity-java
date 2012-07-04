package com.evanreidland.e.server;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.ent.EntityMessageCode;
import com.evanreidland.e.enums.MessageCode;
import com.evanreidland.e.event.ent.EntityDestroyedEvent;
import com.evanreidland.e.event.ent.EntitySpawnedEvent;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.TCPServer;

public class GravityServer extends TCPServer {
	public static GravityServer global;
	public void onDisconnect(long id) {
		broadcastData(id, ("Lost connection: " + getFullAddress(id)).getBytes());
	}
	
	public void onNewConnection(long id) {
		broadcastData(("New connection: " + getFullAddress(id)).getBytes());
	}

	public void onReceiveData(long id, Bits data) {
		//engine.Log(getFullAddress(id) + ": " + new String(data.getBytes()));
		
		broadcastData(id, (id + ": " + new String(data.getBytes())).getBytes());
	}

	public void onListenException(Exception e) {
		engine.Log("Exception: " + e.getMessage());
	}
	
	public void onEntitySpawned(EntitySpawnedEvent event) {
		Bits bits = new Bits();
		bits.writeByte((byte)MessageCode.ENT_NEW.ordinal());
		byte[] className = event.getEntityClass().getBytes();
		bits.writeByte((byte)className.length);
		bits.writeBytes(className);
		
		Vector3 pos = event.getEntity().pos,
				vel = event.getEntity().vel;
		
		bits.writeByte((byte)EntityMessageCode.POSITION.ordinal());
		bits.writeDouble(pos.x);
		bits.writeDouble(pos.y);
		bits.writeDouble(pos.z);
		
		bits.writeByte((byte)EntityMessageCode.VELOCITY.ordinal());
		bits.writeDouble(vel.x);
		bits.writeDouble(vel.y);
		bits.writeDouble(vel.z);
		
		bits.writeByte((byte)EntityMessageCode.MASS.ordinal());
		bits.writeDouble(event.getEntity().mass);
		
		bits.writeByte((byte)EntityMessageCode.RADIUS.ordinal());
		bits.writeDouble(event.getEntity().radius);
		
		broadcastData(bits.trim().getBytes());
	}
	
	public void onEntityDestroyed(EntityDestroyedEvent event) {
		//TODO code.
	}

	public GravityServer() {
		global = this;
	}
}

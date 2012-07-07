package com.evanreidland.e.server;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.ent.EntityMessageCode;
import com.evanreidland.e.event.ent.EntityDestroyedEvent;
import com.evanreidland.e.event.ent.EntitySpawnedEvent;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.TCPServer;
import com.evanreidland.e.shared.enums.MessageCode;
import com.evanreidland.e.shared.net.message;

//TODO: Move to GravityServer project and set it up.
public class GravityServer extends TCPServer {
	public static GravityServer global;
	public static void Log(String str) {
		GravityServerGUI.Log(str);
	}
	public void onDisconnect(long id) {
		Log("Server.onDisconnect: " + id + getFullAddress(id));
	}
	
	public void onNewConnection(long id) {
		Log("Server.onNewConnection: " + id + getFullAddress(id));
	}
	
	public void broadcastMessage(long ignoreID, String str) {
		broadcastData(ignoreID, 
				new Bits().writeByte(message.toByte(MessageCode.MESSAGE))
						  .writeBytes(str.getBytes()));
	}

	public void onReceiveData(long id, Bits data) {
		try {
			while ( data.getRemainingBits() >= 8 ) {
				byte b = data.readByte();
				Log("Byte: " + b);
				MessageCode code = message.getCode(b);
				switch ( code ) {
					case MESSAGE:
						String message = getFullAddress(id) + ": " + data.readString();
						Log(message);
						broadcastMessage(id, message);
						continue;
					default:
						break;
				}
			}
		} catch ( Exception e ) {
			StackTraceElement[] el = e.getStackTrace();
			Log(e.toString());
			for ( int i = 0; i < el.length; i++ ) {
				Log(el[i].toString());
			}
		}
	}

	public void onListenException(Exception e) {
		Log("Exception: " + e.getMessage());
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
		
		broadcastData(bits);
	}
	
	public void onEntityDestroyed(EntityDestroyedEvent event) {
		
	}

	public GravityServer() {
		global = this;
	}
}

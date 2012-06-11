package com.evanreidland.e.net;

import java.net.InetAddress;

public class Packet {
	public static String toAddressString(InetAddress addr, int port ) {
		return addr.toString() + ":" + port;
	}
	
	//private static long ids = 0;
	public static final short maxUDPSize = 256;
	
	public long targetID, timeStamp, packetID;
	public Bits data;
	
	private InetAddress src;
	private int srcPort;
	
	public boolean hasID, wasUDP;
	
	public long getPacketID() {
		return packetID;
	}
	
	public InetAddress getSource() {
		return src;
	}
	
	public int getSourcePort() {
		return srcPort;
	}
	
	public byte[] toBytes() {
		Bits ndata = new Bits();
		ndata.makeRoom(maxUDPSize);
		ndata.writeBit(true); // Has time stamp.
		ndata.writeLong(timeStamp);
		ndata.writeBit(true); // Small packet.
		ndata.writeByte((byte)data.getEnd());
		ndata.writeBit(targetID != 0); // Is targeted.
		if ( targetID != 0 ) {
			ndata.writeLong(targetID);
		}
		/*ndata.writeBit(hasID); // Has id.
		if ( hasID ) {
			ndata.writeLong(++ids);
		}*/
		
		ndata.writeBytes(data.getBytes(), data.getEnd());
		return ndata.getBytes();
	}
	
	public static Packet fromBytes(byte[] bytes, InetAddress src, int port) {
		Bits packetData = new Bits().writeBytes(bytes);
		
		Packet packet = new Packet();
		if ( packetData.readBit() ) {
			packet.timeStamp = packetData.readLong();
		}
		short size = packetData.readBit() ? packetData.readByte() : packetData.readShort();
		if ( packetData.readBit() ) {
			packet.targetID = packetData.readLong();
		}
		/*packet.hasID = packetData.readBit();
		if ( packet.hasID ) {
			packet.packetID = packetData.readLong();
		}*/
		
		packet.data = new Bits().writeBytes(packetData.readBytes(size));
		packet.src = src;
		packet.srcPort = port;
		
		return packet;
	}
	
	public void logInfo() {
		NetLog.Log("Packet id/time/size/src: (" + targetID + ", " + timeStamp + " ms, " + data.getEndBits() + " bits, " + (src != null ? src.toString() : "0") + ":" + srcPort + ")");
		NetLog.Log("Data: " + new String(data.getBytes()));
	}
	
	public Packet(InetAddress src, int port) {
		targetID = 0;
		timeStamp = 0;
		packetID = 0;
		data = new Bits();
		this.src = src;
		this.srcPort = port;
		this.wasUDP = true;
	}
	
	public Packet() {
		this(null, 0);
	}
}

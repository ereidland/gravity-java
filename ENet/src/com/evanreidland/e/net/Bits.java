package com.evanreidland.e.net;

import java.nio.ByteBuffer;

public class Bits {
	//Note: all sizes are in bits.
	public static final byte BOOL_SIZE = 1,
							 BYTE_SIZE = (byte)(Byte.SIZE*8),
							 CHAR_SIZE = (byte)(Character.SIZE*8),
							 INT_SIZE = (byte)(Integer.SIZE*8),
							 FLOAT_SIZE = (byte)(Float.SIZE*8),
							 LONG_SIZE = (byte)(Long.SIZE*8),
							 DOUBLE_SIZE = (byte)(Double.SIZE*8);
	
	public static NetCode toNetCode(byte b) {
		return b >= 0 && b < NetCode.values().length ? NetCode.values()[b] : NetCode.NULL;
	}
	public static byte setBitSafe(byte b, int index, boolean state) {
		return (byte)(
			index >= 0 && index <= 7 ?
				state ? (b | (1 << index))
					  : (b & ~(1 << index))
			: b		  
					 );
	}
	public static byte setBit(byte b, int index, boolean state) {
		return (byte)(
				state ? (b | (1 << index))
					  : (b & ~(1 << index))
					 );
	}
	
	public static boolean getBit(byte b, int index) {
		return ((b >> index) & 1) == 1;
	}
	private byte[] data;
	private int readIndex, end;
	
	//Simple hash.
	public int getHash() {
		int hash = 0;
		for ( int i = 0; i < end; i++ ) {
			hash += data[i] ^ i;
		}
		hash ^= end;
		return hash;
	}
	
	public int getPos() {
		return readIndex;
	}
	
	public int getSize() {
		return data.length;
	}
	public int getNumBits() {
		return getSize()*8;
	}
	
	public int getEnd() {
		return ((int)end/8) + (end%8 == 0 ? 0 : 1);
	}
	public int getEndBits() {
		return end;
	}
	
	public Bits skipTo(int index) {
		if ( index < 0 && index > -end ) {
			readIndex = end + index;
		} else if ( index >= 0 ) {
			readIndex = index;
		}
		return this;
	}
	
	public Bits makeRoom(int size) {
		if ( size > data.length ) {
			byte[] newData = new byte[size];
			for ( int i = 0; i < (end/8) + (end%8 == 0 ? 0 : 1); i++ ) {
				newData[i] = data[i];
			}
			
			data = newData;
		}
		return this;
	}
	
	public Bits trim() {
		
		byte[] newData = new byte[((int)end/8) + (end%8 == 0 ? 0 : 1)];
		for( int i = 0; i < newData.length; i++ ) {
			newData[i] = data[i];
		}
		data = newData;
		return this;
	}
	
	public boolean readBit() {
		if ( readIndex < end ) {
			boolean state = getBit(data[readIndex/8], readIndex % 8);
			readIndex++;
			return state;
		}
		return false;
	}
	
	public byte[] readBits(int howMany) {
		if ( howMany <= 0 ) {
			return new byte[0];
		}
		int newEnd = readIndex + howMany;
		if ( newEnd <= end ) {
			byte[] bytes = new byte[(int)Math.ceil(howMany/8f)];
			int byteIndex = 0, bitIndex = 0;
			for ( int i = 0; i < howMany; i++ ) {
				byteIndex = i/8;
				bitIndex = i % 8;
				bytes[byteIndex] = setBit(bytes[byteIndex], bitIndex, getBit(data[readIndex/8], readIndex % 8));
				readIndex++;
			}
			return bytes;
		} else {
			readIndex = end;
		}
		
		return new byte[0];
	}
	public byte[] readBytes(int howMany) {
		return readBits(howMany*8);
	}
	
	public byte[] readRemaining() {
		return readBits(end - readIndex);
	}
	
	public int getRemainingBits() {
		return end - readIndex;
	}
	public int getRemainingBytes() {
		return ((int)getRemainingBits()/8) + (end%8 == 0 ? 0 : 1);
	}
	
	public byte readByte() {
		byte[] bytes = readBytes(1);
		return bytes.length == 1 ? bytes[0] : 0;
	}
	
	public short readShort() {
		byte[] bytes = readBytes(2);
		return (short)(bytes.length == 2
				   ?  (bytes[1] & 0xFF)
				   | ((bytes[0] & 0xFF) << 8) 
				   : 0);
	}
	
	public int readInt() {
		byte[] bytes = readBytes(4);
		return bytes.length == 4
				?  (bytes[3] & 0xFF)
				| ((bytes[2] & 0xFF) << 8) 
				| ((bytes[1] & 0xFF) << 16)
				| ((bytes[0] & 0xFF) << 24)
				: 0;
	}
	
	public long readLong() {
		return ((long)(readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
	}
	
	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}
	
	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}
	
	public String readString() {
		int length = 0;
		String str = "";
		if ( readBit() ) {
			length = readByte();
		} else if ( readBit() ) {
			length = readShort();
		} else { // There should never be strings this long in normal use.
			length = readInt();
		}
		str = new String(readBytes(length));
		return str;
	}
	
	public Bits writeBit(boolean state) {
		if ( end >= data.length ) {
			if ( data.length > 0 ) {
				int newSize = data.length;
				while ( newSize < end + 1 ) {
					newSize *= 2;
				}
				makeRoom(newSize);
			} else {
				makeRoom(1);
			}
		}
		data[end/8] = setBit(data[end/8], end % 8, state);
		end++;
		return this;
	}
	
	public Bits writeBits(byte[] bits, int howMany) {
		if ( howMany <= 0 ) {
			return this;
		}
		int byteIndex = 0, bitIndex = 0;
		
		int newEnd = end + howMany;
		
		if (newEnd > data.length*8 ) {
			if ( data.length > 0 ) {
				int newSize = data.length;
				while ( newSize*8 < newEnd ) {
					newSize *= 2;
				}
				makeRoom(newSize);
			} else {
				makeRoom(newEnd/8);
			}
		}
		
		for ( int i = 0; i < howMany; i++ ) {
			bitIndex = (i % 8);
			byteIndex = i/8;
			data[end/8] = setBit(data[end/8], end % 8, getBit(bits[byteIndex], bitIndex));
			end++;
		}
		
		return this;
	}
	
	public Bits writeBytes(byte[] bytes, int howMany) {
		writeBits(bytes, howMany*8);
		return this;
	}
	
	public Bits writeBytes(byte[] bytes) {
		return writeBytes(bytes, bytes.length);
	}
	
	public Bits writeByte(byte b) {
		return writeBytes(new byte[] { b });
	}
	
	public Bits writeShort(short toWrite) {
		return writeBytes(ByteBuffer.allocate(2).putShort(toWrite).array());
	}
	
	public Bits writeInt(int toWrite) {
		return writeBytes(ByteBuffer.allocate(4).putInt(toWrite).array());
	}
	
	public Bits writeFloat(float toWrite) {
		return writeBytes(ByteBuffer.allocate(4).putFloat(toWrite).array());
	}
	
	public Bits writeDouble(double toWrite) {
		return writeBytes(ByteBuffer.allocate(8).putDouble(toWrite).array());
	}
	
	public Bits writeLong(long toWrite) {
		return writeBytes(ByteBuffer.allocate(8).putLong(toWrite).array());
	}
	
	public Bits writeString(String str) {
		byte[] strBytes = str.getBytes();
		int len = strBytes.length;
		if ( len <= Byte.MAX_VALUE) {
			writeBit(true);
			writeByte((byte)len);
		} else {
			writeBit(false);
			if ( len <= Short.MAX_VALUE ) {
				writeBit(true);
				writeShort((short)len);
			} else {
				writeBit(false);
				writeInt(len);
			}
		}
		writeBytes(strBytes);
		
		return this;
	}
	
	public byte[] getBytes() {
		return data;
	}
	
	public Bits() {
		data = new byte[0];
		end = 0;
		readIndex = 0;
	}
}

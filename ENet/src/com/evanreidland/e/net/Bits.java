package com.evanreidland.e.net;

import java.nio.ByteBuffer;

public class Bits
{
	// Note: all sizes are in bits.
	public static final byte BOOL_SIZE = 1, BYTE_SIZE = (byte) (Byte.SIZE),
			CHAR_SIZE = (byte) (Character.SIZE), //
			INT_SIZE = (byte) (Integer.SIZE), //
			FLOAT_SIZE = (byte) (Float.SIZE), //
			LONG_SIZE = (byte) (Long.SIZE), //
			DOUBLE_SIZE = (byte) (Double.SIZE); //
			
	public static NetCode toNetCode(byte b)
	{
		return b >= 0 && b < NetCode.values().length ? NetCode.values()[b]
				: NetCode.NULL;
	}
	
	public static byte setBitSafe(byte b, int index, boolean state)
	{
		return (byte) (index >= 0 && index <= 7 ? state ? (b | (1 << index))
				: (b & ~(1 << index)) : b);
	}
	
	public static byte setBit(byte b, int index, boolean state)
	{
		return (byte) (state ? (b | (1 << index)) : (b & ~(1 << index)));
	}
	
	public static boolean getBit(byte b, int index)
	{
		return ((b >> index) & 1) == 1;
	}
	
	private byte[] data;
	private int readIndex, end;
	
	// Simple hash.
	public int getHash()
	{
		int hash = 0;
		for (int i = 0; i < end; i++)
		{
			hash += data[i] ^ i;
		}
		hash ^= end;
		return hash;
	}
	
	public int getPos()
	{
		return readIndex;
	}
	
	public int getSize()
	{
		return data.length;
	}
	
	public int getNumBits()
	{
		return getSize() * 8;
	}
	
	public int getEnd()
	{
		return ((int) end / 8) + (end % 8 == 0 ? 0 : 1);
	}
	
	public int getEndBits()
	{
		return end;
	}
	
	public Bits skipTo(int index)
	{
		if (index < 0 && index >= -end)
		{
			readIndex = end + index;
		}
		else if (index >= 0)
		{
			readIndex = index;
		}
		return this;
	}
	
	public Bits makeRoom(int size)
	{
		if (size > data.length)
		{
			byte[] newData = new byte[size];
			for (int i = 0; i < (end / 8) + (end % 8 == 0 ? 0 : 1); i++)
			{
				newData[i] = data[i];
			}
			
			data = newData;
		}
		return this;
	}
	
	public Bits trim()
	{
		
		byte[] newData = new byte[((int) end / 8) + (end % 8 == 0 ? 0 : 1)];
		for (int i = 0; i < newData.length; i++)
		{
			newData[i] = data[i];
		}
		data = newData;
		return this;
	}
	
	public boolean readBit()
	{
		if (readIndex < end)
		{
			boolean state = getBit(data[readIndex / 8], readIndex % 8);
			readIndex++;
			return state;
		}
		return false;
	}
	
	public byte[] readBits(int howMany)
	{
		if (howMany <= 0)
		{
			return new byte[0];
		}
		int newEnd = readIndex + howMany;
		if (newEnd <= end)
		{
			byte[] bytes = new byte[howMany / 8 + ((howMany % 8) != 0 ? 1 : 0)];
			int byteIndex = 0, bitIndex = 0;
			for (int i = 0; i < howMany; i++)
			{
				byteIndex = i / 8;
				bitIndex = i % 8;
				bytes[byteIndex] = setBit(bytes[byteIndex], bitIndex,
						getBit(data[readIndex / 8], readIndex % 8));
				readIndex++;
			}
			return bytes;
		}
		else
		{
			readIndex = end;
		}
		
		return new byte[0];
	}
	
	public byte[] readBytes(int howMany)
	{
		return readBits(howMany * 8);
	}
	
	public byte[] readRemaining()
	{
		return readBits(getRemainingBits());
	}
	
	public int getRemainingBits()
	{
		return end - readIndex;
	}
	
	public int getRemainingBytes()
	{
		return ((int) getRemainingBits() / 8) + (end % 8 == 0 ? 0 : 1);
	}
	
	public byte readByte()
	{
		byte[] bytes = readBytes(1);
		return bytes.length == 1 ? bytes[0] : 0;
	}
	
	public byte readSmallByte(int bits)
	{
		if (bits <= 8)
		{
			byte[] bytes = readBits(bits);
			return bytes.length > 0 ? bytes[0] : 0;
		}
		return 0;
	}
	
	public int readSmall(int bits)
	{
		if (bits <= 32)
		{
			byte[] bytes = readBits(bits);
			if (bytes.length > 0)
			{
				ByteBuffer buff = ByteBuffer.allocate(bytes.length).put(bytes);
				buff.flip();
				return bits > 16 ? buff.getInt() : bits > 8 ? buff.getShort()
						: buff.get(0);
			}
			
		}
		return 0;
	}
	
	public short readShort()
	{
		ByteBuffer buff = ByteBuffer.allocate(2).put(readBytes(2));
		buff.flip();
		return buff.limit() == 2 ? buff.getShort() : 0;
	}
	
	public int readInt()
	{
		ByteBuffer buff = ByteBuffer.allocate(4).put(readBytes(4));
		buff.flip();
		return buff.limit() == 4 ? buff.getInt() : 0;
	}
	
	public long readLong()
	{
		ByteBuffer buff = ByteBuffer.allocate(8).put(readBytes(8));
		buff.flip();
		return buff.limit() == 8 ? buff.getLong() : 0;
	}
	
	public float readFloat()
	{
		return Float.intBitsToFloat(readInt());
	}
	
	public double readDouble()
	{
		return Double.longBitsToDouble(readLong());
	}
	
	public long readSize()
	{
		long size = 0;
		byte small = readSmallByte(2);
		switch (small)
		{
			case 0:
				size = readByte();
				break;
			case 1:
				size = readShort();
				break;
			case 2:
				size = readInt();
				break;
			case 3:
				size = readLong();
				break;
		}
		return size;
	}
	
	public String readString()
	{
		int length = (int) readSize();
		String str = "";
		str = new String(readBytes(length));
		return str;
	}
	
	public Bits writeBit(boolean state)
	{
		if (end >= data.length)
		{
			if (data.length > 0)
			{
				int newSize = data.length;
				while (newSize < end + 1)
				{
					newSize *= 2;
				}
				makeRoom(newSize);
			}
			else
			{
				makeRoom(1);
			}
		}
		data[end / 8] = setBit(data[end / 8], end % 8, state);
		end++;
		return this;
	}
	
	public Bits writeBits(byte[] bits, int howMany)
	{
		if (howMany <= 0)
		{
			return this;
		}
		int byteIndex = 0, bitIndex = 0;
		
		int newEnd = end + howMany;
		
		if (newEnd >= data.length * 8)
		{
			if (data.length > 0)
			{
				int newSize = data.length;
				while (newSize * 8 <= newEnd)
				{
					newSize *= 2;
				}
				makeRoom(newSize);
			}
			else
			{
				makeRoom(newEnd);
			}
		}
		
		for (int i = 0; i < howMany; i++)
		{
			bitIndex = (i % 8);
			byteIndex = i / 8;
			data[end / 8] = setBit(data[end / 8], end % 8,
					getBit(bits[byteIndex], bitIndex));
			end++;
		}
		
		return this;
	}
	
	public Bits writeBytes(byte[] bytes, int howMany)
	{
		writeBits(bytes, howMany * 8);
		return this;
	}
	
	public Bits writeBytes(byte[] bytes)
	{
		return writeBytes(bytes, bytes.length);
	}
	
	public Bits writeByte(byte b)
	{
		return writeBytes(new byte[]
		{
			b
		});
	}
	
	public Bits writeSmallByte(int num, int bits)
	{
		if (bits <= 8)
		{
			writeBits(new byte[]
			{
				(byte) num
			}, bits);
		}
		return this;
	}
	
	public Bits writeSmall(int num, int bits)
	{
		if (bits <= 32)
		{
			ByteBuffer buff = ByteBuffer.allocate(4).putInt(num);
			buff.flip();
			writeBits(buff.array(), bits);
		}
		return this;
	}
	
	public Bits writeShort(short toWrite)
	{
		return writeBytes(ByteBuffer.allocate(2).putShort(toWrite).array());
	}
	
	public Bits writeInt(int toWrite)
	{
		return writeBytes(ByteBuffer.allocate(4).putInt(toWrite).array());
	}
	
	public Bits writeFloat(float toWrite)
	{
		return writeBytes(ByteBuffer.allocate(4).putFloat(toWrite).array());
	}
	
	public Bits writeDouble(double toWrite)
	{
		return writeBytes(ByteBuffer.allocate(8).putDouble(toWrite).array());
	}
	
	public Bits writeLong(long toWrite)
	{
		return writeBytes(ByteBuffer.allocate(8).putLong(toWrite).array());
	}
	
	public Bits writeSize(long size)
	{
		if (size <= Byte.MAX_VALUE)
		{
			writeSmallByte(0, 2);
			writeByte((byte) size);
		}
		else if (size <= Short.MAX_VALUE)
		{
			writeSmallByte(1, 2);
			writeShort((short) size);
		}
		else if (size <= Integer.MAX_VALUE)
		{
			writeSmallByte(2, 2);
			writeInt((int) size);
		}
		else
		{
			writeSmallByte(3, 2);
			writeLong(size);
		}
		return this;
	}
	
	public Bits writeString(String str)
	{
		byte[] strBytes = str.getBytes();
		writeSize(strBytes.length);
		writeBytes(strBytes);
		
		return this;
	}
	
	public Bits write(Bits bits, int len)
	{
		return writeBits(bits.readRemaining(), len);
	}
	
	public Bits write(Bits bits)
	{
		return write(bits, bits.getRemainingBits());
	}
	
	public Bits copy()
	{
		return new Bits().writeBits(data, end);
	}
	
	public byte[] getBytes()
	{
		return data;
	}
	
	public Bits()
	{
		data = new byte[0];
		end = 0;
		readIndex = 0;
	}
}

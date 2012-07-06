package com.evanreidland.e.shared.net;

import com.evanreidland.e.shared.enums.MessageCode;

public class message {
	public static MessageCode getCode(byte b) {
		return b < MessageCode.values().length ? MessageCode.values()[b] : null;
	}
	
	public static byte toByte(MessageCode code) {
		return (byte)code.ordinal();
	}
}

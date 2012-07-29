package com.evanreidland.e.client.gui;

import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
import com.evanreidland.e.net.Bits;
import com.evanreidland.e.net.MessageCode;
import com.evanreidland.e.net.network;

public class ChatTextField extends TextField
{
	public MessageArea area;
	
	public void onEnter(String text)
	{
		System.out.println("Text: " + text);
		network.Send(new Bits().writeByte(MessageCode.MESSAGE.toByte())
				.writeString(text));
		area.addItem(text);
	}
	
	public void onUpdate()
	{
		boolean pFocus = bFocused;
		super.onUpdate();
		pFocus = pFocus == bFocused;
		if (!bFocused && pFocus && input.isKeyDown(key.KEY_ENTER))
		{
			bFocused = true;
		}
	}
	
	public void onRender()
	{
		if (bFocused)
			super.onRender();
	}
	
	public ChatTextField(String name, double width, double height,
			MessageArea area)
	{
		super(name, width, height);
		this.area = area;
	}
}

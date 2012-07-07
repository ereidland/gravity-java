package com.evanreidland.e.client;

import com.evanreidland.e.Game;
import com.evanreidland.e.ResourceType;
import com.evanreidland.e.engine;
import com.evanreidland.e.audio.sound;
import com.evanreidland.e.client.audio.ALAudioManager;
import com.evanreidland.e.client.audio.ALAudioResourceManager;
import com.evanreidland.e.client.audio.alsound;
import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
import com.evanreidland.e.client.graphics.GLGraphicsManager;
import com.evanreidland.e.client.graphics.GLLight;
import com.evanreidland.e.client.graphics.GLRenderList;
import com.evanreidland.e.client.graphics.TextureResourceManager;
import com.evanreidland.e.client.graphics.VBOGraphicsData;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.gui.hud;
import com.evanreidland.e.shared.ent.register;

public abstract class GameClientBase extends Game
{
	private double lCamWidth, lCamHeight;

	public void onUpdate()
	{
		if (graphics.camera.width != lCamWidth
				|| graphics.camera.height != lCamHeight)
		{
			lCamWidth = graphics.camera.width;
			lCamHeight = graphics.camera.height;
			hud.gui.Layout();

		}
		hud.gui.Update();
		if (input.isKeyDown(key.MOUSE_LBUTTON))
		{
			if (hud.gui != null)
			{
				hud.gui.onClick(Game.mousePos.x, Game.mousePos.y);
			}
		}

		String typed = input.getTyped();
		for (int i = 0; i < typed.length(); i++)
		{
			hud.gui.onType(typed.charAt(i));
		}
	}

	public void onRenderHUD()
	{
		hud.gui.Render();
	}

	public void onRender()
	{

	}

	public void onInit()
	{
		graphics.setGraphicsManager(new GLGraphicsManager());
		engine.addResourceManager(ResourceType.Texture,
				new TextureResourceManager());
		graphics.setDataClass(VBOGraphicsData.class);
		graphics.setLightClass(GLLight.class);
		graphics.setRenderListClass(GLRenderList.class);

		engine.addResourceManager(ResourceType.Sound,
				new ALAudioResourceManager());
		sound.setAudioManager(new ALAudioManager());
		alsound.init();

		EApplet.active.clearR = 0.0f;
		EApplet.active.clearG = 0.1f;
		EApplet.active.clearB = 0.2f;

		register.All();
	}
}

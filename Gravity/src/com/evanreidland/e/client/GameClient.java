package com.evanreidland.e.client;

import org.lwjgl.opengl.GL11;

import com.evanreidland.e.Game;
import com.evanreidland.e.ResourceType;
import com.evanreidland.e.engine;
import com.evanreidland.e.audio.sound;
import com.evanreidland.e.client.audio.ALAudioManager;
import com.evanreidland.e.client.audio.ALAudioResourceManager;
import com.evanreidland.e.client.audio.alsound;
import com.evanreidland.e.client.ent.register;
import com.evanreidland.e.client.graphics.GLGraphicsManager;
import com.evanreidland.e.client.graphics.GLLight;
import com.evanreidland.e.client.graphics.GLRenderList;
import com.evanreidland.e.client.graphics.TextureResourceManager;
import com.evanreidland.e.client.graphics.VBOGraphicsData;
import com.evanreidland.e.graphics.graphics;

public abstract class GameClient extends Game {
	
	public void onInit() {
		graphics.setGraphicsManager(new GLGraphicsManager());
		engine.addResourceManager(ResourceType.Texture, new TextureResourceManager());
		graphics.setDataClass(VBOGraphicsData.class);
		graphics.setLightClass(GLLight.class);
		graphics.setRenderListClass(GLRenderList.class);
		
		engine.addResourceManager(ResourceType.Sound, new ALAudioResourceManager());
		sound.setAudioManager(new ALAudioManager());
		alsound.init();
		
		EApplet.active.clearR = 0.0f;
		EApplet.active.clearG = 0.1f;
		EApplet.active.clearB = 0.2f;
		
		register.All();
		
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
}

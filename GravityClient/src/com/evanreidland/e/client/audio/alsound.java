package com.evanreidland.e.client.audio;

import java.io.FileInputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.graphics.graphics;

public class alsound {
	public static final int MAX_BUFFERS = 64;
	public static Vector3 sourcePos = Vector3.Zero(), sourceVel = Vector3.Zero(), listenerPos = Vector3.Zero(), listenerVel = Vector3.Zero(), listenerAt = new Vector3(1, 1, 0), listenerUp = new Vector3(0, 0, 1);
	private static IntBuffer sources, buffers;
	private static int numFiles = 0;
	
	private static FloatBuffer fsourcePos, fsourceVel, flistenerPos, flistenerVel, flistenerOri;
	
	private static HashMap<String, Integer> files = new HashMap<String, Integer>();
	
	public static double volume = 1f, pitch = 1f;
	
	public static void goToCamera() {
		listenerAt.setAs(graphics.forward);
		listenerUp.setAs(graphics.up);
		listenerPos.setAs(graphics.camera.pos);
		listenerVel.setAs(Vector3.Zero());
		sourceVel.setAs(Vector3.Zero());
		sourcePos.setAs(graphics.camera.pos.plus(graphics.forward));
	}
	
	public static boolean loadSound(String address, String reg) {
		//System.out.println("URL: " + url);
		//System.out.println("Name: " + reg);
		try {
			WaveData data = WaveData.create(new FileInputStream(address));
			if ( data != null ) {
				AL10.alBufferData(buffers.get(numFiles), data.format, data.data, data.samplerate);
				files.put(reg, numFiles);
				data.dispose();
				numFiles++;
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void updateListener() {
		flistenerPos.put(0, (float)listenerPos.x);
		flistenerPos.put(1, (float)listenerPos.y);
		flistenerPos.put(2, (float)listenerPos.z);
		
		flistenerVel.put(0, (float)listenerVel.x);
		flistenerVel.put(1, (float)listenerVel.y);
		flistenerVel.put(2, (float)listenerVel.z);
		
		flistenerOri.put(0, (float)listenerAt.x);
		flistenerOri.put(1, (float)listenerAt.y);
		flistenerOri.put(2, (float)listenerAt.z);
		
		flistenerOri.put(3, (float)listenerUp.x);
		flistenerOri.put(4, (float)listenerUp.y);
		flistenerOri.put(5, (float)listenerUp.z);
		
		AL10.alListener(AL10.AL_POSITION, flistenerPos);
		AL10.alListener(AL10.AL_VELOCITY, flistenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, flistenerOri);
	}
	
	private static void updateSource() {
		fsourcePos.put(0, (float)sourcePos.x);
		fsourcePos.put(1, (float)sourcePos.y);
		fsourcePos.put(2, (float)sourcePos.z);
		
		fsourceVel.put(0, (float)sourceVel.x);
		fsourceVel.put(1, (float)sourceVel.y);
		fsourceVel.put(2, (float)sourceVel.z);
	}
	
	public static void playSound(String reg, double pitch) {
		int play = 0;
		Integer snd = files.get(reg);
		if ( snd == null ) return;
		for ( int i = 0; i < MAX_BUFFERS; i++ ) {
			play = AL10.alGetSourcei(sources.get(i), AL10.AL_SOURCE_STATE);
			if ( play != AL10.AL_PLAYING ) {
				updateListener();
				updateSource();
				AL10.alSourcei(sources.get(i), AL10.AL_BUFFER, buffers.get(snd));
				AL10.alSource(sources.get(i), AL10.AL_POSITION, fsourcePos);
				AL10.alSource(sources.get(i), AL10.AL_VELOCITY, fsourceVel);
				AL10.alSourcef(sources.get(i), AL10.AL_PITCH, (float)pitch);
				AL10.alSourcef(sources.get(i), AL10.AL_GAIN, (float)volume);
				AL10.alSourcePlay(sources.get(i));
				return;
			}
		}
	}
	
	public static void playSound(String reg) {
		playSound(reg, 1);
	}
	
	public static boolean init() {
		try {
			AL.create();
			sources = BufferUtils.createIntBuffer(MAX_BUFFERS);
			buffers = BufferUtils.createIntBuffer(MAX_BUFFERS);
			
			AL10.alGenBuffers(buffers);
			AL10.alGenSources(sources);
			
			fsourceVel = BufferUtils.createFloatBuffer(3);
			fsourcePos = BufferUtils.createFloatBuffer(3);
			
			flistenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0, 0, 0 });
			flistenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { 0, 0, 0 });
			flistenerOri = BufferUtils.createFloatBuffer(6).put(new float[] { 0, 0, -1, 0, 1, 0 });
			
			flistenerPos.flip();
			flistenerVel.flip();
			flistenerOri.flip();
			
			numFiles = 0;
			return AL10.alGetError() == AL10.AL_NO_ERROR;
		} catch ( Exception e ) {
			e.printStackTrace();
			return false;
		}
	}

}

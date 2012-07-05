package com.evanreidland.e.client.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
//import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLU;


public class TextureLoader {
    private HashMap<String, Texture> table = new HashMap<String, Texture>();
    private Vector<Texture> textures = new Vector<Texture>();
    private ColorModel glAlphaColorModel;
    private ColorModel glColorModel;
    public static long sleepTime = 100;
    
    @SuppressWarnings("unchecked")
	public void reloadAll() {
    	Vector<Texture> tempTextures = (Vector<Texture>) textures.clone();
    	textures.clear();
    	table.clear();
    	
    	for ( int i = 0; i < tempTextures.size(); i++ ) {
    		Texture t = tempTextures.get(i);
    		System.out.println("Reloading: " + t.urlString);
    		getTexture(t.urlString);
    	}
    }
    
    public TextureLoader() {
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,8},
                                            true,
                                            false,
                                            ComponentColorModel.TRANSLUCENT,
                                            DataBuffer.TYPE_BYTE);
                                            
        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,0},
                                            false,
                                            false,
                                            ComponentColorModel.OPAQUE,
                                            DataBuffer.TYPE_BYTE);
    }
    private int createTextureID() 
    { 
       IntBuffer tmp = createIntBuffer(1);
       GL11.glGenTextures(tmp); 
       return tmp.get(0);
    }
    public Texture getTexture(String resourceName) {
        Texture tex = (Texture) table.get(resourceName);
        
        if (tex != null) {
            return tex;
        }
        
        tex = getTexture(resourceName,
                         GL11.GL_TEXTURE_2D,
                         GL11.GL_RGBA8,
                         Texture.minFilter,
                         Texture.magFilter);
        table.put(resourceName, tex);
        textures.add(tex);
        
        return tex;
    }
    public Texture getTexture(String resourceName, 
                              int target, 
                              int dstPixelFormat, 
                              int minFilter, 
                              int magFilter)
    { 
        int srcPixelFormat = 0;
        
        // create the texture ID for this texture 
        int textureID = createTextureID(); 
        Texture texture = new Texture(target,textureID); 
        
        // bind this texture 
        GL11.glBindTexture(target, textureID); 
 
        BufferedImage bufferedImage = loadImage(resourceName); 
        if ( bufferedImage == null ) {
        	return texture;
        }
        texture.setWidth(bufferedImage.getWidth());
        texture.setHeight(bufferedImage.getHeight());
        
        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL11.GL_RGBA;
        } else {
            srcPixelFormat = GL11.GL_RGB;
        }

        // convert that image into a byte buffer of texture data 
        ByteBuffer textureBuffer = convertImageData(bufferedImage, texture); 
        
        if (target == GL11.GL_TEXTURE_2D) 
        { 
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, minFilter); 
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, magFilter); 
            
            GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT );
			GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT );
        }
 
        // produce a texture from the byte buffer
        GLU.gluBuild2DMipmaps(
        		target,
        		4,
        		get2Fold(bufferedImage.getWidth()),
        		get2Fold(bufferedImage.getHeight()),
        		srcPixelFormat,
        		GL11.GL_UNSIGNED_BYTE,
        		textureBuffer);
        /*GL11.glTexImage2D(target, 
                      0, 
                      dstPixelFormat, 
                      get2Fold(bufferedImage.getWidth()), 
                      get2Fold(bufferedImage.getHeight()), 
                      0, 
                      srcPixelFormat, 
                      GL11.GL_UNSIGNED_BYTE, 
                      textureBuffer );
        */
        texture.urlString = resourceName;
        return texture; 
    } 
    private int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    } 
    private ByteBuffer convertImageData(BufferedImage bufferedImage, Texture texture) { 
        ByteBuffer imageBuffer = null; 
        WritableRaster raster;
        BufferedImage texImage;
        
        int texWidth = 2;
        int texHeight = 2;
        
        // find the closest power of 2 for the width and height
        // of the produced texture
        while (texWidth < bufferedImage.getWidth()) {
            texWidth *= 2;
        }
        while (texHeight < bufferedImage.getHeight()) {
            texHeight *= 2;
        }
        
        texture.setTextureHeight(texHeight);
        texture.setTextureWidth(texWidth);
        
        // create a raster that can be used by OpenGL as a source
        // for a texture
        if (bufferedImage.getColorModel().hasAlpha()) {
        	raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
        	texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable<String, Object>());
            //texImage = new BufferedImage(texWidth, texHeight, BufferedImage.TYPE_4BYTE_ABGR);
        } else {
        	//texImage = new BufferedImage(texWidth, texHeight, BufferedImage.TYPE_3BYTE_BGR);
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
            texImage = new BufferedImage(glColorModel, raster, false, new Hashtable<String, Object>());
        }
            
        // copy the source image into the produced image
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f, 0f, 0f, 0f));
        g.fillRect(0, 0, texWidth, texHeight);
        g.drawImage(bufferedImage, 0, 0, null);
        
        // build a byte buffer from the temporary image 
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte)(texImage.getData().getDataBuffer())).getData();
        //byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData(); 

        imageBuffer = ByteBuffer.allocateDirect(data.length); 
        imageBuffer.order(ByteOrder.nativeOrder()); 
        imageBuffer.put(data, 0, data.length); 
        imageBuffer.flip();
        
        return imageBuffer; 
    } 
    private BufferedImage loadImage(String ref) 
    { 
    	//System.out.print("Loading " + ref);
    	try {
			BufferedImage i = ImageIO.read(new File(ref));
			if ( i == null ) {
				//System.out.println(" Error!");
				return null;
			} else {
				//System.out.println(" Done!");
			}
			return i;
        } catch ( Exception e) {
        	//e.printStackTrace();
        	return null;
        }
    }
    protected IntBuffer createIntBuffer(int size) {
      ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
      temp.order(ByteOrder.nativeOrder());

      return temp.asIntBuffer();
    }    
}

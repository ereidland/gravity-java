package com.evanreidland.e.client;

import java.util.Vector;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Game;
import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
import com.evanreidland.e.client.ent.ClientEnemy;
import com.evanreidland.e.client.ent.ClientLaser;
import com.evanreidland.e.client.ent.ClientShip;
import com.evanreidland.e.commands.enginescript;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.EntityList;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.event.Event;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.graphics.Model.ModelType;
import com.evanreidland.e.graphics.Quad;
import com.evanreidland.e.graphics.Sprite;
import com.evanreidland.e.graphics.font;
import com.evanreidland.e.graphics.generate;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;
import com.evanreidland.e.shared.config.ServerConfig;

public class GravityGame extends GameClientBase
{
	Resource font1, targetedTex, targetableTex;
	
	Model shipModel;
	
	Resource skybox;
	
	ClientShip ship;
	
	long nextShot;
	
	double viewHeight = 0.01, idleAngle = 0;
	
	Script script;
	String consoleText;
	long nextBackspace, nextFlash;
	boolean showConsole, flashing;
	
	int currentMenu = 0;
	
	Vector3 lastViewSize;
	
	public void onResize()
	{
	}
	
	public void updateConsole()
	{
		if (input.getTyped().length() > 0)
		{
			consoleText += input.getTyped();
			nextFlash = 0;
			flashing = false;
		}
		
		if (System.currentTimeMillis() >= nextFlash)
		{
			flashing = !flashing;
			nextFlash = System.currentTimeMillis() + 500;
		}
		
		if (input.isKeyDown(key.KEY_BACKSPACE))
		{
			nextBackspace = 0;
		}
		
		if (input.getKeyState(key.KEY_BACKSPACE)
				&& System.currentTimeMillis() > nextBackspace)
		{
			if (consoleText.length() > 1)
			{
				if (input.getKeyState(key.KEY_CONTROL))
				{
					int i = consoleText.lastIndexOf(' ');
					if (i > 0)
					{
						consoleText = consoleText.substring(0, i);
					}
					else
					{
						consoleText = "";
					}
				}
				else
				{
					consoleText = consoleText.substring(0,
							consoleText.length() - 1);
				}
				nextBackspace = System.currentTimeMillis()
						+ (nextBackspace == 0 ? 250 : 100);
			}
			else
			{
				consoleText = "";
			}
		}
		
		if (input.isKeyDown(key.KEY_ENTER) && consoleText.length() > 0)
		{
			engine.Log(">" + consoleText);
			String str = script.Execute(consoleText).toString();
			if (str.length() > 0)
			{
				engine.Log(": " + str);
			}
			consoleText = "";
		}
	}
	
	public void renderConsole(double ypos)
	{
		Quad q = new Quad();
		graphics.setTexture(null);
		q.vert[0].pos.setAs(graphics.camera.topLeft());
		q.vert[1].pos.setAs(graphics.camera.topRight());
		q.vert[2].pos.setAs(graphics.camera.bottomRight());
		q.vert[3].pos.setAs(graphics.camera.bottomLeft());
		q.setColor(0.25, 0.25, 0.25, 0.25);
		q.pass();
		
		double fontSize = 16;
		
		Vector<String> log = engine.getLog();
		font.r = font.g = font.a = 1;
		font.b = 0;
		for (int i = 0; i < log.size(); i++)
		{
			font.Render2d(
					font1,
					log.get(i),
					graphics.camera.bottomLeft().plus(
							new Vector3(0, ypos + (log.size() - i)
									* (fontSize + 4), 0)), fontSize, false);
		}
		font.Render2d(font1, ">" + consoleText + (flashing ? "_" : ""),
				graphics.camera.bottomLeft().plus(new Vector3(0, ypos, 0)),
				fontSize, false);
	}
	
	public void drawRing(Vector3 origin, double rad, int numPoints)
	{
		Vector3 lp = origin.plus(Vector3.fromAngle2d(0).multipliedBy(rad));
		for (int i = 1; i <= numPoints; i++)
		{
			Vector3 np = origin.plus(Vector3.fromAngle2d(
					(i / (double) numPoints) * engine.Pi2).multipliedBy(rad));
			graphics.drawLine(lp, np, 1, 1, 1, 1, 0.5);
			lp.setAs(np);
		}
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		
		if (lastViewSize.x != graphics.camera.width
				|| lastViewSize.y != graphics.camera.height)
		{
			lastViewSize.x = graphics.camera.width;
			lastViewSize.y = graphics.camera.height;
			onResize();
		}
		if (input.getKeyState(key.KEY_CONTROL)
				&& input.isKeyDown(key.KEY_ENTER))
		{
			showConsole = !showConsole;
		}
		if (showConsole)
		{
			updateConsole();
		}
		else if (currentMenu == 0 && ship != null)
		{
			double speed = 1;
			if (input.getKeyState(key.KEY_9))
			{
				viewHeight += speed * Game.getDelta();
			}
			if (input.getKeyState(key.KEY_8))
			{
				viewHeight -= speed * Game.getDelta();
			}
			if (viewHeight < 0.01)
				viewHeight = 0.01;
			if (viewHeight > 1)
				viewHeight = 1;
			
			Vector3 velThrust = Vector3.Zero(), angleThrust = Vector3.Zero();
			if (input.getKeyState(key.KEY_SHIFT))
			{
				if (input.getKeyState(key.KEY_UP))
				{
					velThrust.add(ship.angle.getForward());
				}
				if (input.getKeyState(key.KEY_DOWN))
				{
					velThrust.add(ship.angle.getForward().multipliedBy(-1));
				}
				if (input.getKeyState(key.KEY_LEFT))
				{
					velThrust.add(ship.angle.getRight().multipliedBy(-1));
				}
				if (input.getKeyState(key.KEY_RIGHT))
				{
					velThrust.add(ship.angle.getRight());
				}
			}
			else
			{
				if (input.getKeyState(key.KEY_CONTROL))
				{
					if (input.getKeyState(key.KEY_UP))
					{
						velThrust.add(ship.angle.getUp());
					}
					if (input.getKeyState(key.KEY_DOWN))
					{
						velThrust.add(ship.angle.getUp().multipliedBy(-1));
					}
					if (input.getKeyState(key.KEY_LEFT))
					{
						velThrust.add(ship.angle.getRight().multipliedBy(-1));
					}
					if (input.getKeyState(key.KEY_RIGHT))
					{
						velThrust.add(ship.angle.getRight());
					}
				}
				else
				{
					if (ship.angle.x < engine.Pi)
					{
						speed = -speed;
					}
					if (input.getKeyState(key.KEY_LEFT))
					{
						angleThrust.z -= speed;// Math.cos(ship.angle.y)*speed;
					}
					if (input.getKeyState(key.KEY_RIGHT))
					{
						angleThrust.z += speed;// Math.cos(ship.angle.y)*speed;
					}
					
					if (input.getKeyState(key.KEY_UP))
					{
						angleThrust.x += Math.abs(speed);
					}
					if (input.getKeyState(key.KEY_DOWN))
					{
						angleThrust.x -= Math.abs(speed);
					}
					
				}
			}
			ship.velThrust.setAs(velThrust);
			ship.angleThrust.setAs(angleThrust);
		}
		
		ents.list.onThink();
		
		if (ship != null)
		{
			idleAngle += Game.getDelta();
			graphics.camera.angle.setAs(ship.angle);
			graphics.camera.pos.setAs(ship.pos.plus(
					graphics.camera.getForward()
							.multipliedBy(-viewHeight * 2.5)).plus(
					graphics.camera.getUp().multipliedBy(viewHeight)));
		}
		else
		{
			idleAngle += Game.getDelta() * 0.1;
			graphics.camera.angle.setAs(new Vector3(engine.Pi_2, 0, idleAngle));
			graphics.camera.pos.setAs(Vector3.Zero());
		}
	}
	
	public void onRender()
	{
		super.onRender();
		graphics.drawSkybox(skybox, graphics.camera.farDist - 1);
		
		graphics.unbindTexture();
		
		graphics.scene.Render();
	}
	
	public void onRenderHUD()
	{
		super.onRenderHUD();
		double ypos = 16;
		if (ship != null)
		{
			font.Render2d(font1, "Pos: " + ship.pos.toRoundedString(),
					graphics.camera.bottomLeft().plus(0, ypos, 0), 16, false);
			ypos += 16;
			font.Render2d(font1, "Ang: "
					+ ship.angle.clipAngle().toRoundedString(), graphics.camera
					.bottomLeft().plus(0, ypos, 0), 16, false);
			ypos += 16;
			font.Render2d(font1, "Thrust: " + ship.velThrust.toRoundedString()
					+ ", " + ship.angleThrust.toRoundedString(),
					graphics.camera.bottomLeft().plus(0, ypos, 0), 16, false);
			ypos += 16;
		}
		font.Render2d(font1,
				"Delta: " + String.format("%03.03f", Game.getDelta()),
				graphics.camera.bottomLeft().plus(0, ypos, 0), 16, false);
		ypos += 16;
		
		if (ship != null)
		{
			EntityList list = ents.list.getWithFlags(new Flags(
					"enemy targetable"), true);
			Sprite sprite = new Sprite(32, 32, targetableTex);
			sprite.angle.z = idleAngle;
			for (int i = 0; i < list.size(); i++)
			{
				Entity ent = list.get(i);
				sprite.pos.setAs(graphics.toScreen(ent.pos));
				sprite.render2D();
			}
		}
		
		double s = 10;
		graphics.unbindTexture();
		
		Vector3 p = Game.mousePos;
		graphics.drawLine(p.plus(new Vector3(-s, 0, 0)),
				p.plus(new Vector3(0, -s, 0)), 2, 1, 1, 1, 0.5f);
		graphics.drawLine(p.plus(new Vector3(0, -s, 0)),
				p.plus(new Vector3(s, 0, 0)), 2, 1, 1, 1, 0.5f);
		graphics.drawLine(p.plus(new Vector3(s, 0, 0)),
				p.plus(new Vector3(0, s, 0)), 2, 1, 1, 1, 0.5f);
		graphics.drawLine(p.plus(new Vector3(0, s, 0)),
				p.plus(new Vector3(-s, 0, 0)), 2, 1, 1, 1, 0.5f);
		
		if (showConsole)
		{
			renderConsole(ypos);
		}
	}
	
	public void registerEntities()
	{
		ents.Register("explosion", Explosion.class);
		ents.Register("laser", ClientLaser.class);
		ents.Register("enemy", ClientEnemy.class);
		ents.Register("ship", ClientShip.class);
	}
	
	public void loadGraphics()
	{
		generate.setModelType(ModelType.RenderList);
		
		font.buildFont("Courier New", 32, true, false);
		font1 = engine.loadFont("Courier Newx32");
		
		skybox = engine.loadTexture("skybox1.png");
		targetableTex = engine.loadTexture("targetable.png");
	}
	
	public void loadSound()
	{
		// I'm going to have something here eventually.
	}
	
	public void buildGUI()
	{
		onResize();
		// Button button = new Button(128, 64, "button");
		// button.tex = engine.loadTexture("button1.png");
		// button.Position(graphics.camera.topLeft().minus(0,
		// button.rect.getHeight(), 0));
		// hud.addObject(button);
	}
	
	public void onInit()
	{
		lastViewSize = new Vector3(graphics.camera.width,
				graphics.camera.height, 0);
		engine.maxLogs = 40;
		super.onInit();
		ServerConfig.setupConfigs();
		
		Event.addListener(new GravityEntityListener());
		
		script = new Script();
		consoleText = "";
		nextBackspace = 0;
		nextFlash = 0;
		showConsole = false;
		flashing = false;
		
		basefunctions.registerAll(script.env);
		enginescript.registerAll(script.env);
		clientfunctions.registerAll(script.env);
		
		// Note: creates a static reference, GravityClient.global.
		new GravityClient(this);
		
		registerEntities();
		loadGraphics();
		loadSound();
		buildGUI();
		
		graphics.camera.farDist = 1000000000d;
	}
	
	public static void main(String[] args)
	{
		EApplication app = new EApplication(new EApplet());
		engine.game = new GravityGame();
		app.runApplet("Gravity Multiplayer Alpha v0.1", false);
	}
}

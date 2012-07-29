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
import com.evanreidland.e.client.gui.ChatTextField;
import com.evanreidland.e.client.gui.MessageArea;
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
import com.evanreidland.e.gui.hud;
import com.evanreidland.e.net.network;
import com.evanreidland.e.phys.Ray;
import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;
import com.evanreidland.e.shared.action.EntityMoveAction;
import com.evanreidland.e.shared.action.sharedactions;
import com.evanreidland.e.shared.config.ServerConfig;

public class GravityGame extends GameClientBase
{
	Resource font1, targetedTex, targetableTex;
	
	Model shipModel;
	
	Resource skybox;
	
	ClientShip ship;
	
	long nextShot;
	
	double viewHeight = 10, idleAngle = 0;
	
	Script script;
	String consoleText;
	long nextBackspace, nextFlash;
	boolean showConsole, flashing;
	
	int currentMenu = 0;
	
	Vector3 lastViewSize, targetPoint;
	
	ChatTextField textField;
	public MessageArea messageArea;
	
	public static GravityGame active;
	
	public void onResize()
	{
		textField.rect.moveTo(graphics.camera.bottomLeft().plus(
				textField.rect.getSize().multipliedBy(0.5).plus(0, 64, 0)));
		messageArea.rect.moveTo(textField.rect.getCenter().plus(
				0,
				textField.rect.getHeight() * 0.5 + messageArea.rect.getHeight()
						* 0.5, 0));
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
		textField.bActive = !showConsole;
		if (showConsole)
		{
			textField.text = "";
			textField.bFocused = false;
			updateConsole();
		}
		else
		{
			if (input.isKeyDown(key.MOUSE_RBUTTON))
			{
				Ray ray = new Ray(graphics.camera.pos,
						graphics.toWorld(new Vector3(Game.mousePos.x,
								Game.mousePos.y, 0)));
				targetPoint = ray.getPlaneIntersection(Vector3.Zero(),
						new Vector3(0, 0, 1));
			}
			if (input.isKeyDown(key.MOUSE_LBUTTON))
			{
				targetPoint = Vector3.Zero();
			}
			if (currentMenu == 0 && ship != null)
			{
				if (input.isKeyDown(key.MOUSE_RBUTTON))
				{
					GravityClient.global.requestAction(ship,
							new EntityMoveAction(ship, targetPoint));
				}
			}
		}
		
		ents.list.onThink();
		
		graphics.camera.pos.setAs(new Vector3(0, 0, viewHeight));
		graphics.camera.angle.setAs(Vector3.Zero().minus(graphics.camera.pos)
				.getAngle());
		
		idleAngle += Game.getDelta();
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
		font.r = font.g = font.b = font.a = 1;
		if (ship != null)
		{
			font.Render2d(font1, "Pos: " + ship.pos.toRoundedString(),
					graphics.camera.bottomLeft().plus(0, ypos, 0), 16, false);
			ypos += 16;
			font.Render2d(font1, "Ang: "
					+ ship.angle.clipAngle().toRoundedString(), graphics.camera
					.bottomLeft().plus(0, ypos, 0), 16, false);
			ypos += 16;
		}
		font.Render2d(font1,
				"Delta: " + String.format("%03.03f", Game.getDelta())
						+ " / p as " + targetPoint.toRoundedString(),
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
				p.plus(new Vector3(0, -s, 0)), 2, 1, 1, 1, 0.5);
		graphics.drawLine(p.plus(new Vector3(0, -s, 0)),
				p.plus(new Vector3(s, 0, 0)), 2, 1, 1, 1, 0.5);
		graphics.drawLine(p.plus(new Vector3(s, 0, 0)),
				p.plus(new Vector3(0, s, 0)), 2, 1, 1, 1, 0.5);
		graphics.drawLine(p.plus(new Vector3(0, s, 0)),
				p.plus(new Vector3(-s, 0, 0)), 2, 1, 1, 1, 0.5);
		
		p = graphics.toScreen(targetPoint);
		graphics.drawLine(p.plus(new Vector3(-s, 0, 0)),
				p.plus(new Vector3(0, -s, 0)), 2, 1, 1, 0.5, 0.5);
		graphics.drawLine(p.plus(new Vector3(0, -s, 0)),
				p.plus(new Vector3(s, 0, 0)), 2, 1, 1, 0.5, 0.5);
		graphics.drawLine(p.plus(new Vector3(s, 0, 0)),
				p.plus(new Vector3(0, s, 0)), 2, 1, 1, 0.5, 0.5);
		graphics.drawLine(p.plus(new Vector3(0, s, 0)),
				p.plus(new Vector3(-s, 0, 0)), 2, 1, 1, 0.5, 0.5);
		
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
		messageArea = new MessageArea("messages", 256, 256, 18);
		textField = new ChatTextField("chatfield", 256, 18, messageArea);
		
		hud.addObject(messageArea);
		hud.addObject(textField);
		onResize();
	}
	
	public void onInit()
	{
		active = this;
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
		
		targetPoint = Vector3.Zero();
		
		basefunctions.registerAll(script.env);
		enginescript.registerAll(script.env);
		clientfunctions.registerAll(script.env);
		sharedactions.registerAll();
		
		// Note: creates a static reference, GravityClient.global.
		network.setClient(new GravityClient(this));
		
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

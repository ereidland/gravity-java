package com.evanreidland.e.client;

import java.util.Vector;

import com.evanreidland.e.Flags;
import com.evanreidland.e.Game;
import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
<<<<<<< HEAD
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.EntityList;
import com.evanreidland.e.ent.SearchData;
=======
import com.evanreidland.e.client.ent.ClientEnemy;
import com.evanreidland.e.client.ent.ClientLaser;
import com.evanreidland.e.client.ent.ClientShip;
import com.evanreidland.e.commands.enginescript;
>>>>>>> development
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.event.Event;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.graphics.Model.ModelType;
import com.evanreidland.e.graphics.Quad;
import com.evanreidland.e.graphics.font;
import com.evanreidland.e.graphics.generate;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;
import com.evanreidland.e.shared.config.ServerConfig;

public class GravityGame extends GameClientBase
{
	Resource font1;
	
	Model shipModel;
	
	Resource skybox;
	
<<<<<<< HEAD
	Ship ship;
	
	long nextShot;
	
	double viewHeight = 0.01, deltaScaling = 1;
=======
	ClientShip ship;
	
	long nextShot;
	
	double viewHeight = 0.01, idleAngle = 0;
>>>>>>> development
	
	Script script;
	String consoleText;
	long nextBackspace, nextFlash;
	boolean showConsole, flashing;
	
	int currentMenu = 0;
	
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
	
<<<<<<< HEAD
	public void renderConsole()
=======
	public void renderConsole(double ypos)
>>>>>>> development
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
			
<<<<<<< HEAD
			if (input.getKeyState(key.KEY_3))
			{
				deltaScaling += getDelta() * 20;
			}
			if (input.getKeyState(key.KEY_2))
			{
				deltaScaling -= getDelta() * 20;
			}
			if (input.getKeyState(key.KEY_N))
			{
				deltaScaling = 1;
			}
			if (deltaScaling < 0.1)
				deltaScaling = 0.1f;
			if (deltaScaling > 100)
				deltaScaling = 100;
			
=======
>>>>>>> development
			// for ( int i = 0; i < ents.list.getSize(); i++ ) {
			// ents.list.get(i).relativity = deltaScaling; // For now while I
			// test this.
			// }
			
<<<<<<< HEAD
=======
			Vector3 velThrust = Vector3.Zero(), angleThrust = Vector3.Zero();
			
>>>>>>> development
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
<<<<<<< HEAD
						ship.vel.add(ship.angle.getRight().multipliedBy(speed));
					}
					
					if (input.isKeyDown(key.KEY_C))
					{
						ship.vel.setAs(0, 0, 0);
						ship.angleVel.setAs(0, 0, 0);
						ship.bStatic = !ship.bStatic;
=======
						velThrust.add(ship.angle.getRight());
>>>>>>> development
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
		
<<<<<<< HEAD
		// ents.list.simulateGravity(getDelta());
		ents.list.onThink();
		
		graphics.camera.angle.setAs(ship.angle);// planet.pos.minus(ship.pos).getAngle());
		graphics.camera.pos.setAs(ship.pos.plus(
				graphics.camera.getForward().multipliedBy(-viewHeight * 2.5))
				.plus(graphics.camera.getUp().multipliedBy(viewHeight)));
=======
		ents.list.onThink();
		
		if (ship != null)
		{
			
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
>>>>>>> development
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
<<<<<<< HEAD
				graphics.camera.bottomLeft().plus(0, 48, 0), 16, false);
=======
				graphics.camera.bottomLeft().plus(0, ypos, 0), 16, false);
		ypos += 16;
>>>>>>> development
		
		double s = 10;
		graphics.unbindTexture();
		
<<<<<<< HEAD
		EntityList list = ents.list.getWithFlags(new Flags("missile enemy"),
				true);
		for (int i = 0; i < list.size(); i++)
		{
			Vector3 p = graphics.toScreen(list.get(i).pos);
			graphics.drawLine(p.plus(new Vector3(-s, 0, 0)),
					p.plus(new Vector3(0, -s, 0)), 2, 1, 1, 1, 0.5f);
			graphics.drawLine(p.plus(new Vector3(0, -s, 0)),
					p.plus(new Vector3(s, 0, 0)), 2, 1, 1, 1, 0.5f);
			graphics.drawLine(p.plus(new Vector3(s, 0, 0)),
					p.plus(new Vector3(0, s, 0)), 2, 1, 1, 1, 0.5f);
			graphics.drawLine(p.plus(new Vector3(0, s, 0)),
					p.plus(new Vector3(-s, 0, 0)), 2, 1, 1, 1, 0.5f);
			
		}
		SearchData data = ents.list.findNearest(ship.pos, 1000, new Flags(
				"enemy targetable"));
		Vector3 p = data.isPositive ? graphics.toScreen(data.ent.pos)
				: Game.mousePos;
=======
		Vector3 p = Game.mousePos;
>>>>>>> development
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
	
<<<<<<< HEAD
	public void createEntities()
	{
		ship = (Ship) ents.Create("ship");
		
		ship.model = shipModel;
		ship.mass = 0.0001;
		ship.bStatic = false;
		ship.pos = new Vector3(2, 0, 0);
		ship.flags.add("player targetable");
		ship.Spawn();
		
		graphics.scene.addObject(new ModelSceneObject(shipModel), ship);
		
		nextShot = 0;
		
		int num = 20;
		for (int i = 0; i < num; i++)
		{
			Entity ent = ents.Create("enemy");
			ent.pos.setAs(Vector3.fromAngle2d((i / (double) num) * engine.Pi2)
					.multipliedBy(2));
			ent.mass = 0.0001;
			ent.Spawn();
		}
	}
	
	public void loadGraphics()
	{
		generate.setModelType(ModelType.RenderList);
		shipModel = generate.Cube(new Vector3(0, 0, 0), new Vector3(0.01f,
				0.01f, 0.01f), new Vector3());
		shipModel.tex = engine.loadTexture("shiptest1.png");
=======
	public void loadGraphics()
	{
		generate.setModelType(ModelType.RenderList);
>>>>>>> development
		
		font.buildFont("Courier New", 32, true, false);
		font1 = engine.loadFont("Courier Newx32");
		
		skybox = engine.loadTexture("skybox1.png");
	}
	
	public void loadSound()
	{
		// I'm going to have something here eventually.
	}
	
	public void buildGUI()
	{
		// Button button = new Button(128, 64, "button");
		// button.tex = engine.loadTexture("button1.png");
		// button.Position(graphics.camera.topLeft().minus(0,
		// button.rect.getHeight(), 0));
		// hud.addObject(button);
	}
	
	public void onInit()
	{
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
		
<<<<<<< HEAD
		// Note: these both create a static self-reference on construction.
		new GravityNetClient();
=======
		// Note: creates a static reference, GravityClient.global.
		new GravityClient(this);
>>>>>>> development
		
		registerEntities();
		loadGraphics();
		loadSound();
		buildGUI();
<<<<<<< HEAD
		createEntities();
=======
>>>>>>> development
		
		graphics.camera.farDist = 1000000000d;
	}
	
	public static void main(String[] args)
	{
		EApplication app = new EApplication(new EApplet());
		engine.game = new GravityGame();
		app.runApplet("Gravity Multiplayer Alpha v0.1", false);
	}
}

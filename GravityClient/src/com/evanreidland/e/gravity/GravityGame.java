package com.evanreidland.e.gravity;

import java.util.Vector;

import com.evanreidland.e.Game;
import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.client.EApplet;
import com.evanreidland.e.client.EApplication;
import com.evanreidland.e.client.GameClient;
import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
import com.evanreidland.e.client.ent.Ship;
import com.evanreidland.e.config.ServerConfig;
import com.evanreidland.e.console.serverfunctions;
import com.evanreidland.e.ent.Entity;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.event.Event;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.graphics.ModelSceneObject;
import com.evanreidland.e.graphics.Quad;
import com.evanreidland.e.graphics.font;
import com.evanreidland.e.graphics.generate;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.graphics.Model.ModelType;
import com.evanreidland.e.gravity.gui.Button;
import com.evanreidland.e.gui.hud;
import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;
import com.evanreidland.e.server.GravityServer;

public class GravityGame extends GameClient {
	Resource font1;
	
	Model shipModel;
	
	Resource skybox;
	
	Ship ship;
	
	long nextShot;
	
	double viewHeight = 0.01, deltaScaling = 1;
	
	Script script;
	String consoleText;
	long nextBackspace, nextFlash;
	boolean showConsole, flashing;
	
	public void updateConsole() {
		if ( input.getTyped().length() > 0 ) {
			consoleText += input.getTyped();
			nextFlash = 0;
			flashing = false;
		}
		
		if ( System.currentTimeMillis() >= nextFlash ) {
			flashing = !flashing;
			nextFlash = System.currentTimeMillis() + 500;
		}
		
		if ( input.isKeyDown(key.KEY_BACKSPACE) ) {
			nextBackspace = 0;
		}
		
		if ( input.getKeyState(key.KEY_BACKSPACE) && System.currentTimeMillis() > nextBackspace ) {
			if ( consoleText.length() > 1 ) {
				if ( input.getKeyState(key.KEY_CONTROL) ) {
					int i = consoleText.lastIndexOf(' ');
					if ( i > 0 ) {
						consoleText = consoleText.substring(0, i);
					} else {
						consoleText = "";
					}
				}
				else {
					consoleText = consoleText.substring(0, consoleText.length() - 1);
				}
				nextBackspace = System.currentTimeMillis() + (nextBackspace == 0 ? 250 : 100 ); 
			} else {
				consoleText = "";
			}
		}
		
		if ( input.isKeyDown(key.KEY_ENTER) && consoleText.length() > 0 ) {
			engine.Log(">" + consoleText);
			String str = script.Execute(consoleText).toString();
			if ( str.length() > 0 ) {
				engine.Log(": " + str);
			}
			consoleText = "";
		}
	}
	
	public void renderConsole() {
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
		
		for ( int i = 0; i < log.size(); i++ ) {
			font.Render2d(font1, log.get(i), graphics.camera.bottomLeft().plus(new Vector3(0, 64 + (log.size() - i)*(fontSize + 4), 0)), fontSize, false);
		}
		font.Render2d(font1, ">" + consoleText + (flashing ? "_" : ""), graphics.camera.bottomLeft().plus(new Vector3(0, 64, 0)), fontSize, false);
	}
	
	public void drawRing(Vector3 origin, double rad, int numPoints) {
		Vector3 lp = origin.plus(Vector3.fromAngle2d(0).multipliedBy(rad));
		for ( int i = 1; i <= numPoints; i++ ) {
			Vector3 np = origin.plus(Vector3.fromAngle2d((i/(double)numPoints)*engine.Pi2).multipliedBy(rad));
			graphics.drawLine(lp, np, 1, 1, 1, 1, 0.5);
			lp.setAs(np);
		}
	}
	
	public void onUpdate() {
		super.onUpdate();
		if ( input.getKeyState(key.KEY_CONTROL) && input.isKeyDown(key.KEY_ENTER) ) {
			showConsole = !showConsole;
		}
		if ( showConsole ) {
			updateConsole();
		} else {
			double speed = getDelta();
			if ( input.getKeyState(key.KEY_1) && Game.getTime() >= nextShot ) {
				Entity ent = ents.Create("missile",
						new Object[] {
							ship.pos.plus(ship.angle.getForward().multipliedBy(0.01)),
							ship.vel.plus(ship.angle.getForward().multipliedBy(2)),
							false,
							null,
							1d,
							10d,
						});
				ent.Spawn();
				nextShot = Game.getTime() + 100;
			}
			if ( input.getKeyState(key.KEY_9) ) {
				viewHeight += speed;
			}
			if ( input.getKeyState(key.KEY_8) ) {
				viewHeight -= speed;
			}
			if ( viewHeight < 0.01 ) viewHeight = 0.01;
			if ( viewHeight > 1 ) viewHeight = 1;
			
			if ( input.getKeyState(key.KEY_3) ) {
				deltaScaling += getDelta()*20;
			}
			if ( input.getKeyState(key.KEY_2) ) {
				deltaScaling -= getDelta()*20;
			}
			if ( input.getKeyState(key.KEY_N) ) {
				deltaScaling = 1;
			}
			if ( deltaScaling < 0.1 ) deltaScaling = 0.1f;
			if ( deltaScaling > 100 ) deltaScaling = 100;
			
			for ( int i = 0; i < ents.list.getSize(); i++ ) {
				ents.list.get(i).relativity = deltaScaling; // For now while I test this.
			}
			
			if ( input.getKeyState(key.KEY_SHIFT) ) {
				speed *= 2;
				if ( input.getKeyState(key.KEY_UP) ) {
					ship.vel.add(ship.angle.getForward().multipliedBy(speed));
				}
				if ( input.getKeyState(key.KEY_DOWN) ) {
					ship.vel.add(ship.angle.getForward().multipliedBy(-speed));
				}
				if ( input.getKeyState(key.KEY_LEFT) ) {
					ship.vel.add(ship.angle.getRight().multipliedBy(-speed));
				}
				if ( input.getKeyState(key.KEY_RIGHT) ) {
					ship.vel.add(ship.angle.getRight().multipliedBy(speed));
				}
			} else {
				if ( input.getKeyState(key.KEY_CONTROL) ) {
					speed *= 2;
					if ( input.getKeyState(key.KEY_UP) ) {
						ship.vel.add(ship.angle.getUp().multipliedBy(speed));
					}
					if ( input.getKeyState(key.KEY_DOWN) ) {
						ship.vel.add(ship.angle.getUp().multipliedBy(-speed));
					}
					if ( input.getKeyState(key.KEY_LEFT) ) {
						ship.vel.add(ship.angle.getRight().multipliedBy(-speed));
					}
					if ( input.getKeyState(key.KEY_RIGHT) ) {
						ship.vel.add(ship.angle.getRight().multipliedBy(speed));
					}
					
					if ( input.isKeyDown(key.KEY_C) ) {
						ship.vel.setAs(0, 0, 0);
						ship.angleVel.setAs(0, 0, 0);
						ship.bStatic = !ship.bStatic;
					}
				} else {
					if ( ship.angle.x < engine.Pi ) {
						speed = -speed;
					}
					if ( input.getKeyState(key.KEY_LEFT) ) {
						ship.angleVel.z -= speed;//Math.cos(ship.angle.y)*speed;
					}
					if ( input.getKeyState(key.KEY_RIGHT) ) {
						ship.angleVel.z += speed;//Math.cos(ship.angle.y)*speed;
					}
					
					if ( input.getKeyState(key.KEY_UP) ) {
						ship.angleVel.x += Math.abs(speed);
					}
					if ( input.getKeyState(key.KEY_DOWN) ) {
						ship.angleVel.x -= Math.abs(speed);
					}
				}
			}
		}
		
		ents.list.simulateGravity(getDelta());
		ents.list.onThink();
		
		graphics.camera.angle.setAs(ship.angle);//planet.pos.minus(ship.pos).getAngle());
		graphics.camera.pos.setAs(ship.pos.plus(graphics.camera.getForward().multipliedBy(-viewHeight*2.5)).plus(graphics.camera.getUp().multipliedBy(viewHeight)));
	}

	public void onRender() {
		super.onRender();
		graphics.drawSkybox(skybox, graphics.camera.farDist - 1);
	
		graphics.unbindTexture();
		
		graphics.scene.Render();
		ents.list.onRender();
	}

	public void onRenderHUD() {
		super.onRenderHUD();
		font.Render2d(font1, "Pos: " + ship.pos.toRoundedString(), graphics.camera.bottomLeft().plus(0, 16, 0), 16, false);
		font.Render2d(font1, "Ang: " + ship.angle.clipAngle().toRoundedString(), graphics.camera.bottomLeft().plus(0, 32, 0), 16, false);
		font.Render2d(font1, "Delta: " + String.format("%03.03f", Game.getDelta()), graphics.camera.bottomLeft().plus(0, 48, 0), 16, false);
		
		double s = 10;
		graphics.unbindTexture();
		
		Vector3 p = Game.mousePos;
		graphics.drawLine(p.plus(new Vector3(-s, 0, 0)), p.plus(new Vector3(0, -s, 0)), 2, 1, 1, 1, 0.5f);
		graphics.drawLine(p.plus(new Vector3(0, -s, 0)), p.plus(new Vector3(s, 0, 0)), 2, 1, 1, 1, 0.5f);
		graphics.drawLine(p.plus(new Vector3(s, 0, 0)), p.plus(new Vector3(0, s, 0)), 2, 1, 1, 1, 0.5f);
		graphics.drawLine(p.plus(new Vector3(0, s, 0)), p.plus(new Vector3(-s, 0, 0)), 2, 1, 1, 1, 0.5f);
		
		if ( showConsole ) {
			renderConsole();
		}
	}
	
	public void registerEntities() {
		ents.Register("missile", TestInterceptor.class);
		ents.Register("explosion", Explosion.class);
		ents.Register("enemy", TestEnemy.class);
	}
	
	public void createEntities() { 
		ship = (Ship)ents.Create("ship");
		
		ship.model = shipModel;
		ship.mass = 0.0001;
		ship.bStatic = false;
		ship.pos = new Vector3(2, 0, 0);
		ship.flags.add("player targetable");
		
		graphics.scene.addObject(new ModelSceneObject(shipModel), ship);
		
		nextShot = 0;
		
		int num = 20;
		for ( int i = 0; i < num; i++ ) {
			Entity ent = ents.Create("enemy");
			ent.pos.setAs(Vector3.fromAngle2d((i/(double)num)*engine.Pi2).multipliedBy(2));
			ent.mass = 0.0001;
			ent.Spawn();
		}
	}
	
	public void loadGraphics() {
		generate.setModelType(ModelType.RenderList);
		shipModel = generate.Cube(new Vector3(0, 0, 0), new Vector3(0.01f, 0.01f, 0.01f), new Vector3());
		shipModel.tex = engine.loadTexture("shiptest1.png");
		
		font.buildFont("Courier New", 32, true, false);
		font1 = engine.loadFont("Courier Newx32");
		
		skybox = engine.loadTexture("skybox1.png");
	}
	
	public void loadSound() {
		//I'm going to have something here eventually.
	}
	
	public void buildGUI() {
		 Button button = new Button(128, 64, "button");
		 button.tex = engine.loadTexture("button1.png");
		 button.Position(graphics.camera.topLeft().minus(0, button.rect.getHeight(), 0));
		 hud.addObject(button);
	}
	
	public void onInit() {
		super.onInit();
		ServerConfig.setupConfigs();
		
		Event.addListener(new EntityListener());
		
		script = new Script();
		consoleText = "";
		nextBackspace = 0;
		nextFlash = 0;
		showConsole = false;
		flashing = false;
		
		basefunctions.registerAll(script.env);
		serverfunctions.registerAll(script.env);
		clientfunctions.registerAll(script.env);
		
		//Note: these both create a static self-reference on construction.
		new GravityServer();
		new GravityNetClient();
		
		registerEntities();
		loadGraphics();
		loadSound();
		buildGUI();
		createEntities();
		
		graphics.camera.farDist = 1000000000d;
	}
	
	public static void main(String[] args) {
		EApplication app = new EApplication(new EApplet());
		engine.game = new GravityGame();
		app.runApplet("Gravity Alpha v0.1", true);
	}
}

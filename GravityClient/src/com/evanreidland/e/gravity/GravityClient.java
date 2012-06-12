package com.evanreidland.e.gravity;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;
import com.evanreidland.e.engine;
import com.evanreidland.e.client.EApplet;
import com.evanreidland.e.client.EApplication;
import com.evanreidland.e.client.GameClient;
import com.evanreidland.e.client.control.input;
import com.evanreidland.e.client.control.key;
import com.evanreidland.e.client.ent.Planet;
import com.evanreidland.e.client.ent.Ship;
import com.evanreidland.e.ent.ents;
import com.evanreidland.e.graphics.Model;
import com.evanreidland.e.graphics.Sprite;
import com.evanreidland.e.graphics.font;
import com.evanreidland.e.graphics.generate;
import com.evanreidland.e.graphics.graphics;
import com.evanreidland.e.graphics.Model.ModelType;

public class GravityClient extends GameClient {
	Resource font1;
	
	Model shipModel;
	
	Sprite planetSprite;
	
	Ship ship;
	Planet planet;
	
	public void onUpdate() {
		float speed = 2*getDelta();
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
			if ( input.getKeyState(key.KEY_UP) ) {
				ship.angle.x += speed;
			}
			if ( input.getKeyState(key.KEY_DOWN) ) {
				ship.angle.x -= speed;
			}
			//ship.angle.clipAngle();
			
//			if ( input.getKeyState(key.KEY_CONTROL) ) {
//				if ( input.getKeyState(key.KEY_LEFT) ) {
//					ship.angle.y -= speed;
//				}
//				if ( input.getKeyState(key.KEY_RIGHT) ) {
//					ship.angle.y += speed;
//				}
//			} else {
				if ( ship.angle.x < engine.Pi ) {
					speed = -speed;
				}
				if ( input.getKeyState(key.KEY_LEFT) ) {
					ship.angle.z -= speed;//Math.cos(ship.angle.y)*speed;
				}
				if ( input.getKeyState(key.KEY_RIGHT) ) {
					ship.angle.z += speed;//Math.cos(ship.angle.y)*speed;
				}
//			}
		}
		
		if ( input.getKeyState(key.KEY_SPACE) ) { 
			ship.angle = planet.pos.minus(ship.pos).getAngle();
		}
		
		ents.list.simulateGravity(getDelta());
		ents.list.onThink();
		
		graphics.camera.angle.setAs(ship.angle);//planet.pos.minus(ship.pos).getAngle());
		graphics.camera.pos.setAs(ship.pos.plus(graphics.camera.getForward().multipliedBy(-5)));
	}

	public void onRender() {
		ents.list.onRender();
	}

	public void onRenderHUD() {
		font.Render2d(font1, "Pos: " + ship.pos.toRoundedString(), graphics.camera.bottomLeft().plus(0, 16, 0), 16, false);
		font.Render2d(font1, "Ang: " + ship.angle.clipAngle().toRoundedString(), graphics.camera.bottomLeft().plus(0, 32, 0), 16, false);
	}
	
	public void registerEntities() {
		ents.Register("ship", Ship.class);
	}
	
	public void createEntities() {
		ship = (Ship)ents.Create("ship");
		planet = (Planet)ents.Create("planet");
		
		ship.model = shipModel;
		ship.mass = 1;
		ship.bStatic = false;
		
		ship.pos = new Vector3(15, 0, 0);
		
		planet.mass = 100;
		planet.radius = 5;
		planet.sprite = planetSprite;
		planet.bStatic = false;
		
		ship.vel = new Vector3(0, ship.getOrbitalVelocity(ship.pos.x, planet.mass), 0);
		
		float num = 10;
		
		for ( float i = 0; i < num; i++ ) {
			Planet ent = (Planet)ents.Create("planet");
			
			ent.sprite = planet.sprite;
			ent.radius = 1;
			ent.mass = 0.00001f;
			
			float rad = 10;
		
			ent.bStatic = false;
			
			float angle = (i/num)*engine.Pi2;
			
			ent.vel = new Vector3((float)Math.cos(angle + engine.Pi_2), (float)Math.sin(angle + engine.Pi_2), 0)
			.multipliedBy(ent.getOrbitalVelocity(rad, planet.mass));
			
			ent.pos.setAs(planet.pos.plus(Vector3.fromAngle2d(angle).multipliedBy(rad)));
			
			ent.pos.z = (float)Math.sin(angle*4)*2;
		}
		
		ship.angle = planet.pos.minus(ship.pos).getAngle();
	}
	
	public void loadGraphics() {
		planetSprite = new Sprite(32, 32, engine.loadTexture("planet1.png"));
		
		generate.setModelType(ModelType.RenderList);
		shipModel = generate.Cube(new Vector3(0, 0, 0), new Vector3(1, 1, 1), new Vector3());
		shipModel.tex = graphics.loadTexture("shiptest1.png");
		
		font.buildFont("Courier New", 32, true, false);
		font1 = engine.loadFont("Courier Newx32");
	}
	
	public void loadSound() {
		
	}
	
	public void onInit() {
		super.onInit();
		
		registerEntities();
		loadGraphics();
		loadSound();
		createEntities();
	}
	
	public static void main(String[] args) {
		EApplication app = new EApplication(new EApplet());
		engine.game = new GravityClient();
		app.runApplet("Gravity Alpha v0.1");
	}
}

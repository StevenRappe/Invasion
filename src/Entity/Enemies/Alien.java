package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Entity.Enemy;
import Handlers.Content;
import TileMap.TileMap;

public class Alien extends Enemy {
	
	private BufferedImage[] sprites;
	
	public Alien(TileMap tm) {
		super(tm);
		
		TYPE = 1;
		moveSpeed = 0.3;
		maxSpeed = 0.6;
		fallSpeed = 0.2;
		maxFallSpeed = 1.0;
		
		width = 30;
		height = 30;
		cWidth = 20;
		cHeight = 30;
		
		health = maxHealth = 10;
		damage = 1;
		
		sprites = Content.Alien[0];
		animation.setFrames(sprites);
		animation.setDelay(5);
		
		left = true;
		facingRight = right = false;
	}
	
	private void getNextPosition() {
		//movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		if(falling) {
			dy += fallSpeed;
		}
	}
	
	public void update() {
		
		if(flinching) {
			flinchTimer++;
			if(flinchTimer == 100) flinching = false;
		}
		
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);
		
		//check for wall hits and change direction
		calculateCorners(x, yDest + 1);
		if(!bottomLeft) {
			left = false;
			right = facingRight = true;
		}
		else if(!bottomRight) {
			left = true;
			right = facingRight = false;
		}
		 
		else if(dx == 0 && facingRight) {
			left = true;
			right = facingRight = false;
		}
		else if(dx == 0 && left){
			left = false;
			right = facingRight = true;
		}
		
		animation.update();
	}
	
	public void render(Graphics2D g) {
		
		setMapPosition();
		
		if(flinching) {
			if(flinchTimer % 10 < 5) return;
		}

		super.render(g);
	}
}

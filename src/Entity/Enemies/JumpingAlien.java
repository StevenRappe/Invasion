package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Enemy;
import TileMap.TileMap;

public class JumpingAlien extends Enemy {
	
	private BufferedImage[] sprites;
	
	public JumpingAlien(TileMap tm) {
		super(tm);
		
		
		moveSpeed = 0.3;
		maxSpeed = 0.6;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 40;
		height = 40;
		cWidth = 20;
		cHeight = 40;
		
		health = maxHealth = 10;
		damage = 1;
		
		//load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Enemies/JumpingAlien.png"));
			
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		animation.setFrames(sprites);
		animation.setDelay(5);
		
		left = true;
		facingRight = right = false;
	}
	
	private void getNextPosition() {
		// jump 
		
		if(falling) {
			dy += fallSpeed;
		}
		else {
			dy -= 5;
			if (dy == 50) {
				dy += fallSpeed;
			}
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




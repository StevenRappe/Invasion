package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;

public class Aliensoldier extends Enemy {
	
	private BufferedImage[] sprites;
	private BufferedImage[] shootSprites;
	private int tickCount;
	private Player player;
	private ArrayList<Enemy> enemies;
	private boolean isShooting;
	
	public Aliensoldier(TileMap tm, Player player, ArrayList<Enemy> enemies) {
		super(tm);
		
		this.player = player;
		this.enemies = enemies;
		
		moveSpeed = 0.3;
		maxSpeed = 0.6;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 40;
		height = 40;
		cWidth = 40;
		cHeight = 40;
		
		health = maxHealth = 15;
		damage = 1;
		
		isShooting=false;
		
		//load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Enemies/aliensoldier.png"));
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 40, width, height);
			}
			
			shootSprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				shootSprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		animation.setFrames(sprites);
		animation.setDelay(10);
		
		
		tickCount = 0;
		left = true;
		facingRight = right = false;
	}
	
	
	private void getNextPosition() {
		//movement
		if(isShooting==false){
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
		}
			if(falling) {
				dy += fallSpeed;
			}
		
	}
	
	
	public void update() {
		//animation.setFrames(sprites);
		//animation.setDelay(8);
		tickCount++;
		
		if(flinching) {
			flinchTimer++;
			if(flinchTimer == 100) flinching = false;
		}
		
		if( tickCount % 60 == 0 && (player.getX() + 60 >= (int)(x - 60) && player.getX() < x + 120) ) {		
			dx=0;
			left=right=false;
			EnergyFire ef = new EnergyFire(tileMap);
			if(player.getX() < x && !facingRight) {
				
				animation.setFrames(shootSprites);
				animation.setDelay(18);
				/*
				dx=0;
				left=false;
				right=false;
				facingRight=false;
				*/
				ef.setType(EnergyFire.VECTOR);
				ef.setPosition(x, y);
				ef.setVector(-2, -0.01);
				
			}
			else if(player.getX() > x && facingRight) {
				
				animation.setFrames(shootSprites);
				animation.setDelay(15);
				/*
				dx=0;
				left=false;
				right=false;
				facingRight=false;
				*/
				
				ef.setType(EnergyFire.VECTOR);
				ef.setPosition(x, y);
				ef.setVector(2, -0.01);
			}
				
			enemies.add(ef);
			
		}
		
		else if (player.getX() + 60 >= (int)(x - 60) && player.getX() < x + 120) {
			if(player.getX() > x) {
				left = false;
				facingRight = right = true;
			}
			else if(player.getX() < x) {
				left = true;
				facingRight = right = false;
			}
				
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

package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import Entity.Enemy;
import Entity.Player;
import Handlers.Content;
import TileMap.TileMap;

public class AlienShooter extends Enemy {
	
	private BufferedImage[] sprites;
	private int tickCount;
	private Player player;
	private ArrayList<Enemy> enemies;
	
	public AlienShooter(TileMap tm, Player player, ArrayList<Enemy> enemies) {
		super(tm);
		
		this.player = player;
		this.enemies = enemies;
		
		moveSpeed = 0.3;
		maxSpeed = 0.6;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = height = 40;
		cWidth = 25;
		cHeight = 40;
		
		health = maxHealth = 10;
		damage = 1;
		
		sprites = Content.AlienShooter[0];
		animation.setFrames(sprites);
		animation.setDelay(8);
		
		tickCount = 0;
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
		tickCount++;
		
		if(flinching) {
			flinchTimer++;
			if(flinchTimer == 100) flinching = false;
		}
		
		if( tickCount % 60 == 0 && (player.getX() + 60 >= (int)(x - 60) && player.getX() < x + 120) ) {		
			EnergyFire ef = new EnergyFire(tileMap);
			if(player.getX() < x && !facingRight && player.getY() == y) {
				ef.setType(EnergyFire.VECTOR);
				ef.setPosition(x, y);
				ef.setVector(-2, -0.01);
			}
			else if(player.getX() > x && facingRight && player.getY() == y) {
				ef.setType(EnergyFire.VECTOR);
				ef.setPosition(x, y);
				ef.setVector(2, -0.01);
			}
				
			enemies.add(ef);
		}
		
		if (player.getX() + 60 >= (int)(x - 60) && player.getX() < x + 120) {
			if(player.getX() < x && player.getY() == y) {
				left = true;
				facingRight = right = false;
			}
			else if(player.getX() > x && player.getY() == y) {
				left = false;
				facingRight = right = true;
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

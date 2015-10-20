package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;

public class TripleShooter extends Enemy {
	
	private BufferedImage[] sprites;
	private int tickCount;
	private Player player;
	private ArrayList<Enemy> enemies;

	public TripleShooter(TileMap tm, Player player, ArrayList<Enemy> enemies) {
	super(tm);
	this.player = player;
	this.enemies = enemies;
		moveSpeed = 0.3;
		maxSpeed = 0.6;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 40;
		height = 40;
		cWidth = 25;
		cHeight = 40;
		
		health = maxHealth = 10;
		damage = 1;
		
		//load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Enemies/TripleShooter.png"));
			
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
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
			EnergyFire lf = new EnergyFire(tileMap);
			EnergyFire rf = new EnergyFire(tileMap);
			EnergyFire urf = new EnergyFire(tileMap);
			EnergyFire ulf = new EnergyFire(tileMap);
			EnergyFire uf = new EnergyFire(tileMap);
			//if(player.getX() < x && !facingRight || player.getX() > x && facingRight) {
				lf.setType(EnergyFire.VECTOR);
				lf.setPosition(x, y);
				lf.setVector(-2, -0.01);
			//}
			//if(player.getX() < x && !facingRight || player.getX() > x && facingRight) {
				rf.setType(EnergyFire.VECTOR);
				rf.setPosition(x, y);
				rf.setVector(2, -0.01);
				
				ulf.setType(EnergyFire.VECTOR);
				ulf.setPosition(x, y);
				ulf.setVector(-2, -2);
				
				urf.setType(EnergyFire.VECTOR);
				urf.setPosition(x, y);
				urf.setVector(2, -2);
				
				uf.setType(EnergyFire.VECTOR);
				uf.setPosition(x, y);
				uf.setVector(0.00001, -2);
				
				//ef.setType(EnergyFire.VECTOR);
				//ef.setPosition(x,y);
			//}
				
		enemies.add(lf);
		enemies.add(rf);
		enemies.add(urf);
		enemies.add(ulf);
		enemies.add(uf);
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


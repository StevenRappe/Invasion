package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;

public class UFO extends Enemy{
	
	private BufferedImage[] sprites;
	
	private int tickCount;
	//private double a;
	private double b;
	private Player player;
	private ArrayList<Enemy> enemies;
	private boolean specialType;
	private int specialBulletCount;
	
	public UFO(TileMap tileMap, Player player, ArrayList<Enemy> enemies) {
		
		super(tileMap);
		this.player = player;
		this.enemies = enemies;
		
		health = maxHealth = 20;
		
		width = 120;
		height = 60;
		cWidth = 120;
		cHeight = 40;
		
		damage = 1;
		moveSpeed = 5;
		
		facingRight = true;
		falling = false;
		
		TYPE = 2;
		
		try {
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Enemies/ufo2.png"));
			
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		animation.setFrames(sprites);
		animation.setDelay(5);
		
		tickCount = 0;
		//a = Math.random() * 0.03 + 0.09;
		b = Math.random() * 0.03 + 0.09;
	}
	
	public void setSpecial(boolean b, int i) {
		specialType = true;
		specialBulletCount = i;
	}
	
	public void setY(int i) {
		for(int j = 0; j < i; j++) {
			y += j / 10;
		}
	}
	
	public void update() {
		tickCount++;
		
		if(flinching) {
			flinchTimer++;
			if(flinchTimer == 100) flinching = false;
		}
		
		//x = Math.sin(a * tickCount) + x;
		y = Math.sin(b * tickCount) + y;
		
		if(specialType && specialBulletCount == 1 && (player.getX() + 20 >= (int)(x - 60) && player.getX() < x + 120)) {
			specialBulletCount++;
			EnergyFire ef = new EnergyFire(tileMap);
			ef.setType(EnergyFire.VECTOR);
			ef.setPosition(x, y + 30);
			if(player.getX() < x)
				ef.setVector(-(x - player.getX()) / tileMap.getTileSize(), Math.abs(y - player.getY()) / tileMap.getTileSize());
			else
				ef.setVector(Math.abs(x - player.getX()) / tileMap.getTileSize(), Math.abs(y - player.getY()) / tileMap.getTileSize());
			enemies.add(ef);
		}
		
		if( !specialType && tickCount % 30 == 0 && (player.getX() + 60 >= (int)(x - 60) && player.getX() < x + 120) ) {
			
			EnergyFire ef = new EnergyFire(tileMap);
			ef.setType(EnergyFire.VECTOR);
			ef.setPosition(x, y + 30);
			if(player.getX() < x)
				ef.setVector(-(x - player.getX()) / tileMap.getTileSize(), Math.abs(y - player.getY()) / tileMap.getTileSize());
			else
				ef.setVector(Math.abs(x - player.getX()) / tileMap.getTileSize(), Math.abs(y - player.getY()) / tileMap.getTileSize());
			enemies.add(ef);
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

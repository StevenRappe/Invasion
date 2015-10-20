package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entity.Enemy;
import Handlers.Content;
import TileMap.TileMap;

public class EnergyFire extends Enemy {
	
	private BufferedImage[] sprites;
	
	private int count;
	private int type = 0;
	public static int VECTOR = 0;
	public static int STRAIGHT = 0;
	
	public EnergyFire(TileMap tm) {
		super(tm);
		
		width = 8;
		height = 8;
		cWidth = 8;
		cHeight = 8;
		
		damage = 2;
		
		count = 0;
		TYPE = 1;
		
		sprites = Content.EnergyFire[0];
		animation.setFrames(sprites);
	}
	
	public void setType(int i) { type = i; }
	
	public void update() {
		count++;
		
		checkTileMapCollision();
		setPosition(xTemp, yTemp); 
		
		if(x < 0 || x > tileMap.getWidth() || y < 0 || y > tileMap.getHeight() || dx == 0 || dy == 0 || count == 30) {
			remove = true;
		}
		
		if(type == VECTOR) {
			x += dx;
			y += dy;
		}
		
		else if(type == STRAIGHT) {
			moveSpeed = 1;
			if(right) dx = moveSpeed;
			else dx = -moveSpeed;
		}
	}
	
	public void render(Graphics2D g) {
		
		setMapPosition();
		
		super.render(g);
	}
	
}

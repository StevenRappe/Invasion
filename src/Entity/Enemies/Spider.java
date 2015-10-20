package Entity.Enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Enemy;
import Main.GamePanel;
import TileMap.TileMap;

public class Spider extends Enemy {
	
	private BufferedImage[] sprites;
	private boolean lowering;
	private boolean rising;
	private int startPosY;
	
	public Spider(TileMap tilemap) {
		super(tilemap);
		
		
		fallSpeed = 0.02;
		maxFallSpeed = 0.02;
		
		width = 30;
		height = 30;
		cWidth = 20;
		cHeight = 30;
		
		try {
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Enemies/arachnik.gif"));
			
			sprites = new BufferedImage[1];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		lowering = true;
	}
	
	public void setStartY(int i) { startPosY = i; }
	
	private void getNextPosition() {
		
		//System.out.println(startPosY + " " + (startPosY + 60) + " " + (int)y);
		
		if(startPosY == (int)y && rising) {
			lowering = true;
			dy = 0;
			rising = false;
		}
		
		if(startPosY + 60 < (int)y) {
			lowering = false;
			dy = 0;
			rising = true;
		} 
		
		else if(lowering) {
			dy += 0.001;
		}
		
		if(rising) {
			dy -= 0.001;
		}
	}
	
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);
		
		System.out.println((int)(Math.abs(yMap / 1.5 - y)));
	}
	
	public void render(Graphics2D g) {
		
		setMapPosition();
		
		g.setColor(Color.WHITE);
		g.drawLine((int)(this.x + xMap), (int)(Math.abs((yMap / 2) - y)), (int)(this.x + xMap), (int)(this.y + yMap));
		
		super.render(g);
	}
}

package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Handlers.Content;
import TileMap.TileMap;

public class HealthOrb extends MapObject {
	
	private BufferedImage[] sprites;
	private boolean remove;

	public HealthOrb(TileMap tileMap, int x, int y) {
		super(tileMap);
		
		height = width = 15;
		cWidth = 15;
		cHeight = 15;
		fallSpeed = maxFallSpeed = 0.2;
		
		this.x = x;
		this.y = y;
		
		sprites = Content.HealthOrb[0];
		animation.setFrames(sprites);
		animation.setDelay(5);
		
		falling = true;
	}
	
	private void getNextPosition() {
		if(falling) {
			dy += fallSpeed;
		}
	}
	
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void setRemove() { remove = true; } 
	
	public void render(Graphics2D g) {
		
		setMapPosition();
		
		super.render(g);
	}

}

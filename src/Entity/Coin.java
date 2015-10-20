package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Handlers.Content;
import TileMap.TileMap;

public class Coin extends MapObject {
	private BufferedImage[] sprites;
	private boolean remove;
	
	public Coin(TileMap tm) {
		super(tm);
		
		height = width = 15;
		cWidth = 15;
		cHeight = 15;

		sprites = Content.Coin[0];
		animation.setFrames(sprites);
		animation.setDelay(5);
	}
	
	public void update() {
		animation.update();
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void setRemove() { remove = true; } 
	
	public void render(Graphics2D g) {
		setMapPosition();
		super.render(g);
	}
}

package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Handlers.Content;
import TileMap.TileMap;

public class Door extends MapObject {
	
	private BufferedImage[] sprite;
	
	public Door(TileMap tileMap) {
		super(tileMap);
		
		height = width = 40;
		cWidth = 35;
		cHeight = 40;
		
		sprite = Content.Door[0];
		animation.setFrames(sprite);
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		super.render(g);
	}
}

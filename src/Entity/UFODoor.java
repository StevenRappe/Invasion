package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Handlers.Content;
import TileMap.TileMap;

public class UFODoor extends MapObject {
	
	private BufferedImage[] sprite;
	
	public UFODoor(TileMap tileMap) {
		super(tileMap);
		
		width = height = 40;
		cWidth = 40;
		cHeight = 40;
		
		sprite = Content.ufoDoor[0];
		animation.setFrames(sprite);
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		super.render(g);
	}
}
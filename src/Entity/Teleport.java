package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Handlers.Content;
import TileMap.TileMap;

public class Teleport extends MapObject {
	
	private BufferedImage[] sprite;
	
	public Teleport(TileMap tileMap) {
		super(tileMap);
		
		width = height = 40;
		cWidth = 30;
		cHeight = 40;
		
		sprite = Content.Teleport[0];
		animation.setFrames(sprite);
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		super.render(g);
	}
}

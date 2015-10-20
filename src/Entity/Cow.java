package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Cow extends MapObject {

	private BufferedImage[] sprite;
	
	public Cow(TileMap tileMap) {
		super(tileMap);
		
		width =  60;
		height = 40;
		cWidth = 60;
		cHeight = 40;
		
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/other/cow.png")
			);
			sprite = new BufferedImage[1];
			for(int i = 0; i < sprite.length; i++) {
				sprite[i] = spritesheet.getSubimage(
					i * width, 0, width, height
				);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		animation.setFrames(sprite);
	}
	
	public void update() {
		
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		super.render(g);
	}
}

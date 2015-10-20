package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Lightening extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	public Lightening(TileMap tileMap, boolean right) {
		
		super(tileMap);
		
		facingRight = right;
		
		moveSpeed = 3.8;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		width = 30;
		height = 30;
		cWidth = 14;
		cHeight = 14;
		
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/fireball.png"));
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i*width, 0, width, height);
			}
			
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spriteSheet.getSubimage(i*width, height, width, height);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		
		checkTileMapCollision();
		setPosition(xTemp, yTemp);
		
		if(dx == 0 && !hit) {
			setHit();
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public void render(Graphics2D g) {
		
		setMapPosition();
		
		super.render(g);
	}
	
}

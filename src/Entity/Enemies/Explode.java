package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import Entity.MapObject;
import Handlers.Content;
import TileMap.TileMap;


public class Explode extends MapObject {
	
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	private Point[] points;
	private int speed;
	private double diagSpeed;
	
	public Explode(TileMap tm, int x, int y) {
		
		super(tm);
		
		this.x = x;
		this.y = y;
		
		width = 30;
		height = 30;
		
		speed = 3;
		diagSpeed = 2;
		
		sprites = Content.Explosion[0];
		
		animation.setFrames(sprites);
		animation.setDelay(6);
		
		points = new Point[8];
		for(int i = 0; i < points.length; i++) {
			points[i] = new Point(x, y);
		}
		
	}
	
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
		points[0].x += speed;
		points[1].x += diagSpeed;
		points[1].y += diagSpeed;
		points[2].y += speed;
		points[3].x -= diagSpeed;
		points[3].y += diagSpeed;
		points[4].x -= speed;
		points[5].x -= diagSpeed;
		points[5].y -= diagSpeed;
		points[6].y -= speed;
		points[7].x += diagSpeed;
		points[7].y -= diagSpeed;
		
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void render(Graphics2D g) {
		setMapPosition();
		for(int i = 0; i < points.length; i++) {
			g.drawImage(
				animation.getImage(),
				(int) (points[i].x + xMap - width / 2),
				(int) (points[i].y + yMap - height / 2),
				null
			);
		}
	}
	
}

package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Handlers.Content;
import TileMap.TileMap;

public class Particle extends MapObject {
	
	private int count;
	private boolean remove;
	
	private BufferedImage[] sprites;
	
	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	
	public static int RED = 0;
	public static int BLUE = 1;
	public static int PURPLE = 2;
	public static int YELLOW = 3;
	
	public Particle(TileMap tm, double x, double y, int dir, int color) {
		super(tm);
		
		this.x = x;
		this.y = y;
		double d1 = Math.random() * 2.5 - 1.25;
		double d2 = -Math.random() - 0.8;
		if(dir == UP) {
			dx = d1;
			dy = d2;
		}
		else if(dir == LEFT) {
			dx = d2;
			dy = d1;
		}
		else if(dir == DOWN) {
			dx = d1;
			dy = -d2;
		}
		else {
			dx = -d2;
			dy = d1;
		}
		
		count = 0;
		if(color == RED)
			sprites = Content.ParticleRed[0];
		if(color == BLUE)
			sprites = Content.ParticleBlue[0];
		if(color == PURPLE)
			sprites = Content.ParticlePurple[0];
		if(color == YELLOW)
			sprites = Content.ParticleYellow[0];
		
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	public void update() {
		x += dx;
		y += dy;
		count++;
		if(count == 60) remove = true;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void render(Graphics2D g) {
		//setMapPosition();
		super.render(g);
	}

}

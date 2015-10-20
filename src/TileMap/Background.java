package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

/*
 * This class builds the background for game states. It can be used by any state to draw a 
 * background to the screen. We need to draw something to the background, or set it as a solid color constantly
 * */

public class Background {
		
	private BufferedImage image;
	
	//positioning
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	//parallaxing of the screen / used to make movement look realistic
	private double moveScale;
	
	//Constructor
	public Background(String s, double ms) {
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			moveScale = ms;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//sets the position of the background on the screen
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	//sets the vector of the screen
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	//update background compared to camera position
	public void update() {
		x += dx;
		y += dy;
	}
	
	//render the background, if the background moves we must adjust here
	public void render(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, null);
		if(x < 0) {
			g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
		}
		if(x > 0) {
			g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
		}
	}
}

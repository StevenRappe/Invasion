package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class HUD {
	
	private Player player;
	private BufferedImage life;
	private int health;
	private int magic;
	
	private Rectangle healthRect;
	private Rectangle healthBorder;
	private Rectangle magicRect;
	private Rectangle magicBorder;
	
	public HUD(Player p) {
		player = p;
		healthRect = new Rectangle(10, 24, health, 2);
		healthBorder = new Rectangle(9, 23, player.getHealth() + 1, 4);
		magicRect = new Rectangle(10, 30, magic, 2);
		magicBorder = new Rectangle(9, 29, player.getMagic() + 1, 4);
		
		try {
			BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/HUD/Hud.png"));
			life = image.getSubimage(0, 12, 12, 11);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		health = player.getHealth() - 1;
		magic = player.getMagic() - 1;
		
		healthRect.setSize(health, 2);
		magicRect.setSize(magic, 2);
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.RED);
		g.fill(healthRect);
		g.draw(healthRect);
		
		g.setColor(Color.GRAY);
		g.draw(magicBorder);
		g.draw(healthBorder);
		
		g.setColor(Color.GREEN);
		g.fill(magicRect);
		g.draw(magicRect);

		for(int i = 0; i < player.getLife(); i++){
			g.drawImage(life, 10 + 12*i, 10, null);
		}
		
	}
}

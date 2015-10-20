package Handlers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import Main.GamePanel;

public class Label {
	
	public BufferedImage image;
	private BufferedImage img;
	public String string;
	
	public int visibleCount;
	private boolean finished;
	private boolean shouldRemove;
	private boolean text;
	Font font;
	
	private double x;
	private double y;
	private double dx;
	
	private int width;
	
	public Label(String s, int fontSize) {
		font = new Font("Arial", Font.PLAIN, fontSize);
		string = s;
		
		/////////////////////////Builds an image from the string to position on the screen//////////////////////////////////////
		img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D) img.getGraphics();
		g2.setFont(font);
		FontRenderContext frc= g2.getFontMetrics().getFontRenderContext();
		Rectangle2D rect = font.getStringBounds(string.toString(), frc);
		g2.dispose();
		img = new BufferedImage((int)Math.ceil(rect.getWidth()), (int)Math.ceil(rect.getHeight()), BufferedImage.TYPE_4BYTE_ABGR);
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		width = img.getWidth();
		x = width;
		finished = false;
		text = true;
	}
	
	public Label(BufferedImage image) {
		this.image = image;
		width = image.getWidth();
		x = 0;
		finished = false;
		text = false;
	}
	
	public boolean shouldRemove() { return shouldRemove; }
	
	public void setY(double y) { this.y = y; }
	
	public void begin() { dx = 10; }
	
	public void update() {
		if(!finished && text) {
			if(x >= (GamePanel.WIDTH - width) / 2 - 10) {
				x = (GamePanel.WIDTH - width) / 2 - 10;
				visibleCount++; 
				if(visibleCount >= 200) finished = true;
			}
			else x += dx;
		}
		if(!finished && !text) {
			if(x >= (GamePanel.WIDTH - width) / 2) {
				x = (GamePanel.WIDTH - width) / 2;
				visibleCount++;
				if(visibleCount >= 200) finished = true;
			}
			else x += dx;
		}
		else {
			x += dx;
			if(x > GamePanel.WIDTH) shouldRemove = true;
		}
	}
	
	public void render(Graphics2D g) {
		if(string != null) {
			g.setColor(Color.WHITE);
			g.setFont(font);
			g.drawString(string, (int)x, (int)y);
		}
		g.drawImage(image, (int)x, (int)y, null);
	}
}

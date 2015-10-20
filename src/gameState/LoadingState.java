package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Audio.SoundPlayer;
import Handlers.Label;
import Main.GamePanel;

public class LoadingState extends GameState implements Runnable {
	
	private int loadingTimer;
	private Label loadingLabel;
	private Thread thread2;
	private boolean running;
	private Color titleColor;
	private Font titleFont;

	public LoadingState(GameStateManager gsm) {
		super(gsm);
		
		titleColor = new Color(255, 255, 255);
		titleFont = new Font("Times New Roman", Font.PLAIN, 16);
		
		running = true;
		if(thread2 == null) {
			thread2 = new Thread(this);
			thread2.start();
		}
		
		init();
	}

	public void init() {
		loadingTimer = 0;
	}

	public void update() {
		loadingTimer++;
		
		if(running == false && loadingTimer == 1200) {
			gsm.setState(GameStateManager.LEVEL1STATE);
			try {
				thread2.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void render(Graphics2D g) {
		Rectangle rect = new Rectangle(0,0, GamePanel.WIDTH, GamePanel.HEIGHT);
		Color myColor = new Color(0, 0, 0);
		g.setColor(myColor);
		g.fill(rect);
		if(loadingLabel != null) loadingLabel.render(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		
		if(loadingTimer > 0)
			g.drawString("One morning you are awoken by a crashing", 10, 20);
		if(loadingTimer > 200)
			g.drawString("You rush to the window to see a startling sight", 10, 40);
		if(loadingTimer > 400)
			g.drawString("Your precious cow is being abducted by a UFO", 10, 60);
		if(loadingTimer > 600)
			g.drawString("You rush outside to attempt to save her.....", 10, 80);
		if(loadingTimer > 800) {
			g.drawString("Remember use left and right arrows to move", 10, 120);
			g.drawString("Use W to jump, use D to use your bat", 10, 140);
			g.drawString("Use A to Dash, use S to use your shield", 10, 160);
			g.drawString("Dash and shield must be unlocked first though", 10, 180);
		}
		if(loadingTimer > 1000)
			g.drawString("Press escape in game to see the controls again", 10, 220);
		
	}

	public void handleInput() {
		return;
	}

	public void run() {
		while(running) {
			SoundPlayer.load("/Music/level1.mp3", "level1");
			running = false;
		}
	}

}

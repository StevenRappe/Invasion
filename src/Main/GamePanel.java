package Main;

import gameState.GameStateManager;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import Audio.SoundPlayer;
import Handlers.Keys;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {
	
	//dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	//game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	//image
	private BufferedImage image;
	private Graphics2D g;
	
	//game state manager
	private GameStateManager gsm;
	
	//builds game window
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	//thread control
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	//init basic needs for the game
	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		
		SoundPlayer.init();
		gsm = new GameStateManager();
	}
	
	//basic game loop controls most the important functions for the running of the game
	//handles threads, rendering, updates, etc.
	public void run() {
		init();
		
		long start;
		long elapsed;
		long wait;
		
		//game loop
		while(running) {
			
			start = System.nanoTime();
			
			update();
			render();
			renderToScreen();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) {
				wait = 5;
			}
			
			try {
				Thread.sleep(wait);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//global call for update of everything
	private void update() {
		gsm.update();
		Keys.update();
	}
	//global call of render for everything
	private void render() {
		gsm.render(g);
	}
	//draw the graphics to the screen
	private void renderToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
	//global key pressed listener/passer
	public void keyPressed(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), true);
	}
	//global key released listener/passer
	public void keyReleased(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), false);
	}
	//not being used but needed.
	public void keyTyped(KeyEvent key) {

	}

}

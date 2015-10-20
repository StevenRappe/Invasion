package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Handlers.Keys;
import Main.GamePanel;

public class MenuState extends GameState {
	
	//private Background bg;
	
	private int currentChoice = 0;
	
	private String[] options = {"Start", "Help", "Quit"};
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
		
		try {
			
			//bg = new Background("/Backgrounds/menubg1.png", 1);
			//bg.setVector(-0.1, 0);
			titleColor = new Color(255, 255, 255);
			titleFont = new Font("Times New Roman", Font.PLAIN, 28);
			font = new Font("Arial", Font.PLAIN, 12);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public void update() {
		//bg.update();
		handleInput();
	}

	public void render(Graphics2D g) {
		//draw background
		g.clearRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		//draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("I N V A S I O N", 70, 70);
		
		//draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.WHITE);
			}
			else {
				g.setColor(Color.red);
			}
			g.drawString(options[i], 145, 140 + i * 15);
		}
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
			//gsm.setState(GameStateManager.TESTSTATE);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.LEVEL1BSTATE);
		}
		if(currentChoice == 2) {
			gsm.setState(GameStateManager.LEVEL2STATE);
		}
		if(currentChoice == 3) {
			gsm.setState(GameStateManager.LEVEL3STATE);
		}
		if(currentChoice == 4) {
			System.exit(0);
		}
	}

	public void handleInput() {
		if(Keys.isPressed(Keys.ENTER)) select();
		if(Keys.isPressed(Keys.UP)) {
			if(currentChoice > 0) {
				currentChoice--;
			}
		}
		if(Keys.isPressed(Keys.DOWN)) {
			if(currentChoice < options.length - 1) {
				currentChoice++;
			}
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}

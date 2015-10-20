package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Handlers.Keys;
import Main.GamePanel;

public class PausedState extends GameState {

	private Font font;
	private Font font2;
	private String mainText;
	private String controlText;
	private String menuText;
	private String control1Text;
	private String control2Text;
	private String control3Text;
	private String control4Text;
	private String control5Text;
	private String control6Text;
	private String control8Text;
	
	private int currentChoice = 0;
	
	private String[] options = {"RETURN TO GAME", "EXIT TO MENU", "QUIT GAME"};
	
	private ArrayList<String> controls;
	
	
	public PausedState(GameStateManager gsm) {
		super(gsm);
		font = new Font("Arial", Font.PLAIN, 26);
		font2 = new Font("Arial", Font.PLAIN, 10);
		controls = new ArrayList<String>();
		initLabels();
	}
	
	public void update() {
		handleInput();
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		
		int length1 = g.getFontMetrics(font).stringWidth(mainText);
		g.drawString(mainText, (GamePanel.WIDTH / 2 - length1 / 2), 30);
		
		g.setFont(font2);
		g.drawString(menuText, 140, 60);
		
		g.drawString(controlText, 135, 135);
		
		for(int i = 0; i < controls.size(); i++) {
			g.setFont(font2);
			//int length = g.getFontMetrics(font2).stringWidth(controls.get(i));
			g.drawString(controls.get(i), 100, 150 + 10 * i);
		}
		
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.WHITE);
			}
			g.drawString(options[i], 100, 75 + i * 15);
		}
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setPaused(false);
		}
		if(currentChoice == 1) {
			gsm.setPaused(false);
			gsm.setState(GameStateManager.MENUSTATE);
		}
		if(currentChoice == 2) {
			System.exit(0);
		}
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(false);
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

	public void init() {
	
	} 
	
	public void initLabels() {
		mainText = "PAUSED";
		
		controlText = "Controls: ";
		
		menuText = "Menu: ";
		
		control1Text = "Left Arrow        =>      Left";
		controls.add(control1Text);
		
		control2Text = "Right Arrow      =>      Right";
		controls.add(control2Text);
		
		control8Text = "S      =>      Shield";
		controls.add(control8Text);
		
		control4Text = "Space Bar        =>      Sprint";
		controls.add(control4Text);
		
		control3Text = "W      =>      Jump";
		controls.add(control3Text);
		
		control5Text = "D       =>      Basic Attack";
		controls.add(control5Text);
		
		control6Text = "A       =>      Dash Attack";
		controls.add(control6Text);
		
		//control7Text = "F       =>      Projectile Attack";
		//controls.add(control7Text);
		

	}
	
}

package gameState;

import Main.GamePanel;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	
	private PausedState paused;
	private boolean isPaused;
	
	public static final int NUMSTATES = 10;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int LEVEL1BSTATE = 2;
	public static final int LEVEL2STATE = 3;
	public static final int LEVEL3STATE = 4;
	
	public static final int LOADSTATE = 5;
	
	public GameStateManager() {
		
		gameStates = new GameState[NUMSTATES];
		
		paused = new PausedState(this);
		isPaused = false;
		
		currentState = MENUSTATE;
		loadState(currentState);
	}
	
	public void loadState(int state) {
		if(state == MENUSTATE) 
			gameStates[state] = new MenuState(this);
		else if(state == LEVEL1STATE) 
			gameStates[state] = new Level1AState(this);
		else if(state == LEVEL1BSTATE) 
			gameStates[state] = new Level1BState(this);
		else if(state == LEVEL2STATE) 
			gameStates[state] = new Level2State(this);
		else if(state == LEVEL3STATE)
			gameStates[state] = new Level3State(this);
		else if(state == LOADSTATE)
			gameStates[state] = new LoadingState(this);
	}
	
	public void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public void setPaused(boolean b) { isPaused = b; }
	
	public void update() {
		if(isPaused) {
			paused.update();
			return;
		}
		if(gameStates[currentState] != null)
			gameStates[currentState].update();
	}
	
	public void render(java.awt.Graphics2D g) {
		if(isPaused) {
			paused.render(g);
			return;
		}
		if(gameStates[currentState] != null)
			gameStates[currentState].render(g);
		else {
			g.setColor(java.awt.Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
	}
}

package gameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import Entity.Cow;
import Entity.Enemy;
import Entity.HUD;
import Entity.HealthOrb;
import Entity.Particle;
import Entity.Player;
import Entity.PlayerReset;
import Entity.Teleport;
import Entity.Enemies.Alien;
import Entity.Enemies.AlienShooter;
import Entity.Enemies.Explode;
import Entity.Enemies.UFO;
import Handlers.Keys;
import Handlers.Label;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1BState extends GameState {
	
	private TileMap tileMap;
	private Background bg;
	private HUD hud;
	
	private Player player;
	private Cow cow;
	private Teleport teleport;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Particle> particles;
	private ArrayList<Explode> explosions;
	private ArrayList<HealthOrb> healthorbs;
	
	private Label chapterText;
	private Label subChapterText;
	
	//events
	private ArrayList<Rectangle> tb;
	private boolean blockInput = false;
	private int eventCount = 0;
	private boolean eventStart;
	private boolean eventEnd;
	private boolean eventDead;
	private boolean abduction;
	private int abductionPlayed = 0;
	private boolean killCow = false;
	
	//set game state manager and init level
	public Level1BState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	//init basic needs of the level
	public void init() {

		//tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/cavetileset.png");
		tileMap.loadMap("/Maps/level1b-map.map");
		tileMap.setposition(0, 0);
		tileMap.setTwitch(1);
		
		//background
		bg = new Background("/Backgrounds/cave.png", .1);
		//bg.setVector(-1, 0);
		
		//create player
		player = new Player(tileMap);
		//player.setPosition(4700, 400);
		player.setPosition(100, 400);
		//player.setTime(PlayerReset.getTime());
		
		//enemies
		enemies = new ArrayList<Enemy>();
		populateEnemies();
		
		//healthorbs
		healthorbs = new ArrayList<HealthOrb>();
		
		//explosions
		explosions = new ArrayList<Explode>();
		
		//particles
		particles = new ArrayList<Particle>();
		
		//init player
		player.init(particles, enemies);
		player.setLife(PlayerReset.getLives());
		
		cow = new Cow(tileMap);
		cow.setPosition(4800, 490);
		
		//create HUD
		hud = new HUD(player);
		
		teleport = new Teleport(tileMap);
		//teleport.setPosition(4800, 490);
		
		//Title / subtitle here
		
		
		//end level here
		
		//event here
		eventStart = true;
		tb = new ArrayList<Rectangle>();
		eventStart();
		
		//sound effects here
		
		//music here
	}
	
	private void populateEnemies() {
		enemies.clear();
		Alien a;
		a = new Alien(tileMap);
		a.setPosition(840, 460);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(4000, 130);
		enemies.add(a);
		
		AlienShooter as;
		as = new AlienShooter(tileMap, player, enemies);
		as.setPosition(1700, 70);
		enemies.add(as);
		
		as = new AlienShooter(tileMap, player, enemies);
		as.setPosition(2000, 490);
		enemies.add(as);
		
		as = new AlienShooter(tileMap, player, enemies);
		as.setPosition(2700, 460);
		enemies.add(as);
		
		as = new AlienShooter(tileMap, player, enemies);
		as.setPosition(3040, 460);
		enemies.add(as);
		
		UFO u;
		u = new UFO(tileMap, player, enemies);
		u.setPosition(4800, 375);
		u.setSpecial(true, 0);
		enemies.add(u);
		
	}

	//update everything
	public void update() {

		handleInput();
		
		if(player.getHealth() == 0 || player.getY() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}
		
		if(teleport.contains(player)) {
			eventEnd = blockInput = true;
		}
		
		if(player.getX() > 4700 && abductionPlayed == 0) {
			abduction = blockInput = true;
		}
		
		
		//events
		if(eventStart) eventStart();
		if(eventDead) eventDead();				
		if(eventEnd) eventEnd();
		if(abduction) abductionEvent(); 
		
		//update player
		player.update();
		
		if(cow != null) {
			cow.update();
		}
		
		//update tilemap
		tileMap.setposition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());
		tileMap.update();
				
		
		//update all enemies
		for(int i = 0; i < enemies.size(); i++) {
		 	Enemy e = enemies.get(i);
			if(e.notOnScreen(player.getX() + 200)) {
				e.update();
				if(e.isDead() || e.shouldRemove()) {
					enemies.remove(i);
					int rand = (int) (Math.random() * 2);
					i--;
					explosions.add(new Explode(tileMap, e.getX(), e.getY()));
					if(rand % 2 == 0 && e.getType() != 5)
						healthorbs.add(new HealthOrb(tileMap, e.getX(), e.getY()));
				}
			}
		}
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		if(healthorbs.size() > 0) {
			for(int i = 0; i < healthorbs.size(); i++) {
				healthorbs.get(i).update();
				if(player.intersects(healthorbs.get(i))) {
					if(player.getHealth() < player.getMaxHealth()) {
						player.setHealth(player.getHealth() + 5);
						if(player.getHealth() > player.getMaxHealth()) {
							player.setHealth(player.getMaxHealth());
						}
					}
					healthorbs.get(i).setRemove();
				}
				if(healthorbs.get(i).shouldRemove()) {
					healthorbs.remove(i);
					i--;
				}
			}
		}
		
		//title / subtitle update
		if(chapterText != null) {
			chapterText.update();
			if(chapterText.shouldRemove()) chapterText = null;
		}
		if(subChapterText != null) {
			subChapterText.update();
			if(subChapterText.shouldRemove()) subChapterText = null;
		}
		
		//update HUD
		hud.update();
		
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).update();
			if(particles.get(i).shouldRemove()) {
				particles.remove(i);
				i--;
			}
		}
		
		//set background movement
		//bg.setPosition(tileMap.getX(), tileMap.getY());
	}
	
	//render everything
	public void render(Graphics2D g) {
		
		//render background
		bg.render(g);
		
		//render tilemap
		tileMap.render(g);
		
		//draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			//if(enemies.get(i).notOnScreen()) break;
			if(enemies.get(i).notOnScreen(player.getX() + 200)) {
				enemies.get(i).render(g);
			}
		}
				
		//draw particles
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).render(g);
		}
		
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).render(g);
		}
		
		for(int i = 0; i < healthorbs.size(); i++) {
			healthorbs.get(i).render(g);
		}
		
		if(cow != null && !killCow) {
			cow.render(g);
		}
		
		//render player
		player.render(g);
		
		teleport.render(g);
		
		//draw HUD
		hud.render(g);
		
		if(chapterText != null) chapterText.render(g);
		if(subChapterText != null) subChapterText.render(g);
		
		
		//draw transitions
		g.setColor(Color.BLACK);
		for(int i = 0; i < tb.size(); i++) {
			g.fill(tb.get(i));
		}
	}
	
	//key listener
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(true);
		if(blockInput || player.getHealth() == 0) return;
		player.setUp(Keys.keyState[Keys.UP]);
		player.setLeft(Keys.keyState[Keys.LEFT]);
		//player.setShield(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.BUTTON1]);
		player.setSprinting(Keys.keyState[Keys.SPACE]);
		if(Keys.isPressed(Keys.BUTTON2))player.setAttack();
		if(Keys.isPressed(Keys.BUTTON4)) player.setDashing();
		//if(Keys.isPressed(Keys.BUTTON3)) player.setFiring();
	}
	
///////////////////////////////////////////////////////
////////////////////EVENTS
///////////////////////////////////////////////////////

	private void reset() {
		enemies.clear();
		explosions.clear();
		healthorbs.clear();
		player.reset();
		player.setPosition(100, 400);
		populateEnemies();
		blockInput = true;
		eventCount = 0;
		eventStart = true;
		eventStart();
	}
	
	//start the level
	private void eventStart() {
		eventCount++;
		if(eventCount == 1) {
			tb.clear();
			tb.add(new Rectangle(0,0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
		}
		if(eventCount > 1 && eventCount < 60) {
			tb.get(0).height -= 4;
			tb.get(1).width -= 6;
			tb.get(2).y += 4;
			tb.get(3).x += 6;
		}
		if(eventCount == 60) {
			chapterText = new Label("CHAPTER 1", 20);
			chapterText.setY(90);
			chapterText.begin();
			
			subChapterText = new Label("The Cave", 20);
			subChapterText.setY(120);
			subChapterText.begin();
			
			eventStart = blockInput = false;
			eventCount = 0;
			tb.clear();
		}	
	}
	
	private void abductionEvent() {
		eventCount++;
		if(eventCount == 1) {
			player.stop();
			tileMap.loadMap("/Maps/level1b.b-map.map");
			tileMap.setposition(0, 0);
			tileMap.setTwitch(1);
		}
		else if(eventCount == 5) {
			player.setEmotion(Player.SURPRISED);
		}
		else if(eventCount == 30) {
			player.setEmotion(Player.NONE);
		}
		else if(eventCount >= 80 && eventCount < 135) {
			cow.setPosition(4800, cow.getY() - 0.5);
		}
		else if(eventCount == 135) {
			killCow = true;
			player.setEmotion(Player.SURPRISED);
		}
		else if(eventCount == 180){
			player.setEmotion(Player.NONE);
		}
		else if(eventCount == 200) {
			teleport.setPosition(4800, 490);
			abduction = blockInput = false;
			eventCount = 0;
			abductionPlayed = 1;
		}
	}
	
	private void eventEnd() {
		eventCount++;
		if(eventCount == 1) {
			player.stop();
		}
		else if(eventCount == 30) {
			tb.add(new Rectangle(0,0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
		}
		if(eventCount == 30) {
			tb.get(0).height -= 4;
			tb.get(1).width -= 6;
			tb.get(2).y += 4;
			tb.get(3).x += 6;
		}
		if(eventCount == 60) {
			eventCount = 0;
			PlayerReset.setHealth(player.getHealth());
			PlayerReset.setLives(player.getLife());
			gsm.setState(GameStateManager.LEVEL2STATE);
			tb.clear();
			eventEnd = blockInput = false;
		}
	}
	
	private void eventDead() {
		eventCount++;
		if(eventCount == 1) {
			player.setDead();
			player.stop();
		}
		if(eventCount == 60) {
			tb.clear();
			tb.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		else if(eventCount > 60) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if(eventCount >= 120) {
			if(player.getLife() == 0) {
				gsm.setState(GameStateManager.MENUSTATE);
			}
			else {
				eventDead = blockInput = false;
				eventCount = 0;
				player.loseLife();
				reset();
			}
		}
	}
	
	
}

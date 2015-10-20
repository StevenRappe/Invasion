package gameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import Audio.SoundPlayer;
import Entity.Enemy;
import Entity.HUD;
import Entity.HealthOrb;
import Entity.Particle;
import Entity.Player;
import Entity.PlayerReset;
import Entity.Enemies.Aliensoldier;
import Entity.Enemies.Fire;
import Entity.Enemies.Explode;
import Handlers.Keys;
import Handlers.Label;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
import Entity.Teleport;

public class Level3State extends GameState {
	
	private TileMap tileMap;
	private Background bg;
	private Background clouds;
	private HUD hud;
	
	private Player player;
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
	
	//set game state manager and init level
	public Level3State(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	//init basic needs of the level
	public void init() {

		//tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/citytileset.gif");
		tileMap.loadMap("/Maps/level3.map");
		tileMap.setposition(0, 0);
		tileMap.setTwitch(1);
		
		//background
		bg = new Background("/Backgrounds/level3bg.png", .1);
		clouds = new Background("/Backgrounds/whiteclouds.png", .1);
		//bg.setVector(-1, 0);
		
		//create player
		player = new Player(tileMap);
		player.setPosition(100, 250);
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
		//player.setLife(PlayerReset.getLives());
		
		//create HUD
		hud = new HUD(player);
		
		
		teleport = new Teleport(tileMap);
		teleport.setPosition(4500, 310);
		
		//Title / subtitle here
		
		
		//end level here
		
		//event here
		eventStart = true;
		tb = new ArrayList<Rectangle>();
		eventStart();
		
		//sound effects here
		SoundPlayer.load("/Sounds/playerDeath.mp3", "playerDeath");
		
		//music here
	}
	
	private void populateEnemies() {
		enemies.clear();
		Fire flame;
		flame = new Fire(tileMap);
		flame.setPosition(130, 255);
		enemies.add(flame);
		
		flame = new Fire(tileMap);
		flame.setPosition(160, 255);
		enemies.add(flame);
		
		flame = new Fire(tileMap);
		flame.setPosition(190, 255);
		enemies.add(flame);
		
		flame = new Fire(tileMap);
		flame.setPosition(610, 315);
		enemies.add(flame);
		
		flame = new Fire(tileMap);
		flame.setPosition(670, 315);
		enemies.add(flame);
		
		flame = new Fire(tileMap);
		flame.setPosition(730, 315);
		enemies.add(flame);
		
		flame = new Fire(tileMap);
		flame.setPosition(790, 315);
		enemies.add(flame);
		
		flame = new Fire(tileMap);
		flame.setPosition(1186, 285);
		enemies.add(flame);
		
		flame = new Fire(tileMap);
		flame.setPosition(2150, 315);
		enemies.add(flame);
		
		flame = new Fire(tileMap);
		flame.setPosition(3764, 165);
		enemies.add(flame);
		
		/*
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
		
		
		Aliensoldier as;
		as = new Aliensoldier(tileMap, player, enemies);
		as.setPosition(1060, 220);
		enemies.add(as);
		
		as = new Aliensoldier(tileMap, player, enemies);
		as.setPosition(1370, 280);
		enemies.add(as);
		
		as = new Aliensoldier(tileMap, player, enemies);
		as.setPosition(1700, 280);
		enemies.add(as);
		
		
		as = new Aliensoldier(tileMap, player, enemies);
		as.setPosition(3100, 100);
		enemies.add(as);
		
		as = new Aliensoldier(tileMap, player, enemies);
		as.setPosition(4400, 310);
		enemies.add(as);*/
		
		
	}

	//update everything
	public void update() {
		
		if(teleport.contains(player)) {
			eventEnd = blockInput = true;
		}
		
		handleInput();
		
		System.out.println (player.getX() +" , "+ player.getY ());
		
		if(player.getHealth() == 0 || player.getY() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}
		
		if(player.getX() > 4600) {
			tileMap.loadMap("/Maps/level1b.b-map.map");
			tileMap.setposition(0, 0);
			tileMap.setTwitch(1);
		}
		
		
		//events
		if(eventStart) eventStart();
		if(eventDead) eventDead();				
		if(eventEnd) eventEnd();
		
		//update player
		player.update();
		
		//update tilemap
		tileMap.setposition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());
		tileMap.update();
				
		
		//update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			//if(enemies.get(i).notOnScreen()) break;
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead() || e.shouldRemove()) {
				enemies.remove(i);
				int rand = (int) (Math.random() * 5);
				i--;
				explosions.add(new Explode(tileMap, e.getX(), e.getY()));
				if(rand % 2 == 0)
					healthorbs.add(new HealthOrb(tileMap, e.getX(), e.getY()));
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
		clouds.setPosition(tileMap.getX(), tileMap.getY());
	}
	
	//render everything
	public void render(Graphics2D g) {
		
		//render background
		bg.render(g);
		clouds.render(g);
		//render tilemap
		tileMap.render(g);
		
		//draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			//if(enemies.get(i).notOnScreen()) break;
			enemies.get(i).render(g);
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
		
		//render player
		player.render(g);
		
		//render door
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
		player.setPosition(100, 240);
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
			chapterText = new Label("CHAPTER 3", 20);
			chapterText.setY(90);
			chapterText.begin();
			
			subChapterText = new Label("The City", 20);
			subChapterText.setY(120);
			subChapterText.begin();
			
			eventStart = blockInput = false;
			eventCount = 0;
			tb.clear();
		}	
	}
	
	private void eventDead() {
		eventCount++;
		if(eventCount == 1) {
			player.setDead();
			SoundPlayer.play("playerDeath");
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
			tb.clear();
			eventCount = 0;
			eventEnd = blockInput = false;
			PlayerReset.setHealth(player.getHealth());
			PlayerReset.setLives(player.getLife());
			gsm.setState(GameStateManager.LEVEL3STATE);
		}
	}
}




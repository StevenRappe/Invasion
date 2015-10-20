package gameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Audio.SoundPlayer;
import Entity.Door;
import Entity.Enemy;
import Entity.HUD;
import Entity.HealthOrb;
import Entity.Particle;
import Entity.Player;
import Entity.PlayerReset;
import Entity.Enemies.Alien;
import Entity.Enemies.Explode;
import Entity.Enemies.UFO;
import Handlers.Keys;
import Handlers.Label;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

/*
 * This class builds the background for game states. 
 * */
public class Level1AState extends GameState {
	
	private TileMap tileMap;
	private Background clouds;
	private Background sky;
	private HUD hud;
	
	//TITLE and POWER UP STUFF
	private Label chapterText;
	private Label subChapterText;
	
	private BufferedImage powerImage;
	private Label powerupLabel;
	private Label dashLabel;
	private Label dashText;
	
	//ENTITIES
	private Player player;
	private Door door;
	private UFO ufo;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Particle> particles;
	private ArrayList<Explode> explosions;
	private ArrayList<HealthOrb> healthorbs;
	
	//events
	private ArrayList<Rectangle> tb;
	private boolean blockInput = false;
	private int eventCount = 0;
	private boolean eventStart;
	private boolean eventEnd;
	private boolean eventDead;
	private boolean eventLanding;
	private int landingPlayed = 0;
	private boolean dashing = false;
	
	//set game state manager and init level
	public Level1AState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	//init basic needs of the level
	public void init() {
		
		//tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/nightgrasstileset.png");
		tileMap.loadMap("/Maps/level1A-map.map");
		tileMap.setposition(0, 0);
		tileMap.setTwitch(1);
		
		//background
		sky = new Background("/Backgrounds/level1.1.png", .1);
		clouds = new Background("/Backgrounds/level1.png", .1);
		//bg.setVector(-1, 0);
		
		//create player
		player = new Player(tileMap);
		player.setPosition(100, 400);
		player.setHealth(PlayerReset.getHealth());
		player.setLife(PlayerReset.getLives());
		//player.setTime(PlayerReset.getTime());
		
		//healthorbs
		healthorbs = new ArrayList<HealthOrb>();
		
		//particles
		particles = new ArrayList<Particle>();
		
		//explosion
		explosions = new ArrayList<Explode>();
		
		//enemies
		enemies = new ArrayList<Enemy>();
		populateEnemies();
		
		//init player
		player.init(particles, enemies);
		
		//create HUD
		hud = new HUD(player);
		
		//Title / subtitle here
		try {
			powerImage = ImageIO.read(
				getClass().getResourceAsStream("/HUD/powerup.png")
			);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//end level here
		door = new Door(tileMap);
		door.setPosition(4440, 430);
		
		//event here
		eventStart = true;
		tb = new ArrayList<Rectangle>();
		eventStart();
		
		//sound effects here
		SoundPlayer.load("/Sounds/ufoLanding.mp3", "ufoLanding");
		SoundPlayer.load("/Sounds/ufoTeleport.mp3", "ufoTeleport");
		SoundPlayer.load("/Sounds/healthUp.mp3", "healthUp");
		SoundPlayer.load("/Sounds/alienDeath.mp3", "alienDeath");
		SoundPlayer.load("/Sounds/playerDeath.mp3", "playerDeath");
		
		//music here
		SoundPlayer.loop("level1", 0);
	}
	
	private void populateEnemies() {
		enemies.clear();
		//create enemies
		
		Alien a;
		a = new Alien(tileMap);
		a.setPosition(690, 250);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(985, 400);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(1170, 400);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(1760, 400);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(2720, 400);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(3200, 430);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(3300, 430);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(3380, 430);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(3580, 370);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(3810, 400);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(4120, 430);
		enemies.add(a);
				
		//ufo
		if(landingPlayed == 0) {
			ufo = new UFO(tileMap, player, enemies);
			ufo.setPosition(2460, 325);
			ufo.setSpecial(true, 1);
			enemies.add(ufo);
		}
	}

	//update everything
	public void update() {
		
		handleInput();
		
		//set background movement
		clouds.setPosition(tileMap.getX(), 0);
		
		//update player
		player.update();
		
		//update tilemap
		tileMap.setposition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());
		tileMap.update();
		
		//update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if(e.notOnScreen(player.getX() + 500)) {
				e.update();
				if(e.isDead() || e.shouldRemove()) {
					if(e.getType() == 1) SoundPlayer.play("alienDeath");
					enemies.remove(i);
					int rand = (int) (Math.random() * 2);
					i--;
					explosions.add(new Explode(tileMap, e.getX(), e.getY()));
					if(rand % 2 == 0 && e.getType() != 5)
						healthorbs.add(new HealthOrb(tileMap, e.getX(), e.getY()));
				}
			}
		}
		
		if(explosions.size() > 0) {
			for(int i = 0; i < explosions.size(); i++) {
				explosions.get(i).update();
				if(explosions.get(i).shouldRemove()) {
					explosions.remove(i);
					i--;
				}
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
					SoundPlayer.play("healthUp");
					healthorbs.remove(i);
					i--;
				}
			}
		}
		
		//title / subtitle update
		if(powerupLabel != null) {
			powerupLabel.update();
			if(powerupLabel.shouldRemove()) powerupLabel = null;
		}
		if(dashLabel != null) {
			dashLabel.update();
			if(dashLabel.shouldRemove()) dashLabel = null;
		}
		if(dashText != null) {
			dashText.update();
			if(dashText.shouldRemove()) dashText = null;
		}
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
		
		if(player.getHealth() == 0 || player.getY() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}
		
		if(door.contains(player)) {
			eventEnd = blockInput = true;
		}
		
		if(player.getX() > 2170 && landingPlayed == 0) {
			eventLanding = blockInput = true;
		}
		
		//events
		if(eventStart) eventStart();
		if(eventDead) eventDead();				
		if(eventEnd) eventEnd();
		if(eventLanding) ufoLanding();
	}
	
	//render everything
	public void render(Graphics2D g) {
		
		//render background
		sky.render(g);
		clouds.render(g);
		
		//render tilemap
		tileMap.render(g);
		
		//draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			if(enemies.get(i).notOnScreen(player.getX() + 500)) {
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
		
		door.render(g);
		
		//render player
		player.render(g);
		
		//NIGHT TIME
		Rectangle rect = new Rectangle(0,0, GamePanel.WIDTH, GamePanel.HEIGHT);
		Color myColor = new Color(0, 0, 0, 100);
		g.setColor(myColor);
		g.fill(rect);
		
		//draw HUD
		hud.render(g);
		
		//labels / titles
		if(powerupLabel != null) powerupLabel.render(g);
		if(dashLabel != null) dashLabel.render(g);
		if(dashText != null) dashText.render(g);
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
		//if(Keys.isPressed(Keys.DOWN)) player.setShield();
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.BUTTON1]);
		player.setSprinting(Keys.keyState[Keys.SPACE]);
		if(Keys.isPressed(Keys.BUTTON2))player.setAttack();
		if(Keys.isPressed(Keys.BUTTON4) && dashing == true) player.setDashing();
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
		player.setPosition(100, 350);
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
			
			subChapterText = new Label("The Farm", 20);
			subChapterText.setY(120);
			subChapterText.begin();
			eventStart = blockInput = false;
			eventCount = 0;
			tb.clear();
		}	
	}
	
	private void ufoLanding() {
		eventCount++;
		if(eventCount == 1) {
			player.stop();
			player.setPosition(2170, player.getY());
		}
		if(eventCount == 30) {
			player.setEmotion(1);
		}
		if(eventCount == 80) {
			SoundPlayer.play("ufoLanding");
		}
		if(eventCount == 100) {
			player.setEmotion(0);
			tileMap.setShaking(true, 5);
		}
		if(eventCount == 200) {
			player.setEmotion(2);
			tileMap.setShaking(false, 0);
		}
		if(eventCount == 300) {
			player.setEmotion(0);
			player.setRight(true);
			player.specialHit(true);
		}
		if(eventCount == 460) {
			player.setEmotion(2);
		}
		if(eventCount == 520) {
			player.setEmotion(0);
			ufo.hit(50);
			SoundPlayer.play("ufoTeleport");
		}
		if(eventCount == 560) {
			player.setEmotion(1);
		}
		if(eventCount == 630) {
			player.setEmotion(0);
			powerupLabel = new Label(powerImage.getSubimage(0, 0, 180, 30));
			powerupLabel.setY(60);
			powerupLabel.begin();
		}
		if(eventCount == 660) {
			dashLabel = new Label(powerImage.getSubimage(0, 30, 110, 30));
			dashLabel.setY(95);
			dashLabel.begin();
		}
		if(eventCount == 740) {
			dashing = true;
			blockInput = eventLanding = false;
			landingPlayed = 1;
			eventCount = 0;
			dashText = new Label("PRESS C TO DASH AT YOUR ENEMIES AND HURT THEM", 10);
			dashText.setY(150);
			dashText.begin();
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
			gsm.setState(GameStateManager.LEVEL1BSTATE);
		}
	}
}

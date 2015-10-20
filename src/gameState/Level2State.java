package gameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import Audio.SoundPlayer;
import Entity.UFODoor;
import Entity.Enemy;
import Entity.HUD;
import Entity.HealthOrb;
import Entity.Particle;
import Entity.Player;
import Entity.PlayerReset;
import Entity.Enemies.AlienShooter;
import Entity.Enemies.TripleShooter;
import Entity.Enemies.JumpingAlien;
import Entity.Enemies.Alien;
import Entity.Enemies.Explode;
import Handlers.Keys;
import Handlers.Label;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

/*
 * This class builds the background for game states. 
 * */
public class Level2State extends GameState {
	
	private TileMap tileMap;
	private Background spaceShip;
	private HUD hud;
	
	//TITLE and POWER UP STUFF
	private Label chapterText;
	private Label subChapterText;
	
	
	//ENTITIES
	private Player player;
	private UFODoor door;
	
	
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
	
	private boolean dashing = true;
	private boolean shield = true;
	
	//set game state manager and init level
	public Level2State(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	//init basic needs of the level
	public void init() {
		
		//tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/level2.png");
		tileMap.loadMap("/Maps/level2-map.map");
		tileMap.setposition(0, 0);
		tileMap.setTwitch(1);
		
		//background
		spaceShip = new Background("/Backgrounds/level2Background.png", .1);
		//bg.setVector(-1, 0);
		
		//create player
		player = new Player(tileMap);
		player.setPosition(100, 300);
		player.setHealth(PlayerReset.getHealth());
		player.setLife(PlayerReset.getLives());
		//player.setTime(PlayerReset.getTime());
		
		//enemies
		enemies = new ArrayList<Enemy>();
		populateEnemies();
		
		//healthorbs
		healthorbs = new ArrayList<HealthOrb>();
		
		//particles
		particles = new ArrayList<Particle>();
		
		//explosion
		explosions = new ArrayList<Explode>();
		
		//init player
		player.init(particles, enemies);
		
		//create HUD
		hud = new HUD(player);
		
		//Title / subtitle here
		/*try {
			powerImage = ImageIO.read(
				getClass().getResourceAsStream("/HUD/powerup.png")
			);
		}
		catch(Exception e) {
			e.printStackTrace();
		}*/
		
		//end level here
		door = new UFODoor(tileMap);
		door.setPosition(7500, 280);
		
		//event here
		eventStart = true;
		tb = new ArrayList<Rectangle>();
		eventStart();
		
	}
	
	private void populateEnemies() {
		enemies.clear();
		//create enemies
		
		Alien a;
		TripleShooter b;
		JumpingAlien c;
		AlienShooter d;
		
		
		a = new Alien(tileMap);
		a.setPosition(200, 300);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(500, 200);
		enemies.add(a);
		
		b = new TripleShooter(tileMap, player, enemies);
		b.setPosition(1000, 250);
		enemies.add(b);
		
		a = new Alien(tileMap);
		a.setPosition(1250, 300);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(1700, 30);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(2150, 200);
		enemies.add(a);
		
		b = new TripleShooter(tileMap, player, enemies);
		b.setPosition(2400, 300);
		enemies.add(b);
		
		b = new TripleShooter(tileMap, player, enemies);
		b.setPosition(2500, 300);
		enemies.add(b);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(2355, 300);
		enemies.add(c);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(2474, 300);
		enemies.add(c);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(2682, 200);
		enemies.add(c);
		
		d = new AlienShooter(tileMap, player, enemies);
		d.setPosition(2800, 200);
		enemies.add(d);
		
		d = new AlienShooter(tileMap, player, enemies);
		d.setPosition(2900, 200);
		enemies.add(d);
		
		d = new AlienShooter(tileMap, player, enemies);
		d.setPosition(3000, 200);
		enemies.add(d);
		
		b = new TripleShooter(tileMap, player, enemies);
		b.setPosition(3500, 300);
		enemies.add(b);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(3545, 250);
		enemies.add(c);
		
		b = new TripleShooter(tileMap, player, enemies);
		b.setPosition(3600, 300);
		enemies.add(b);
		
		a = new Alien(tileMap);
		a.setPosition(3500, 30);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(3650, 30);
		enemies.add(a);

		d = new AlienShooter(tileMap, player, enemies);
		d.setPosition(3800, 30);
		enemies.add(d);
		
		a = new Alien(tileMap);
		a.setPosition(3800, 180);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(4550, 200);
		enemies.add(a);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(4600, 200);
		enemies.add(c);
		
		a = new Alien(tileMap);
		a.setPosition(4650, 200);
		enemies.add(a);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(4700, 200);
		enemies.add(c);
		
		a = new Alien(tileMap);
		a.setPosition(4750, 200);
		enemies.add(a);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(4800, 200);
		enemies.add(c);
		
		a = new Alien(tileMap);
		a.setPosition(4850, 200);
		enemies.add(a);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(4900, 200);
		enemies.add(c);
		
		a = new Alien(tileMap);
		a.setPosition(4500, 300);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(4620, 300);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(4690, 300);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(4750, 300);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(4800, 300);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(4850, 300);
		enemies.add(a);

		a = new Alien(tileMap);
		a.setPosition(4950, 300);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(5350, 200);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(5500, 200);
		enemies.add(a);
		
		a = new Alien(tileMap);
		a.setPosition(5675, 200);
		enemies.add(a);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(5850, 150);
		enemies.add(c);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(5950, 150);
		enemies.add(c);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(6050, 150);
		enemies.add(c);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(7000, 150);
		enemies.add(c);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(7200, 150);
		enemies.add(c);
		
		c = new JumpingAlien(tileMap);
		c.setPosition(7400, 150);
		enemies.add(c);
		
		a = new Alien(tileMap);
		a.setPosition(7500, 150);
		enemies.add(a);
			
	}

	//update everything
	public void update() {
		
		handleInput();
		
		if(player.getHealth() == 0 || player.getY() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}
		
		if(door.contains(player)) {
			eventEnd = blockInput = true;
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
				System.out.println(rand % 2);
				if(rand % 2 == 0 && e.getType() != 5)
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
		
	}
	
	//render everything
	public void render(Graphics2D g) {
		
		//render background
		spaceShip.render(g);
		
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
		if(Keys.isPressed(Keys.DOWN)) player.setShield();
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
		player.reset();
		player.setPosition(100, 300);
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
			chapterText = new Label("CHAPTER 2", 20);
			chapterText.setY(90);
			chapterText.begin();
			
			subChapterText = new Label("Inside the UFO", 20);
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


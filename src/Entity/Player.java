package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Audio.SoundPlayer;
import TileMap.TileMap;

public class Player extends MapObject {

	//player stuff
	private int shieldCount;
	private int health;
	private int maxHealth;
	private int magic;
	private int life;
	private ArrayList<Particle> particles;
	private ArrayList<Enemy> enemies;
	private boolean dead = false;
	private boolean specialHit = false;
	
	public int rheight = 10;
	public int rwidth = 40; 
	
	//sprint
	public boolean sprint;
	
	//shield
	public boolean shield;
	
	//dash attack
	private int dashDamage;
	public boolean dashing;
	public int dashingTick;
	
	//attack
	private int attackDamage;
	private boolean attack;
	private Rectangle attackRect;
	
	//fire
	private boolean firing;
	private int lightningDamage;
	private ArrayList<Lightening> lightening;
	
	//recoil 
	private boolean flinching;
	private int flinchCount;
	private boolean recoil;
	
	//animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = { 1, 15, 3, 2, 6, 6, 6, 1, 2, 15, 2 };
	private final int[] FRAMEWIDTH = { 40, 40, 40, 40, 80, 80, 40, 40, 40, 40, 60 };
	private final int[] FRAMEHEIGHT = { 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40 };
	private final int[] SPRITEDELAY = { -1, 3, 3, 9, 3, 5, 6, -1, 5, 1, 1 };
	
	
	//animations actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int ATTACK = 4;
	private static final int FIRE = 5;
	private static final int SHIELD = 6;
	private static final int DEAD = 7;
	private static final int RECOIL = 8;
	private static final int SPRINT = 9;
	private static final int DASHING = 10;
	
	//emotions
	private BufferedImage surprised;
	private BufferedImage question;
	public static final int NONE = 0;
	public static final int SURPRISED = 1;
	public static final int QUESTION = 2;
	private int emotion;

	public Player(TileMap tMap) {
		super(tMap);

		cWidth = 18;
		cHeight = 40;

		attackRect = new Rectangle((int)x - cWidth / 2, (int)y - cHeight / 2, cWidth, cHeight);
		attackRect.width = 60;
		attackRect.height = 40;
		
		moveSpeed = 0.2;
		maxSpeed = 1.75;
		stopSpeed = 0.4;
		fallSpeed = 0.20;
		maxFallSpeed = 6.0;
		jumpStart = -6.25;
		stopJumpSpeed = 0.5;
		
		lightningDamage = 5;
		dashDamage = 5;
		attackDamage = 10;
		lightening = new ArrayList<Lightening>();
		
		//load sprites
		try {
			
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/Player.png"));
			
			int count = 0;
			sprites = new ArrayList<BufferedImage[]>();
			
			for(int i = 0; i < NUMFRAMES.length; i++) {
				BufferedImage[] bi = new BufferedImage[NUMFRAMES[i]];
				for(int j = 0; j < NUMFRAMES[i]; j++) {
					bi[j] = spriteSheet.getSubimage(
							j * FRAMEWIDTH[i], count, FRAMEWIDTH[i], FRAMEHEIGHT[i]);
				}
				
				sprites.add(bi);
				count += FRAMEHEIGHT[i];
			}
			
			spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/emotions.png"));
			surprised = spriteSheet.getSubimage(0, 0, 15, 20);
			question = spriteSheet.getSubimage(15, 0, 15, 20);
			
		} catch(Exception e) { e.printStackTrace(); }
		
		setAnimation(IDLE);	
		
		SoundPlayer.load("/Sounds/jump.mp3", "playerJump");
		SoundPlayer.load("/Sounds/baseball.mp3", "playerSwing");
		SoundPlayer.load("/Sounds/dash.mp3", "playerDash");
		SoundPlayer.load("/Sounds/recoil.mp3", "playerHurt");
		SoundPlayer.load("/Sounds/shield.mp3", "playerShield");
		SoundPlayer.load("/Sounds/landing.mp3", "playerLanding");
	}
	
	public void init(ArrayList<Particle> particles, ArrayList<Enemy> enemies) {
		this.particles = particles;
		this.enemies = enemies;
		health = maxHealth = 40;
		magic = 25;
		life = 5;
	}
	
	//emotion
	public void setEmotion(int i) {
		emotion = i;
	}
	
	//HEALTH
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public void setHealth(int i) { health = i; }
	
	//MAGIC
	public int getMagic() { return magic; }
	public void setMagic(int i) { magic = i; }
	
	//LIFE
	public int getLife() { return life; }
	public void setLife(int i) { life = i; }
	public void loseLife() { life--; }
	
	//SPRINTING
	public void setSprinting(boolean b) {
		if(!b) sprint = false;
		else if(b && !falling) {
			sprint = true;
		}
	}
	
	public boolean isSprinting() { return sprint; }
	
	//ATTACK
	public void setAttack() { 
		if(recoil) return;
		if(dashing) return;
		if(!attack)
			attack = true;
	}
	public void setFiring() { 
		if(recoil) return;
		if(dashing) return;
		if(magic == 0) return;
		firing = true;
	}
	public void setDashing() {
		if(recoil) return;
		if(!attack && !firing && !shield && !dashing) {
			dashing = true;
			dashingTick = 0;
		}
	}
	
	//Shield
	public void setShield() { 
		if(recoil) return;
		if(magic <= 0) {
			shield = false;
			return;
		}
		shield = true;
	}
	
	//DEAD
	public void setDead() { 
		health = 0;
		stop();
	}
	
	public void stop() {
		flinchCount = 0;
		left = right = up = down = flinching = jumping = attack = firing = sprint = dashing = dead = false;
	}
	
	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAY[currentAction]);
		width = FRAMEWIDTH[currentAction];
		height = FRAMEHEIGHT[currentAction];
	}
	
	public void setCurrentAction(int i) {
		currentAction = i;
	}
	
	public void specialHit(boolean b) {
		specialHit = b;
	}
	
	public void special() {
		stop();
		specialHit = false;
		setShield();
	}
	
	public boolean specialHit() {
		return specialHit;
	}
	
	public void hit(int damage) {
		if(flinching || dead) return;
		if(specialHit) { special(); return;}
		stop();
		health -= 10;
		if(health < 0) health = 0;
		flinching = true;
		flinchCount = 0;
		if(facingRight) dx = -1;
		else dx = 1;
		dy = -3;
		recoil = true;
		falling = true;
		jumping = false;
	}
	
	public void reset() {
		health = maxHealth;
		facingRight = true;
		stop();
		currentAction = IDLE;
	}
	
	private void getNextPosition() {
		
		//recoil from enemy hit
		if(recoil) {
			SoundPlayer.play("playerHurt");
			dy += fallSpeed * 2;
			if(!falling) {
				recoil = false;
			}
			return;
		}
		
		double maxSpeed = this.maxSpeed;
		if(sprint) maxSpeed *= 1.75;
		
		//movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) { dx = -maxSpeed; }
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) { dx = maxSpeed; }
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) { dx = 0;}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) { dx = 0; }
			}
		}
		
		//cannot attack while moving except in air
		if((attack || firing || shield || dead || dashing) && !(jumping || falling)) {
			dx = 0;
		}
		
		if(shield) {
			SoundPlayer.play("playerShield");
			dx = 0;
		}
		if(!shield) {
			SoundPlayer.stop("playerShield");
		}
		
		if(dashing) {
			SoundPlayer.play("playerDash");
			attack = firing = shield= false;
			dashingTick++;
			if(facingRight) dx = moveSpeed * 6 * (7 - dashingTick * 0.07);
			else dx = -moveSpeed * 6 * (7 - dashingTick * 0.07);
		}
		
		if(attack) {
			SoundPlayer.play("playerSwing");
		}
		
		//jumping
		if(jumping && !falling && !shield) {
			SoundPlayer.play("playerJump");
			dy = jumpStart;
			falling = true;
		}
		
		//falling
		else if(falling) {
			dy += fallSpeed;
			if(dy < 0 && !jumping) { dy += stopJumpSpeed; }
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}

	public void update() {
		if(facingRight)
			attackRect.setLocation((int)x - cWidth / 2, (int)y - cHeight / 2);
		else {
			attackRect.setLocation((int)x - cWidth * 2 - 15, (int)y - cHeight / 2);
		}
		//update position
		boolean isFalling = falling;
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);
		if(isFalling && !falling) {
			//SoundPlayer.play("playerLanding");
		}
		
		if(dx == 0) { x = (int)x; }
		
		if(flinching) {
			flinchCount++;
			if(flinchCount > 100) {
				flinching = false;
			}
		}
		
		if(shield) {
			shieldCount++;
			if(shieldCount % 40 == 0) magic--;
		}
		
		if(currentAction == SHIELD) { if(animation.hasPlayedOnce()) shield = false; }
		if(currentAction == ATTACK) { if(animation.hasPlayedOnce()) attack = false; }
		if(currentAction == FIRE) { if(animation.hasPlayedOnce()) firing = false; }
		if(currentAction == DASHING) {
			if(animation.hasPlayed(5)) {
				dashing = false;
			}
			attackRect.y = (int)y - 20;
			if(facingRight) attackRect.x = (int)x - 15;
			else attackRect.x = (int)x - 35;
			
			//particles
			for(int i = 0; i < 3; i++) {
				if(facingRight) particles.add(new Particle(tileMap, (int)(x + getXMap()) + 10, (int)(y + getYMap() + 15), Particle.LEFT, Particle.YELLOW));
				else particles.add(new Particle(tileMap, (int)(x + getXMap()) - 10, (int)(y + getYMap() + 15), Particle.RIGHT, Particle.YELLOW));
			}
		}

		for(int i = 0; i < lightening.size(); i++) {
			lightening.get(i).update();
			if(lightening.get(i).shouldRemove()) {
				lightening.remove(i);
				i--;
			}
		}
		
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			
			if(e.notOnScreen(this.getX() + 200)) {
				if(shield && e.getType() == 1) {
					if(intersects(e)) {
						e.remove = true;
						if(health < maxHealth && health != maxHealth) health += 2;
					}
				}
				
				else if(currentAction == DASHING) {
					if(animation.getFrame() == 1) {
						if(e.intersects(attackRect)) {
							e.hit(dashDamage);
						}
					}
				}
				
				else if(currentAction == ATTACK && animation.getFrame() == 4){
					if(e.intersects(attackRect)) {
						e.hit(attackDamage);
					}
				}
				
				else if(intersects(e) && !dashing) { 
					hit(e.getDamage()); 
				}
				
				for(int j = 0; j < lightening.size(); j++) {
					if(lightening.get(j).intersects(e)) {
						e.hit(lightningDamage);
						lightening.get(j).setHit();
						break;
					}
				}
			}
		}
		
		if(firing && currentAction != FIRE && !attack) {
			Lightening l = new Lightening(tileMap, facingRight);
			l.setPosition(x,y);
			lightening.add(l);
			magic = magic - 5;
		}

		else if(recoil) { if(currentAction != RECOIL) { setAnimation(RECOIL); } }
		
		else if(health == 0) { if(currentAction != DEAD) setAnimation(DEAD); }
		
		else if(attack) { 
			if(currentAction != ATTACK) { 
				SoundPlayer.play("playerSwing");
				setAnimation(ATTACK); } }
		
		else if(firing) { if(currentAction != FIRE) { setAnimation(FIRE); } }
		
		else if(shield) { if(currentAction != SHIELD) { setAnimation(SHIELD); } }
		
		else if(dashing) { if(currentAction != DASHING) { setAnimation(DASHING); } }
			
		else if(dy < 0) { if(currentAction != JUMPING) { setAnimation(JUMPING); } }
		
		else if(dy > 0) { 
			if(currentAction != FALLING && currentAction != RECOIL) { setAnimation(FALLING); } 
		}
		
		else if(sprint && (left || right)) { if(currentAction != SPRINT) { setAnimation(SPRINT); } }
			
		else if(left || right) { if(currentAction != WALKING) { setAnimation(WALKING); } }
			
		else if(currentAction != IDLE) { setAnimation(IDLE); }
		
		animation.update();
		
		if(!attack && !dashing && !recoil && !firing) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		// draw emote
		if(emotion == QUESTION) {
			g.drawImage(question, (int)(x + xMap - cWidth / 2), (int)(y + yMap - 40), null);
		}
		else if(emotion == SURPRISED) {
			g.drawImage(surprised, (int)(x + xMap - cWidth / 2), (int)(y + yMap - 40), null);
		}
		
		for(int i = 0; i < lightening.size(); i++) {
			lightening.get(i).render(g);
		}
		
		if(flinching && !recoil) {
			if(flinchCount % 10 < 5) return;
		}
		
		super.render(g);
	}
}

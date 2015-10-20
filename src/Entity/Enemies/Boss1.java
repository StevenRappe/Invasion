package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Particle;
import Handlers.Content;
import Main.GamePanel;
import TileMap.TileMap;

public class Boss1 extends Enemy {
	
	private BufferedImage[] sprites;
	private BufferedImage[] finalSprites;
	
	private int attackTick;
	private int delayCount;
	private int shotCount;
	private int stepCount;
	
	private TileMap tm;
	private ArrayList<Enemy> enemies;
	private ArrayList<Particle> particles;
	
	private boolean charge;
	private boolean shaking = false;
	
	public Boss1(TileMap tm, ArrayList<Enemy> enemies, ArrayList<Particle> particles) {
		super(tm);
		
		this.tm = tm;
		this.enemies = enemies;
		this.particles = particles;
		
		moveSpeed = 3.0;
		maxSpeed = 7.0;
		fallSpeed = 3.0;
		maxFallSpeed = 7.0;
		jumpStart = -6.0;
		stopJumpSpeed = .5;
		
		attackTick = shotCount = delayCount = stepCount = 0;
		
		width = 60;
		height = 70;
		cWidth = 59;
		cHeight = 70;
		
		health = maxHealth = 40;
		damage = 1;
		
		finalSprites = Content.Boss1[1];
		sprites = Content.Boss1[0];
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		left = right = facingRight = false;
	}
	
	private void getNextPosition() {
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		if(falling) {
			if(!falling) {
				
			}
			else {
				dy += fallSpeed;
				if(dy < 0 && !jumping) dy += stopJumpSpeed;
				if(dy >= maxFallSpeed) dy = maxFallSpeed;
			}
		}
	}
	
	public void update() {
		attackTick++;
		
		if(flinching) {
			flinchTimer++;
			if(flinchTimer == 100) flinching = false;
		}
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);
		
		calculateCorners(x, yDest + 1);
		if(bottomLeft || bottomRight) {
			falling = false;
			dy = 0;
		}
		
		calculateCorners(xDest - 1, y);
		if(left) {
			if(bottomLeft || topLeft) {
				left = false;
				dx = 0;
				facingRight = true;
				shaking = true;
				x = x + 30;
			}
		}
		calculateCorners(xDest + 1, y);
		if(right) {
			if(bottomRight || topRight) {
				right = facingRight = false;
				dx = 0;
				shaking = true;
				x = x - 30;
			}
		}
		
		if(health <= maxHealth / 2) {
			delayCount++;
			if(delayCount == 1) {
				charge = false;
				tm.setShaking(false, 0);
				animation.setFrames(finalSprites);
				//animation.setDelay(9);
			}
			x = GamePanel.WIDTH / 2 + width / 2;
			y = GamePanel.HEIGHT / 2 + width;
			
			if(attackTick % 10 == 0) {
				EnergyFire ef = new EnergyFire(tileMap);
				ef.setType(EnergyFire.VECTOR);
				ef.setPosition(x, y + 10);
				ef.setVector(2 * Math.sin(delayCount / 30), 2 * Math.cos(delayCount / 30));
				enemies.add(ef);

				EnergyFire e = new EnergyFire(tileMap);
				e.setType(EnergyFire.VECTOR);
				e.setPosition(x, y + 10);
				e.setVector(2 * Math.sin(delayCount / 60), 2 * Math.cos(delayCount / 60));
				enemies.add(e);
				
				EnergyFire e2 = new EnergyFire(tileMap);
				e2.setType(EnergyFire.VECTOR);
				e2.setPosition(x, y + 10);
				e2.setVector(2 * Math.sin(delayCount / 90), 2 * Math.cos(delayCount / 90));
				enemies.add(e2);
			}
			delayCount++;
			stepCount = 5;
		}
		
		if(stepCount < 3) {
			if(shaking) {
				if(delayCount == 0) {
					tm.setShaking(true, 9);
				}
				
				delayCount++;
				if(delayCount == 10) {
					shaking = false;
					tm.setShaking(false, 0);
					delayCount = 0;
					shotCount = 0;
				}
			}
			
			if(charge) {
				delayCount++;
				if(delayCount == 1) {
					//animation.setFrames(chargeSprites);
					//animation.setDelay(-1);
				}
				if(delayCount > 65 && delayCount <= 80) {
					if(facingRight) {
						for(int i = 0; i < 3; i++) {
							particles.add(new Particle(tm, (int)(x + getXMap() - 30), (int)(y + getYMap()), Particle.LEFT, Particle.RED));
							particles.add(new Particle(tm, (int)(x + getXMap() - 30), (int)(y + getYMap()), Particle.LEFT, Particle.PURPLE));
						}
					}

					else {
						for(int i = 0; i < 3; i++) {
							particles.add(new Particle(tm, (int)(x + getXMap() + 30), (int)(y + getYMap()), Particle.RIGHT, Particle.RED));
							particles.add(new Particle(tm, (int)(x + getXMap() + 30), (int)(y + getYMap()), Particle.RIGHT, Particle.PURPLE));
						}
					}
				}
				if(delayCount == 80) {
					delayCount = 0;
					charge = false;
					if(facingRight)
						right = true;
					else
						left = true;
				}
			}
			
			if(!charge) {
				//animation.setFrames(sprites);
				//animation.setDelay(-1);
			}
			
			if(attackTick % 60 == 0 && shotCount < 6) {
				EnergyFire ef = new EnergyFire(tileMap);
				ef.setType(EnergyFire.VECTOR);
				ef.setPosition(x, y + 10);
				ef.setVector(-4, -0.01);
					
				EnergyFire ef2 = new EnergyFire(tileMap);
				ef2.setType(EnergyFire.VECTOR);
				ef2.setPosition(x, y + 10);
				ef2.setVector(4, -0.01);
				
				enemies.add(ef);
				enemies.add(ef2);
				shotCount++;
				
				if(shotCount == 5) {
					shotCount = 0;
					stepCount++;
					charge = true;
				}
			}
		}
		
		if(stepCount == 3) {
			delayCount++;
			if(delayCount > 100 && delayCount <= 121) {
				for(int i = 0; i < 20; i++) {
					particles.add(new Particle(tm, (int)(x + getXMap()), (int)(y + getYMap() + 35), Particle.DOWN, Particle.RED));
					particles.add(new Particle(tm, (int)(x + getXMap()), (int)(y + getYMap() + 35), Particle.DOWN, Particle.PURPLE));
				}
			}
			if(delayCount == 120) {
				dy -= 20.0;
			}
			if(delayCount > 145 && delayCount < 170) {
				if(delayCount % 2 == 0) {
					EnergyFire ef = new EnergyFire(tileMap);
					ef.setType(EnergyFire.VECTOR);
					ef.setPosition(x, y + 10);
					ef.setVector(-4, -0.01);
					enemies.add(ef);
					
					EnergyFire ef2 = new EnergyFire(tileMap);
					ef2.setType(EnergyFire.VECTOR);
					ef2.setPosition(x, y + 10);
					ef2.setVector(4, -0.01);
					enemies.add(ef2);
				}
				tm.setShaking(true, 9);
			}
			if(delayCount == 210) {
				tm.setShaking(false, 0);
				stepCount = 0;
				shotCount = 0;
				delayCount = 0;
				attackTick = 0;
			}
		}
		
		
		animation.update();
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		if(flinching) {
			if(flinchTimer % 10 < 5) return;
		}

		super.render(g);
	}
}


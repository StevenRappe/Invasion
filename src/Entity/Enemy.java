package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected boolean remove;
	protected boolean specialType;
	protected int TYPE;
	
	protected boolean flinching;
	protected long flinchTimer;
	
	public Enemy(TileMap tm) {
		super(tm);
		remove = false;
		TYPE = 0;
		specialType = false;
	}
	
	public int getType() { return TYPE; }
	
	public boolean shouldRemove() { return remove; }
	
	public boolean isDead() { return dead; }
	
	public int getDamage() { return damage; }
	
	public boolean getSpecial() { return specialType; }
	
	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = 0;			
	}
	
	public void update() {}
	
}

package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import TileMap.Tile;
import TileMap.TileMap;


/*
 * This is the entity class, it gets extended by any "living" object. That means
 * player, enemy, bosses, anything that moves and has action must extend this!
 * 
 * */
public abstract class MapObject {
	
	//tiles
	protected TileMap tileMap;
	protected int tileSize;
	protected double xMap;
	protected double yMap;
	
	//positions
	protected double x;
	protected double y;
	
	//vector
	protected double dx;
	protected double dy;
	
	//dimensions
	protected int width;
	protected int height;
	
	//Collision box
	protected int cWidth;
	protected int cHeight;
	
	//Collision
	protected int currRow;
	protected int currCol;
	protected double xDest;
	protected double yDest;
	protected double xTemp;
	protected double yTemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	//animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	//movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	protected boolean active;
	
	//movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	//constructor
	public MapObject(TileMap tMap) {
		tileMap = tMap;
		tileSize = tMap.getTileSize();
		animation = new Animation();
		facingRight = true;
	}
	
	//check for collision with other entity(enemy)
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public boolean intersects(Rectangle r) {
		return getRectangle().intersects(r);
	}
	
	public boolean contains(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.contains(r2);
	}
	
	public boolean contains(Rectangle r) {
		return getRectangle().contains(r);
	}
	
	//create rectangle for collision
	public Rectangle getRectangle() {
		return new Rectangle((int)x - cWidth / 2, (int)y - cHeight / 2, cWidth, cHeight);
	}
	
	//calculate collision with walls
	public void calculateCorners(double x, double y) {
		
		int leftTile = (int)(x - cWidth / 2) / tileSize; //this will return the tile at that position
		int rightTile = (int)(x + cWidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cHeight / 2) / tileSize;
		int bottomTile = (int)(y + cHeight / 2 - 1) / tileSize;
		
		if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
				leftTile < 0 || rightTile >= tileMap.getNumCols()) {
				topLeft = topRight = bottomLeft = bottomRight = false;
				return;
		}

		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
		
	}
	
	//check which kind of collision has occurred
	protected void checkTileMapCollision() {
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xDest = x + dx;
		yDest = y + dy;
		
		xTemp = x;
		yTemp = y;
		
		calculateCorners(x, yDest);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				yTemp = currRow * tileSize + cHeight / 2;
			}
			else {
				yTemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				yTemp = (currRow + 1) * tileSize - cHeight / 2;
			}
			else {
				yTemp += dy;
			}
		}
		
		calculateCorners(xDest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xTemp = currCol * tileSize + cWidth / 2;
			}
			else {
				xTemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xTemp = (currCol + 1) * tileSize - cWidth / 2;
			}
			else {
				xTemp += dx;
			}
		}
		
		if(!falling) {
			calculateCorners(x, yDest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}
	
	public int getX() {return (int)x;}
	
	public int getY() {return (int)y;}
	
	public int getWidth() {return width;}
	
	public int getHeight() {return height;}
	
	public int getCWidth() {return cWidth;}
	
	public int getCHeight() {return cHeight;}
	
	public int getXMap() { return (int) xMap; }
	
	public int getYMap() { return (int) yMap; }
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition() {
		xMap = tileMap.getX();
		yMap = tileMap.getY();
	}
	
	public void setLeft(boolean b) { left = b; }
	
	public void setRight(boolean b) { right = b; }
	
	public void setUp(boolean b) { up = b; }
	
	public void setDown(boolean b) { down = b; }
	
	public void setJumping(boolean b) { jumping = b; }
	
	
	//if the entity is off the screen we don't need to render them.
	public boolean notOnScreen(int x) {
		if(x < this.getX()) {
			return false;
		}
		return true;
	}
	
	
	public void render(Graphics2D g) {
		//setMapPosition();
		
		if(facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xMap - width / 2), (int)(y + yMap - height / 2), null);
		}
		
		else {
			g.drawImage(animation.getImage(), (int)(x + xMap - width / 2 + width), (int)(y + yMap - height / 2), -width, height, null);
			
		}
		
		/*g.setColor(Color.BLUE);
		Rectangle r = getRectangle();
		r.x += xMap;
		r.y += yMap;
		g.draw(r);*/
	}
}

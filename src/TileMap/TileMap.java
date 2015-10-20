package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap {
	
	private double x;
	private double y;
	
	private int xMin;
	private int yMin;
	private int xMax;
	private int yMax;
	
	//camera twitch
	private double cameraTwitch;
	
	//map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	//tileset
	private BufferedImage tileSet;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	//drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	//earthquake
	private boolean shaking;
	private int strength;
	
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		cameraTwitch = 0.7;
	}
	
	public void loadTiles(String s) {
		
		try {
			tileSet = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileSet.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			
			//tiles in top row are normal blocks / decoration
			//tiles in bottom row are collision blocks
			BufferedImage subImage;
			for(int col = 0; col < numTilesAcross; col++) {
				subImage = tileSet.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subImage, Tile.NORMAL);
				subImage = tileSet.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subImage, Tile.BLOCKED);
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void loadMap(String s) {
		
		try {
			
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(in));
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xMin = GamePanel.WIDTH - width;
			xMax = 0;
			yMin = GamePanel.HEIGHT - height;
			yMax = 0;
			
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public int getTileSize() { return tileSize; }
	
	public double getX() { return x; }
	
	public double getY() { return y; }
	
	public int getWidth() { return width; }
	
	public int getHeight() { return height; }
	
	public int getNumRows() { return numRows; }
	
	public int getNumCols() { return numCols; }
	
	public boolean isShaking() { return shaking; }
	
	public int getType(int row, int col) {
		
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	
	public void setposition(double x, double y) {
		
		this.x += (x - this.x) * cameraTwitch;
		this.y += (y - this.y) * cameraTwitch;
		
		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
	}
	
	public void setTwitch(double i) { this.cameraTwitch = i; }
	
	public void setShaking(boolean b, int i) { 
		shaking = b; 
		strength = i; 
	}
	
	public void shake(int i, int j) {
		strength = i;
		for(int x = 0; x < j; x++) {
			this.x += Math.random() * strength - strength / 2;
			this.y += Math.random() * strength - strength / 2;
		}
	}
	
	private void fixBounds() {
		if(x < xMin) x = xMin;
		if(x > xMax) x = xMax;
		if(y < yMin) y = yMin;
		if(y > yMax) y = yMax;
	}
	
	public void update() {
		if(shaking) {
			this.x += Math.random() * strength - strength / 2;
			this.y += Math.random() * strength - strength / 2;
		}
	}
	
	public void render(Graphics2D g) {
		
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			
			if(row >= numRows) break;
			
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				
				if(col >= numCols) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);
				
			}
		}
		
	}
}
